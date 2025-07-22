package health.care.medicore.Controllers.v1.Patient;

import health.care.medicore.RequestDTO.SignupRequest;
import health.care.medicore.ResponseDTO.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dashboard")
@Slf4j
public class DashboardController extends PatientController {

    @PostMapping("getDashboardData")
    public ResponseEntity<BaseResponse> getDashboardData(@RequestBody SignupRequest signupRequest) throws Exception {
        log.info("Executing getDashboardData in DashboardController");
        BaseResponse response = userService.signup(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
