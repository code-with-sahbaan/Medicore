package health.care.medicore.Utils;

import health.care.medicore.Entities.AppConfigs;
import health.care.medicore.Entities.Appointments;
import health.care.medicore.Entities.Patient.Workout;
import health.care.medicore.Entities.Role;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.AppConfigRepository;
import health.care.medicore.Repositories.AppointmentsRepository;
import health.care.medicore.Repositories.Patient.WorkoutRepository;
import health.care.medicore.Repositories.RoleRepository;
import health.care.medicore.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

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

        log.info("*** INSERTING PATIENT AND DOCTOR WITH APPOINTMENT AND WORKOUT USER FOR TESTING ***");
        if (
                userRepository.findByEmail("sahbaanalam34@gmail.com").isEmpty()
                && userRepository.findByEmail("sahbaanalam25@gmail.com").isEmpty()
        ){

            // Inserting Patient

            Users user = new Users();
            user.setIsActive(true);
            user.setPassword(new BCryptPasswordEncoder().encode("123456789"));
            user.setRole(roleRepository.findByRoleIgnoreCase(Constants.PATIENT));
            user.setEmail("sahbaanalam34@gmail.com");
            user.setCredits(200);
            user.setFullName("Sahbaan Alam");
            Users saved1 = userRepository.save(user);

            // Inserting Doctor

            Users user2 = new Users();
            user2.setIsActive(true);
            user2.setPassword(new BCryptPasswordEncoder().encode("123456789"));
            user2.setRole(roleRepository.findByRoleIgnoreCase(Constants.PATIENT));
            user2.setEmail("sahbaanalam25@gmail.com");
            user2.setCredits(200);
            user2.setFullName("Sahbaan Alam - Doctor");
            Users saved2 = userRepository.save(user2);

            // Inserting Appointment

            Appointments appointments = new Appointments();
            appointments.setAppointmentDateTime(LocalDateTime.now().plusHours(2));
            appointments.setPatient(saved1);
            appointments.setDoctor(saved2);
            appointments.setAppointmentDuration(30);
            appointmentsRepository.save(appointments);

            // Inserting Workout for Patient

            Workout workout = new Workout();
            workout.setWorkoutDate(LocalDate.now());
            LocalDateTime start = LocalDateTime.now().plusHours(2);
            workout.setStart(start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            workout.setEnd(start.plusMinutes(40).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            workout.setTitle("Running");
            workout.setUsers(saved1);
            workoutRepository.save(workout);

        }

        log.info("*** INSERTING EMAIL OTP TEMPLATE ***");
        if (appConfigRepository.findByName(Constants.EMAIL_OTP_TEMPLATE_NAME) == null){
            AppConfigs appConfigs = new AppConfigs();
            appConfigs.setName(Constants.EMAIL_OTP_TEMPLATE_NAME);
            appConfigs.setValue(Constants.EMAIL_OTP_TEMPLATE);
            appConfigRepository.save(appConfigs);
        }

        log.info("*** INSERTING FORGOT PASSWORD OTP TEMPLATE ***");
        if (appConfigRepository.findByName(Constants.FORGOT_PASSWORD_OTP_TEMPLATE_NAME) == null){
            AppConfigs appConfigs = new AppConfigs();
            appConfigs.setName(Constants.FORGOT_PASSWORD_OTP_TEMPLATE_NAME);
            appConfigs.setValue(Constants.FORGOT_PASSWORD_OTP_TEMPLATE);
            appConfigRepository.save(appConfigs);
        }
    }
}
