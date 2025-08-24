package health.care.medicore.Controllers.v1;

import health.care.medicore.RequestDTO.Doctor.PayoutCredits;
import health.care.medicore.RequestDTO.Doctor.PayoutCredits2;
import health.care.medicore.RequestDTO.Patient.BuyCredits;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Doctor.ExternalAccount;
import health.care.medicore.ResponseDTO.Patient.BuyCreditsDetails;
import health.care.medicore.Services.Patient.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("v1/buyCredits")
    public ResponseEntity<BaseResponse<BuyCreditsDetails>> buyCredits(@RequestBody BuyCredits buyCredits) throws Exception {
        log.info("Executing buyCredits in AppointmentController");
        BaseResponse<BuyCreditsDetails> response = paymentService.buyCredits(buyCredits);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("v1/updateCredits")
    public ResponseEntity<BaseResponse<BuyCreditsDetails>> updateCredits(@RequestBody BuyCredits buyCredits) throws Exception {
        log.info("Executing updateCredits in AppointmentController");
        paymentService.updateCredits(buyCredits);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("v1/payoutCredits")
    public ResponseEntity<BaseResponse<?>> payoutCredits(@RequestBody PayoutCredits payoutCredits) throws Exception {
        log.info("Executing payoutCredits in AppointmentController");
        paymentService.payoutCredits(payoutCredits);
        return new ResponseEntity<>(new BaseResponse<>("Credits Withdraw successfully", null), HttpStatus.OK);
    }

    @PostMapping("v1/payoutCredits2")
    public ResponseEntity<BaseResponse<?>> payoutCredits(@RequestBody PayoutCredits2 payoutCredits) throws Exception {
        log.info("Executing payoutCredits2 in AppointmentController");
        paymentService.payoutCredits(payoutCredits);
        return new ResponseEntity<>(new BaseResponse<>("Credits Withdraw successfully", null), HttpStatus.OK);
    }

    @GetMapping("v1/updateVerification")
    public ResponseEntity<BaseResponse<?>> updateVerification() throws Exception {
        log.info("Executing updateVerification in AppointmentController");
        paymentService.updateVerification();
        return new ResponseEntity<>(new BaseResponse<>("Account Verification Completed", null), HttpStatus.OK);
    }

    @GetMapping("v1/getExternalAccounts")
    public ResponseEntity<BaseResponse<?>> getExternalAccounts() throws Exception {
        log.info("Executing getExternalAccounts in AppointmentController");
        List<ExternalAccount> accountList = paymentService.getExternalAccounts();
        return new ResponseEntity<>(new BaseResponse<>("Accounts Fetched Successfully", accountList), HttpStatus.OK);
    }

    @DeleteMapping("v1/deleteBankAccount")
    public ResponseEntity<BaseResponse<?>> deleteBankAccount() throws Exception {
        log.info("Executing deleteBankAccount in AppointmentController");
        paymentService.deleteBankAccount();
        return new ResponseEntity<>(new BaseResponse<>("Account Deleted Successfully", null), HttpStatus.OK);
    }
}
