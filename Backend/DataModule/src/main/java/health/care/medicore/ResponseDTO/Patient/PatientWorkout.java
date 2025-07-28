package health.care.medicore.ResponseDTO.Patient;

import health.care.medicore.Entities.Patient.Workout;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class PatientWorkout {

    private long workoutId;

    private String title;

    private LocalDate workoutDate;

    private long start;

    private long end;

    public PatientWorkout(Workout workout) {
        this.workoutId = workout.getWorkoutId();
        this.title = workout.getTitle();
        this.workoutDate = workout.getWorkoutDate();
        this.start = convertLocalDateTimeToMilli(workout.getStart());
        this.end = convertLocalDateTimeToMilli(workout.getEnd());
    }

    private long convertLocalDateTimeToMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
