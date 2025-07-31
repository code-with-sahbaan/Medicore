package health.care.medicore.RequestDTO.Patient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class AppointmentTimeSlots {
    private String label;
    private LocalTime value;
}
