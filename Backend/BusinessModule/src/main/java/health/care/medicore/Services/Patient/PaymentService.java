package health.care.medicore.Services.Patient;

import com.stripe.model.Account;
import health.care.medicore.RequestDTO.Doctor.PayoutCredits;
import health.care.medicore.RequestDTO.Patient.BuyCredits;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.BuyCreditsDetails;

public interface PaymentService {

    public BaseResponse<BuyCreditsDetails> buyCredits(BuyCredits buyCredits) throws Exception;

    public void updateCredits(BuyCredits buyCredits) throws Exception;

    public void payoutCredits(PayoutCredits payoutCredits) throws Exception;

    void updateVerification() throws  Exception;
}
