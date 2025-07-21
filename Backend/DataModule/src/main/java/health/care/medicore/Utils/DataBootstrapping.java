package health.care.medicore.Utils;

import health.care.medicore.Entities.AppConfigs;
import health.care.medicore.Entities.Role;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.AppConfigRepository;
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

    @Autowired
    private AppConfigRepository appConfigRepository;

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

        log.info("*** INSERTING EMAIL OTP TEMPLATE ***");
        if (appConfigRepository.findByName(Constants.EMAIL_OTP_TEMPLATE_NAME) == null){
            AppConfigs appConfigs = new AppConfigs();
            appConfigs.setName(Constants.EMAIL_OTP_TEMPLATE_NAME);
            appConfigs.setValue(Constants.EMAIL_OTP_TEMPLATE);
            appConfigRepository.save(appConfigs);
        }
    }
}
