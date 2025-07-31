package health.care.medicore.RequestDTO.Patient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BookAppointment {

    private String doctorEmail;
    private LocalDate appointmentDate;
    List<AppointmentTimeSlots> appointmentTimes;
}
