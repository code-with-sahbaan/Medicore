package health.care.medicore.Services;


import health.care.medicore.Entities.Users;
import health.care.medicore.RequestDTO.SignupRequest;

import java.util.Optional;

public interface UserService{

    Optional<Users> getUserByEmail(String email);

    void signup(SignupRequest signupRequest) throws Exception;
}
