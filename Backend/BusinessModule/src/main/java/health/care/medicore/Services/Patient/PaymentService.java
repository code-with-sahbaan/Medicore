package health.care.medicore.Services.Patient;

import health.care.medicore.RequestDTO.Doctor.PayoutCredits;
import health.care.medicore.RequestDTO.Doctor.PayoutCredits2;
import health.care.medicore.RequestDTO.Patient.BuyCredits;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Doctor.ExternalAccount;
import health.care.medicore.ResponseDTO.Patient.BuyCreditsDetails;

import java.util.List;

public interface PaymentService {

    BaseResponse<BuyCreditsDetails> buyCredits(BuyCredits buyCredits) throws Exception;

    void updateCredits(BuyCredits buyCredits) throws Exception;

    void payoutCredits(PayoutCredits payoutCredits) throws Exception;

    void updateVerification() throws  Exception;

    List<ExternalAccount> getExternalAccounts() throws Exception;

    void payoutCredits(PayoutCredits2 payoutCredits) throws Exception;

    void deleteBankAccount() throws Exception;
}
