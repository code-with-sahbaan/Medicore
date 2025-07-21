package health.care.medicore.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
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

    @Column(name = "APPOINTMENT_DATE")
    private Date appointmentDate;

    @Column(name = "APPOINTMENT_TIME")
    private Time appointmentTime;

    @OneToMany(mappedBy = "appointments", fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference
    private List<UserAppointments> userAppointments = new ArrayList<>();
}
