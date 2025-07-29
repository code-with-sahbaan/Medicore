package health.care.medicore.ResponseDTO.Patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class PatientAppointment {

    private long appointmentId;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private long appointmentDuration;

    private String doctorName;

    public PatientAppointment(long appointmentId, LocalDate appointmentDate, LocalTime appointmentTime, long appointmentDuration, String doctorName) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentDuration = appointmentDuration;
        this.doctorName = doctorName;
    }
}
