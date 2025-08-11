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
    private Boolean isAppointmentTimeOccurred; // this is for enabling or disabling Join Conversation button on UI

    public MyAppointments(long appointmentId, String doctorName, LocalDate date, LocalTime start, LocalTime end) {
        this.appointmentId = appointmentId;
        this.title = "Appointment with " + doctorName;
        this.start = LocalDateTime.of(date, start);
        this.end = LocalDateTime.of(date, end);
        this.allDay = false;
        this.isAppointmentTimeOccurred = checkAppointmentTimeOccurred(start, end, date);
    }

    private Boolean checkAppointmentTimeOccurred(LocalTime start, LocalTime end, LocalDate date) {
        if (start.isAfter(end)) {
            date = date.plusDays(1);
        }
        LocalDateTime startTime =  LocalDateTime.of(date, start);
        LocalDateTime endTime =  LocalDateTime.of(date, end);
        return (LocalDateTime.now().isEqual(startTime) || LocalDateTime.now().isAfter(startTime)) && LocalDateTime.now().isBefore(endTime);
    }
}
