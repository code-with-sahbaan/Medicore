package health.care.medicore.Repositories;

import health.care.medicore.Entities.Appointments;
import health.care.medicore.Entities.Users;
import health.care.medicore.ResponseDTO.Patient.PatientAppointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {

    @Query("SELECT NEW health.care.medicore.ResponseDTO.Patient.PatientAppointment" +
            "(ap.appointmentId, ap.appointmentDate, ap.appointmentStartTime, ap.appointmentDuration, ap.doctor.fullName) " +
            "FROM Appointments ap " +
            "WHERE ap.patient.userId = :patientId " +
            "AND ((ap.appointmentDate = :currentDate AND ap.appointmentStartTime >= :currentTime) OR ap.appointmentDate > :currentDate) " +
            "ORDER BY ap.appointmentDate ASC, ap.appointmentStartTime ASC")
    Page<PatientAppointment> getTop1AppointmentsByPatientId(@Param("patientId") long patientId, @Param("currentDate") LocalDate currentDate, @Param("currentTime")LocalTime currentTime, Pageable pageable);

    @Query("SELECT COUNT(ap.appointmentId) FROM Appointments ap WHERE ap.patient.userId = :patientId")
    long totalNumberOfAppointmentsByPatientId(@Param("patientId") long patientId);

    @Query("SELECT ap.appointmentStartTime FROM Appointments ap WHERE ap.doctor = :doctor AND ap.appointmentDate = :today")
    Set<LocalTime> getAppointmentTimesByDoctorId(@Param("doctor") Users doctor, @Param("today") LocalDate today);

    @Query("SELECT ap.appointmentId FROM Appointments ap WHERE ap.appointmentDate = :today AND ap.doctor = :doctor AND ap.appointmentStartTime = :time")
    Optional<Long> isAppointmentAvailable(@Param("today") LocalDate today, @Param("doctor") Users doctor, @Param("time") LocalTime time);
}
