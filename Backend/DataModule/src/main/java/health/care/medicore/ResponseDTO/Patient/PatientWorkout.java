package health.care.medicore.ResponseDTO.Patient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PatientWorkout {

    private long workoutId;

    private String title;

    private LocalDate workoutDate;

    private long start;

    private long end;
}
