package health.care.medicore.Controllers.v1;

import health.care.medicore.RequestDTO.SignupRequest;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *A Controller that is accessible to every type of role
 * */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signup(@RequestBody SignupRequest signupRequest) throws Exception {
        BaseResponse response = userService.signup(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
