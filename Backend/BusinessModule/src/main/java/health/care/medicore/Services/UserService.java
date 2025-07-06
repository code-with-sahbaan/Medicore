package health.care.medicore.Services;


import health.care.medicore.Entities.Users;
import health.care.medicore.RequestDTO.SignupRequest;
import health.care.medicore.RequestDTO.VerifyOtpRequest;
import health.care.medicore.ResponseDTO.BaseResponse;

import java.util.Optional;

public interface UserService{

    Optional<Users> getUserByEmail(String email);

    BaseResponse signup(SignupRequest signupRequest) throws Exception;

    void verifyOtp(VerifyOtpRequest verifyOtpRequest) throws Exception;
}
