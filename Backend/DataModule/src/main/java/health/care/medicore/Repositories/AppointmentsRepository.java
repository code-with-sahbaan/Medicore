package health.care.medicore.Repositories;

import health.care.medicore.Entities.Appointments;
import health.care.medicore.ResponseDTO.Patient.PatientAppointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {

    @Query("SELECT NEW health.care.medicore.ResponseDTO.Patient.PatientAppointment" +
            "(ap.appointmentId, ap.appointmentDateTime, ap.appointmentDuration, ap.doctor.fullName) " +
            "FROM Appointments ap " +
            "WHERE ap.patient.userId = :patientId " +
            "AND (ap.appointmentDateTime > :now OR ap.appointmentDateTime = :now) " +
            "ORDER BY ap.appointmentDateTime ASC")
    Page<PatientAppointment> getTop1AppointmentsByPatientId(long patientId, LocalDateTime now, Pageable pageable);
}
