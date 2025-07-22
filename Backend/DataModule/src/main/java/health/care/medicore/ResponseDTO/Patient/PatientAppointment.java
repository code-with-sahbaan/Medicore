package health.care.medicore.ResponseDTO.Patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PatientAppointment {

    private long appointmentId;

    private LocalDateTime appointmentDateTime;

    private long appointmentDuration;

    private String doctorName;
}
