package health.care.medicore.Controllers.v1;

import health.care.medicore.RequestDTO.Doctor.PayoutCredits;
import health.care.medicore.RequestDTO.Patient.BuyCredits;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.BuyCreditsDetails;
import health.care.medicore.Services.Patient.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("v1/updateVerification")
    public ResponseEntity<BaseResponse<?>> updateVerification() throws Exception {
        log.info("Executing updateVerification in AppointmentController");
        paymentService.updateVerification();
        return new ResponseEntity<>(new BaseResponse<>("Account Verification Completed", null), HttpStatus.OK);
    }
}
