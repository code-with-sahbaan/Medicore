package health.care.medicore.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "USER_APPOINTMENTS")
@Entity
@Getter
@Setter
public class UserAppointments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_APPOINTMENT_ID")
    private long userAppointmentId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "appointmentId")
    private Appointments appointments;

}
