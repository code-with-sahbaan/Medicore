package health.care.medicore.ServicesImpl.Patient;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import health.care.medicore.Entities.Users;
import health.care.medicore.RequestDTO.Patient.BuyCredits;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.BuyCreditsDetails;
import health.care.medicore.Services.Patient.PaymentService;
import health.care.medicore.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${app.currency}")
    private String currency;

    @Autowired
    private UserService userService;


    @Override
    public BuyCreditsDetails buyCredits(BuyCredits buyCredits) throws Exception {
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
            return buyCreditsDetails;
        }catch (Exception e){
            throw new Exception("Failed to Buy Credits");
        }
    }

    private long updatedAmount(long amount){
        return (long) ((amount * 0.1) + amount); // Adding 10% fees of Platform
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
}
