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
    private String constraint; // this is for putting doctor ID in constraint parameter in event object on frontend.

    public MyAppointments(long appointmentId, String doctorName, LocalDate date, LocalTime start, LocalTime end, long doctorId) {
        this.appointmentId = appointmentId;
        this.title = "Appointment with " + doctorName;
        this.start = LocalDateTime.of(date, start);
        this.end = LocalDateTime.of(date, end);
        this.allDay = false;
        this.constraint = String.valueOf(doctorId);
    }
}
