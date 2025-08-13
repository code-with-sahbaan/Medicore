package health.care.medicore.Services.Patient;

import health.care.medicore.RequestDTO.Patient.BuyCredits;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.BuyCreditsDetails;

public interface PaymentService {

    public BaseResponse<BuyCreditsDetails> buyCredits(BuyCredits buyCredits) throws Exception;

    public void updateCredits(BuyCredits buyCredits) throws Exception;
}
