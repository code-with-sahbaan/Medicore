package health.care.medicore.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "APPOINTMENTS")
@Entity
@Getter
@Setter
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "APPOINTMENT_ID")
    private long appointmentId;

    @Column(name = "APPOINTMENT_DATE_TIME")
    private LocalDateTime appointmentDateTime;

    @Column(name = "APPOINTMENT_DURATION")
    private long appointmentDuration;

    @ManyToOne
    @JoinColumn(name = "doctorId")
    private Users doctor;

    @ManyToOne
    @JoinColumn(name = "patientId")
    private Users patient;
}
