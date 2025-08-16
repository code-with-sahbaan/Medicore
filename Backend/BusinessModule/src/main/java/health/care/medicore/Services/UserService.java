package health.care.medicore.Services;


import health.care.medicore.Entities.Users;
import health.care.medicore.RequestDTO.ForgotPassword;
import health.care.medicore.RequestDTO.PageableRequest;
import health.care.medicore.ResponseDTO.Doctor.DoctorProfile;
import health.care.medicore.ResponseDTO.Patient.PatientProfile;
import health.care.medicore.RequestDTO.SignupRequest;
import health.care.medicore.RequestDTO.VerifyOtpRequest;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.GetCredits;
import health.care.medicore.ResponseDTO.Patient.GetAllConsultants;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService{

    Optional<Users> getUserByEmail(String email);

    BaseResponse<?> signup(SignupRequest signupRequest) throws Exception;

    void verifyOtp(VerifyOtpRequest verifyOtpRequest) throws Exception;

    Users getCurrentUser();

    void forgotPassword(ForgotPassword forgotPassword) throws Exception;

    BaseResponse<Page<GetAllConsultants>> getUsersByDoctorRole(PageableRequest pageableRequest) throws Exception;

    void updateUser(Users users) throws Exception;

    BaseResponse<GetCredits> getCredits() throws Exception;

    BaseResponse<PatientProfile> getUserProfile() throws Exception;

    BaseResponse<?> updateProfile(DoctorProfile doctorProfile) throws Exception;
}
