package health.care.medicore.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "appointments", fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference
    private List<UserAppointments> userAppointments = new ArrayList<>();
}
