package health.care.medicore.Entities;

import health.care.medicore.Entities.Patient.Workout;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Table(name = "USERS")
@Entity
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "EMAIL",
            unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FULL_NAME")
    private String fullName;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @Column(name = "EMAIL_OTP")
    private String emailOTP;

    @Column(name = "FORGOT_PASSWORD_OTP")
    private String forgotPasswordOTP;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = false;

    @Column(name = "CREDITS")
    private long credits;

    @Column(name = "WORKING_HOUR_START")
    private LocalTime workingHourStart;

    @Column(name = "WORKING_HOUR_END")
    private LocalTime workingHourEnd;

    // Appointments where this user is the doctor
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Appointments> doctorAppointments;

    // Appointments where this user is the patient
    @OneToMany(mappedBy = "patient", fetch =  FetchType.LAZY)
    private List<Appointments> patientAppointments;

    // Workout where this user is the patient
    @OneToMany(mappedBy = "users", fetch =  FetchType.LAZY)
    private List<Workout> workouts;

}
