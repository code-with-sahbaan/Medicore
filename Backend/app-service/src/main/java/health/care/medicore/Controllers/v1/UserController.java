package health.care.medicore.Controllers.v1;

import health.care.medicore.RequestDTO.ForgotPassword;
import health.care.medicore.RequestDTO.PageableRequest;
import health.care.medicore.RequestDTO.SignupRequest;
import health.care.medicore.RequestDTO.VerifyOtpRequest;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.GetCredits;
import health.care.medicore.ResponseDTO.Patient.GetAllConsultants;
import health.care.medicore.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 *A Controller that is accessible to every type of role
 * */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("v1/signup")
    public ResponseEntity<BaseResponse<?>> signup(@RequestBody SignupRequest signupRequest) throws Exception {
        log.info("Executing signup in UserController");
        BaseResponse<?> response = userService.signup(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("v1/verifyOtp")
    public ResponseEntity<BaseResponse<?>> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest) throws Exception {
        log.info("Executing verifyOtp in UserController");
        userService.verifyOtp(verifyOtpRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("v1/forgotPassword")
    public ResponseEntity<BaseResponse<?>> forgotPassword(@RequestBody ForgotPassword forgotPassword) throws Exception {
        log.info("Executing forgotPassword in UserController");
        userService.forgotPassword(forgotPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Above this line all the APIs are non-tokenized. Always add non-tokenized APIs before this line

    @PostMapping("v1/getAllConsultants")
    public ResponseEntity<BaseResponse<Page<GetAllConsultants>>> getAllConsultants(@RequestBody PageableRequest pageableRequest) throws Exception {
        log.info("Executing getAllConsultants in UserController");
        BaseResponse<Page<GetAllConsultants>> response = userService.getUsersByDoctorRole(pageableRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("v1/getMyCredits")
    public ResponseEntity<BaseResponse<GetCredits>> getMyCredits() throws Exception {
        log.info("Executing getMyCredits in UserController");
        BaseResponse<GetCredits> response = userService.getCredits();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
