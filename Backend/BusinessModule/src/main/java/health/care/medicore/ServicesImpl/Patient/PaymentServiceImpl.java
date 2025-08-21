package health.care.medicore.ServicesImpl.Patient;

import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.param.*;
import health.care.medicore.Entities.Users;
import health.care.medicore.RequestDTO.Doctor.PayoutCredits;
import health.care.medicore.RequestDTO.Patient.BuyCredits;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.BuyCreditsDetails;
import health.care.medicore.Services.Patient.PaymentService;
import health.care.medicore.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${app.currency}")
    private String currency;

    @Value("${app.platform.charges.percentage}")
    private int platformChargesPercentage;

    @Autowired
    private UserService userService;


    @Override
    public BaseResponse<BuyCreditsDetails> buyCredits(BuyCredits buyCredits) throws Exception {
        try{
            long quantity = buyCredits.getCredits();
            long amount = quantity * 100L; // $1 = 100 cents

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(updatedAmount(amount))
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
        int platformFee = platformChargesPercentage / 100;
        return (amount * platformFee) + amount; // Adding 10% fees of Platform
    }

    private long updatedAmountForPayout(long amount){
        int platformFee = platformChargesPercentage / 100;
        return amount - (amount * platformFee); // Reducing 10% fees of Platform
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
            }else{
                String accountId = users.getStripeAccountId();
                account = Account.retrieve(accountId);
            }

            /* CONNECTING BANK ACCOUNT WITH STRIPE */
            ExternalAccountCollectionCreateParams externalAccountCollectionCreateParams = ExternalAccountCollectionCreateParams.builder()
                    .setExternalAccount(payoutCredits.getBankToken()).build();
            ExternalAccount externalAccount = account.getExternalAccounts().create(externalAccountCollectionCreateParams);
            /* PAYING OUT CREDITS */
            PayoutCreateParams payoutCreateParams =  PayoutCreateParams.builder()
                    .setAmount(updatedAmountForPayout(payoutCredits.getCredits())) // converting cents to actual amount
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
            throw new Exception("Failed to Payout Credits");
        }
    }
}
