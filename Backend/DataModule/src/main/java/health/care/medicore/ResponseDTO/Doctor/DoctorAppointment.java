package health.care.medicore.ResponseDTO.Doctor;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class DoctorAppointment {

    private long appointmentId;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private long appointmentDuration;

    private String patientName;

    public DoctorAppointment(long appointmentId, LocalDate appointmentDate, LocalTime appointmentTime, long appointmentDuration, String patientName) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentDuration = appointmentDuration;
        this.patientName = patientName;
    }
}
