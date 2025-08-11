package health.care.medicore.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Table(name = "APPOINTMENTS")
@Entity
@Getter
@Setter
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "APPOINTMENT_ID")
    private long appointmentId;

    @Column(name = "APPOINTMENT_DATE")
    private LocalDate appointmentDate;

    @Column(name = "APPOINTMENT_START_TIME")
    private LocalTime appointmentStartTime;

    @Column(name = "APPOINTMENT_END_TIME")
    private LocalTime appointmentEndTime;

    @Column(name = "APPOINTMENT_DURATION")
    private long appointmentDuration;

    @ManyToOne
    @JoinColumn(name = "doctorId")
    private Users doctor;

    @ManyToOne
    @JoinColumn(name = "patientId")
    private Users patient;

    @PrePersist
    protected void updateAppointmentEndTime()
    {
        this.appointmentEndTime = LocalDateTime.of(appointmentDate, appointmentStartTime).plusMinutes(appointmentDuration).toLocalTime();
    }
}
