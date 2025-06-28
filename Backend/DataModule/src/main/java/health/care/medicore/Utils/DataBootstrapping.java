package health.care.medicore.Utils;

import health.care.medicore.Entities.Role;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.RoleRepository;
import health.care.medicore.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Configuration
@Slf4j
public class DataBootstrapping implements CommandLineRunner {


    private static final String[] roles = new String[]{
            Constants.OWNER,
            Constants.ADMIN,
            Constants.DOCTOR,
            Constants.NURSE,
            Constants.PATIENT,
            Constants.RECEPTION
    };

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        log.info("*** CHECKING ROLES IN DB ***");
        List<Role> roleList = roleRepository.findAll();
        if (roleList.size() != roles.length) {
            for (int i = roleList.size(); i < roles.length; i++) {
                Role role = new Role();
                role.setRole(roles[i]);
                roleRepository.save(role);
            }
        }

        log.info("*** ADDING DEFAULT USER IN DB ***");
        /*
         * Adding a user for testing with
         * email: abc@example.com
         * password: password
         * */
        Users users = new Users();
        users.setEmail("abc@example.com");
        users.setPassword(new BCryptPasswordEncoder().encode("password"));
        users.setFullName("My User 01");
        users.setRole(roleRepository.findByRoleIgnoreCase(Constants.OWNER));
        userRepository.save(users);
    }
}
