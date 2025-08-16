package health.care.medicore.Repositories;

import health.care.medicore.Entities.Role;
import health.care.medicore.Entities.Users;
import health.care.medicore.ResponseDTO.Doctor.DoctorProfile;
import health.care.medicore.ResponseDTO.Patient.PatientProfile;
import health.care.medicore.ResponseDTO.Patient.GetAllConsultants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    @Query("SELECT new health.care.medicore.ResponseDTO.Patient.GetAllConsultants(u.userId, u.email, u.fullName, u.consultationRates) FROM Users u WHERE u.role = :role")
    Page<GetAllConsultants> getUsersByDoctorRole(@Param("role") Role role, Pageable pageable);

    @Query("SELECT new health.care.medicore.ResponseDTO.Patient.PatientProfile(usr.email, usr.fullName) FROM Users usr " +
            "WHERE usr.userId = :userId")
    PatientProfile getPatientProfile(@Param("userId") long userId);

    @Query("SELECT new health.care.medicore.ResponseDTO.Doctor.DoctorProfile(" +
            "usr.email, usr.fullName, usr.workingHourStart, usr.workingHourEnd, usr.consultationRates) FROM Users usr " +
            "WHERE usr.userId = :userId")
    DoctorProfile getDoctorProfile(@Param("userId") long userId);
}
