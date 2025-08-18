package health.care.medicore.ServicesImpl;

import health.care.medicore.Entities.Role;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.UserRepository;
import health.care.medicore.RequestDTO.*;
import health.care.medicore.ResponseDTO.Doctor.DoctorProfile;
import health.care.medicore.ResponseDTO.Patient.PatientProfile;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.GetCredits;
import health.care.medicore.ResponseDTO.Patient.GetAllConsultants;
import health.care.medicore.Services.AppConfigService;
import health.care.medicore.Services.QueueService;
import health.care.medicore.Services.RoleService;
import health.care.medicore.Services.UserService;
import health.care.medicore.Utils.Constants;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl extends GenericServiceImpl<Users> implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private QueueService queueService;

    @Value("${medicore.email.sender}")
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
    public BaseResponse<?> signup(SignupRequest signupRequest) throws Exception {
        /*
        * Checking if user exists with this email
        * */
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()){
            throw new IllegalArgumentException("User Already Exists with this Email");
        }
        try{
            Users users = convertDtoToEntity(signupRequest);
            users.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
            Role role = roleService.getByRole(signupRequest.getRole());
            users.setRole(role);
            Users savedUser = userRepository.save(users);
            /* Sending OTP via Email */
            sendOTP(savedUser);
            return new BaseResponse<>("Account Created Successfully", null);
        } catch (Exception e) {
            throw new Exception("Failed to Signup");
        }
    }

    @Override
    public void verifyOtp(VerifyOtpRequest verifyOtpRequest) throws Exception {
        Users users = userRepository.findByEmail(verifyOtpRequest.getEmail()).get();
        if (verifyOtpRequest.getVerificationType().equalsIgnoreCase("profileActivation")){
            // Profile Verification
            if (!users.getEmailOTP().equals(verifyOtpRequest.getOtp())){
                throw new Exception("Verification failed due to incorrect OTP");
            }
            users.setIsActive(true);
        }else{
            // Password Reset
            if (!users.getForgotPasswordOTP().equals(verifyOtpRequest.getOtp())){
                throw new Exception("Verification failed due to incorrect OTP");
            }
            users.setPassword(new BCryptPasswordEncoder().encode(verifyOtpRequest.getPassword()));
        }
        userRepository.save(users);
    }

    @Override
    public Users getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserByEmail(email).get();
    }

    @Override
    public void forgotPassword(ForgotPassword forgotPassword) throws Exception {
        try{
            sendForgotPasswordOTP(getUserByEmail(forgotPassword.getEmail()).get());
        } catch (Exception e) {
            throw new Exception("failed to reset password");
        }
    }

    @Override
    public BaseResponse<Page<GetAllConsultants>> getUsersByDoctorRole(PageableRequest pageableRequest) throws Exception {
        try{
            Pageable pageable = getPageable(pageableRequest);
            return new BaseResponse<>("Consultants fetched successfully", userRepository.getUsersByDoctorRole(roleService.getByRole(Constants.DOCTOR), pageable));
        }catch (Exception e){
            throw new Exception("Failed to fetch Consultants");
        }

    }

    @Override
    public void updateUser(Users users) throws Exception {
        userRepository.save(users);
    }

    @Override
    public BaseResponse<GetCredits> getCredits() throws Exception {
        GetCredits getCredits = new GetCredits();
        long credits = getCurrentUser().getCredits();
        getCredits.setCredits(credits);
        return new BaseResponse<>("Credits fetched successfully", getCredits);
    }

    @Override
    public BaseResponse<PatientProfile> getUserProfile() throws Exception {
        try{
            Users users = getCurrentUser();
            if (users.getRole().getRole().equals(Constants.DOCTOR)){
                return new BaseResponse<>("User data fetched successfully", userRepository.getDoctorProfile(users.getUserId()));
            }else{
                return new BaseResponse<>("User data fetched successfully", userRepository.getPatientProfile(users.getUserId()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve user profile");
        }
    }

    @Override
    public BaseResponse<?> updateProfile(DoctorProfile doctorProfile) throws Exception {
        try{
            Users users = getCurrentUser();
            BeanUtils.copyProperties(doctorProfile, users);
            /* Converting full Dates into Time */
            if (users.getRole().getRole().equals(Constants.DOCTOR)){
                users.setWorkingHourStart(doctorProfile.getWorkingHourStart().toLocalTime());
                users.setWorkingHourEnd(doctorProfile.getWorkingHourEnd().toLocalTime());
            }
            userRepository.save(users);
            return new BaseResponse<>("Profile Updated successfully", null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user profile");
        }
    }

    public void sendOTP(Users users) throws Exception {
        // Creating OTP
        String otp = Integer.toString(generateRandomNumber());
        users.setEmailOTP(otp);
        userRepository.save(users);
        // Sending Email
        SendEmail sendEmail = new SendEmail();
        sendEmail.setToEmail(users.getEmail());
        sendEmail.setSubject("User Activation");
        sendEmail.setEmailTemplateName(Constants.EMAIL_OTP_TEMPLATE_NAME);
        Map<String,String> emailContent = new HashMap<>();
        emailContent.put("code",otp);
        emailContent.put("name",users.getFullName());
        sendEmail.setContent(emailContent);
        queueService.sendEmailToQueue(sendEmail);
    }

    private void sendForgotPasswordOTP(Users users) throws Exception {
        // Creating OTP
        String otp = Integer.toString(generateRandomNumber());
        users.setForgotPasswordOTP(otp);
        userRepository.save(users);
        // Sending Email
        SendEmail sendEmail = new SendEmail();
        sendEmail.setToEmail(users.getEmail());
        sendEmail.setSubject("Reset Password");
        sendEmail.setEmailTemplateName(Constants.FORGOT_PASSWORD_OTP_TEMPLATE_NAME);
        Map<String,String> emailContent = new HashMap<>();
        emailContent.put("code",otp);
        emailContent.put("name",users.getFullName());
        sendEmail.setContent(emailContent);
        queueService.sendEmailToQueue(sendEmail);
    }

    private int generateRandomNumber() {
        Random r = new Random(System.currentTimeMillis());
        return (10000 + r.nextInt(20000));
    }
}
