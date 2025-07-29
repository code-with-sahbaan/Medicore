package health.care.medicore.ResponseDTO.Patient;

import health.care.medicore.Entities.Patient.Workout;
import health.care.medicore.Utils.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
        this.start = DateTimeUtil.convertLocalDateTimeToMilli(workout.getStart());
        this.end = DateTimeUtil.convertLocalDateTimeToMilli(workout.getEnd());
    }

}
