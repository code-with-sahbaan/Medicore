package health.care.medicore.Services;


import health.care.medicore.Entities.Users;

import java.util.Optional;

public interface UserService{

    Optional<Users> getUserByEmail(String email);

    void doSomething() throws Exception;
}
