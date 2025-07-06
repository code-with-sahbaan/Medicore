package health.care.medicore.ServicesImpl;

import health.care.medicore.Entities.AppConfigs;
import health.care.medicore.Entities.Role;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.AppConfigRepository;
import health.care.medicore.Repositories.UserRepository;
import health.care.medicore.RequestDTO.SignupRequest;
import health.care.medicore.RequestDTO.VerifyOtpRequest;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.Services.RoleService;
import health.care.medicore.Services.UserService;
import health.care.medicore.Utils.Constants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl extends GenericServiceImpl<Users> implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailSender;

    @Value("${spring.application.name}")
    private String appName;

    public UserServiceImpl() {
        super(Users.class);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Loading user by email
        Optional<Users> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            // Throwing error if user is not found
            throw new UsernameNotFoundException("User with this email does not exist");
        } else{
            // if user found, collecting authorities and returning User object
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.get().getRole().getRole()));
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
        }
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        // finding user by email
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public BaseResponse signup(SignupRequest signupRequest) throws Exception {
        /*
        * Checking if user exists with this email
        * */
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()){
            throw new IllegalArgumentException("User Already Exists with this Email");
        }
        Users users = convertDtoToEntity(signupRequest);
        users.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        Role role = roleService.getByRole(signupRequest.getRole());
        users.setRole(role);
        Users savedUser = userRepository.save(users);
        sendOTP(savedUser);
        return new BaseResponse("Account Created Successfully", null);
    }

    @Override
    public void verifyOtp(VerifyOtpRequest verifyOtpRequest) throws Exception {
        Users users = userRepository.findByEmail(verifyOtpRequest.getEmail()).get();
        if (!users.getEmailOTP().equals(verifyOtpRequest.getOtp())){
            throw new Exception("Verification failed due to incorrect OTP");
        }
    }

    public void sendOTP(Users users) throws MessagingException, UnsupportedEncodingException {
        // Creating OTP
        Random r = new Random(System.currentTimeMillis());
        int RandomCode = (10000 + r.nextInt(20000));
        String otp = Integer.toString(RandomCode);
        users.setEmailOTP(otp);
        userRepository.save(users);
        // Sending Email
        /* GENERATING EMAIL */
        AppConfigs appConfigs = appConfigRepository.findByName(Constants.EMAIL_OTP_TEMPLATE_NAME);
        String fullName = users.getFullName();
        String toEmail = users.getEmail();
        String fromEmail = emailSender;
        String subject = "User Activation";
        String content = appConfigs.getValue();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(fromEmail, appName);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setTo(toEmail);
        content = content.replace("[[name]]", fullName);
        content = content.replace("[[code]]", Integer.toString(RandomCode));
        mimeMessageHelper.setText(content, true);
        mailSender.send(mimeMessage);
    }
}
