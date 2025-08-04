package health.care.medicore.ResponseDTO.Patient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class MyAppointments {

    private long appointmentId;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private Boolean allDay;

    public MyAppointments(long appointmentId, String doctorName, LocalDate date, LocalTime start, LocalTime end) {
        this.appointmentId = appointmentId;
        this.title = "Appointment with " + doctorName;
        this.start = LocalDateTime.of(date, start);
        this.end = LocalDateTime.of(date, end);
        this.allDay = false;
    }
}
