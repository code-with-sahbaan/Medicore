package health.care.medicore.RequestDTO.Patient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetAvailableTimeSlots {

    private String doctorEmail;
    private LocalDate appointmentDate;
}
