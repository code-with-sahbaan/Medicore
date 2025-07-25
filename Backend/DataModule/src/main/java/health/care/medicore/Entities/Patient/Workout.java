package health.care.medicore.Entities.Patient;

import health.care.medicore.Entities.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "WORKOUT")
@Entity
@Getter
@Setter
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "WORKOUT_ID")
    private long workoutId;

    @Column(name = "WORKOUT_NAME")
    private String title;

    @Column(name = "WORKOUT_DATE")
    private LocalDate workoutDate;

    @Column(name = "WORKOUT_START_TIME")
    private LocalDateTime start;

    @Column(name = "WORKOUT_END_TIME")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users users;
}
