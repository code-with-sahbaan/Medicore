package health.care.medicore.ServicesImpl.Patient;

import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.param.*;
import health.care.medicore.Entities.Users;
import health.care.medicore.RequestDTO.Doctor.PayoutCredits;
import health.care.medicore.RequestDTO.Doctor.PayoutCredits2;
import health.care.medicore.RequestDTO.Patient.BuyCredits;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Doctor.ExternalAccount;
import health.care.medicore.ResponseDTO.Patient.BuyCreditsDetails;
import health.care.medicore.Services.Patient.PaymentService;
import health.care.medicore.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${app.currency}")
    private String currency;

    @Value("${app.platform.charges.percentage}")
    private int platformChargesPercentage;

    @Value("${cors.allowed.origins}")
    private String appUrl;

    @Autowired
    private UserService userService;


    @Override
    public BaseResponse<BuyCreditsDetails> buyCredits(BuyCredits buyCredits) throws Exception {
        try{
            long quantity = buyCredits.getCredits();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(updatedAmount(quantity))
                    .setCurrency(currency)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                    )
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            BuyCreditsDetails buyCreditsDetails = new BuyCreditsDetails();
            buyCreditsDetails.setClientSecret(intent.getClientSecret());
            return new BaseResponse<>("Payment Intent Created", buyCreditsDetails);
        }catch (Exception e){
            throw new Exception("Failed to Buy Credits");
        }
    }

    private long updatedAmount(long amount){
        double platformFee = 1 + (platformChargesPercentage / 100.00);
        return Math.round(amount * platformFee * 100); // Adding 10% fees of Platform + converting usd into cents
    }

    private long updatedAmountForPayout(long amount){
        double platformFee = 1 - (platformChargesPercentage / 100.00);
        return Math.round(amount * platformFee * 100); // Reducing 10% fees of Platform + converting usd into cents
    }

    @Override
    public void updateCredits(BuyCredits buyCredits) throws Exception {
        try{
            Users users = userService.getCurrentUser();
            long credits = users.getCredits();
            credits += buyCredits.getCredits();
            users.setCredits(credits);
            userService.updateUser(users);
        }catch (Exception e){
            throw new Exception("Failed to Buy Credits");
        }
    }

    public Account createConnectedAccount(String email, String country, String accountHolderType) throws Exception {
        try{
            AccountCreateParams params = AccountCreateParams.builder()
                    .setType(AccountCreateParams.Type.CUSTOM) // EXPRESS also works
                    .setCountry(country)
                    .setEmail(email)
                    .putExtraParam("capabilities[transfers][requested]", true)
                    .putExtraParam("settings[payouts][schedule][interval]", "manual")
                    .setBusinessType(accountHolderType.equalsIgnoreCase("individual")? AccountCreateParams.BusinessType.INDIVIDUAL : AccountCreateParams.BusinessType.COMPANY)
                    .build();

            return Account.create(params);
        }catch (Exception e){
            throw new Exception("Failed to Create Connected Account");
        }
    }

    @Override
    public void payoutCredits(PayoutCredits payoutCredits) throws Exception {
        try{
            Users users = userService.getCurrentUser();
            if (users.getCredits() < payoutCredits.getCredits()) {
                throw new Exception("Payout Credit Not Enough");
            }
            /* CREATING STRIPE ACCOUNT */
            Account account;
            if (Objects.isNull(users.getStripeAccountId())){
                account = createConnectedAccount(users.getEmail(), payoutCredits.getCountry(), payoutCredits.getAccount_holder_type());
                users.setStripeAccountId(account.getId());
                userService.updateUser(users);
                /* CONNECTING BANK ACCOUNT WITH STRIPE */
                if (account.getExternalAccounts().getData().isEmpty()){
                    ExternalAccountCollectionCreateParams externalAccountCollectionCreateParams = ExternalAccountCollectionCreateParams.builder()
                            .setExternalAccount(payoutCredits.getBankToken()).build();
                    account.getExternalAccounts().create(externalAccountCollectionCreateParams);
                }
                AccountLinkCreateParams params =
                        AccountLinkCreateParams.builder()
                                .setAccount(account.getId())
                                .setRefreshUrl(appUrl + "/doctor/payout") // where to send if they abandon
                                .setReturnUrl(appUrl +
                                        "/doctor/payout?verificationStatus=completed&bankToken="
                                        + payoutCredits.getBankToken()
                                        + "&credits=" + payoutCredits.getCredits()
                                        + "&currency=" + payoutCredits.getCurrency()) // where to send after finishing
                                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING) // for initial onboarding
                                .build();

                AccountLink accountLink = AccountLink.create(params);
                throw new Exception(accountLink.getUrl());
            }else{
                String accountId = users.getStripeAccountId();
                account = Account.retrieve(accountId);
                if (!users.getIsVerificationCompleted()){
                    AccountLinkCreateParams params =
                            AccountLinkCreateParams.builder()
                                    .setAccount(accountId)
                                    .setRefreshUrl(appUrl + "/doctor/payout") // where to send if they abandon
                                    .setReturnUrl(appUrl +
                                            "/doctor/payout?verificationStatus=completed&bankToken="
                                            + payoutCredits.getBankToken()
                                            + "&credits=" + payoutCredits.getCredits()
                                            + "&currency=" + payoutCredits.getCurrency()) // where to send after finishing
                                    .setType(AccountLinkCreateParams.Type.ACCOUNT_UPDATE) // 👈 request missing info
                                    .build();
                    AccountLink accountLink = AccountLink.create(params);
                    throw new Exception(accountLink.getUrl());
                }
            }

            long creditsToTransfer = updatedAmountForPayout(payoutCredits.getCredits());
            /* TRANSFER AMOUNT FROM PLATFORM STRIPE TO CONNECTED ACCOUNT */
            TransferCreateParams transferParams = TransferCreateParams.builder()
                    .setAmount(creditsToTransfer) // in cents
                    .setCurrency(payoutCredits.getCurrency())
                    .setDestination(account.getId()) // connected account ID
                    .build();

            Transfer transfer = Transfer.create(transferParams);

            /* PAYING OUT CREDITS */
            PayoutCreateParams payoutCreateParams =  PayoutCreateParams.builder()
                    .setAmount(creditsToTransfer)
                    .setCurrency(payoutCredits.getCurrency())
                    .build();
            Payout.create(payoutCreateParams,
                    RequestOptions.builder()
                            .setStripeAccount(account.getId())
                            .build());

            /* UPDATING USER CREDITS */
            long updatedCredits = users.getCredits() - payoutCredits.getCredits();
            users.setCredits(updatedCredits);
            userService.updateUser(users);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public void payoutCredits(PayoutCredits2 payoutCredits) throws Exception {
        try{
            Users users = userService.getCurrentUser();
            if (users.getCredits() < payoutCredits.getCredits()) {
                throw new Exception("Payout Credit Not Enough");
            }
            Account account = Account.retrieve(users.getStripeAccountId());
            BankAccount externalAccount = (BankAccount) account.getExternalAccounts().getData().get(0);
            long creditsToTransfer = updatedAmountForPayout(payoutCredits.getCredits());
            /* TRANSFER AMOUNT FROM PLATFORM STRIPE TO CONNECTED ACCOUNT */
            TransferCreateParams transferParams = TransferCreateParams.builder()
                    .setAmount(creditsToTransfer) // in cents
                    .setCurrency(externalAccount.getCurrency())
                    .setDestination(account.getId()) // connected account ID
                    .build();

            Transfer.create(transferParams);

            /* PAYING OUT CREDITS */
            PayoutCreateParams payoutCreateParams =  PayoutCreateParams.builder()
                    .setAmount(creditsToTransfer)
                    .setCurrency(externalAccount.getCurrency())
                    .build();
            Payout.create(payoutCreateParams,
                    RequestOptions.builder()
                            .setStripeAccount(account.getId())
                            .build());

            /* UPDATING USER CREDITS */
            long updatedCredits = users.getCredits() - payoutCredits.getCredits();
            users.setCredits(updatedCredits);
            userService.updateUser(users);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public void updateVerification()  throws  Exception{
        try{
            Users users = userService.getCurrentUser();
            users.setIsVerificationCompleted(true);
            userService.updateUser(users);
        }catch (Exception e){
            throw new Exception("Failed to Update Verification");
        }
    }

    @Override
    public List<ExternalAccount> getExternalAccounts() throws Exception {
        try{
            Users users = userService.getCurrentUser();
            if (users.getStripeAccountId() != null){
                Account account = Account.retrieve(users.getStripeAccountId());
                BankAccount bankAccount = (BankAccount) account.getExternalAccounts().getData().get(0);
                ExternalAccount externalAccount = new ExternalAccount();
                externalAccount.setCountry(bankAccount.getCountry());
                externalAccount.setCurrency(bankAccount.getCurrency());
                externalAccount.setLast4(bankAccount.getLast4());
                externalAccount.setRoutingNumber(bankAccount.getRoutingNumber());

                List<ExternalAccount> externalAccounts = new ArrayList<>();
                externalAccounts.add(externalAccount);
                return externalAccounts;
            }
        }catch (Exception e){
            throw new Exception("Failed to get external accounts");
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteBankAccount() throws Exception {
        try{
            Users users = userService.getCurrentUser();
            Account account = Account.retrieve(users.getStripeAccountId());
            BankAccount bankAccount = (BankAccount) account.getExternalAccounts().getData().get(0);
            bankAccount.delete();
        }catch (Exception e){
            throw new Exception("Failed to delete bank account");
        }
    }
}
