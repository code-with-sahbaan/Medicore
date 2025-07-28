package health.care.medicore.Repositories.Patient;

import health.care.medicore.Entities.Patient.Workout;
import health.care.medicore.Entities.Users;
import health.care.medicore.ResponseDTO.Patient.PatientWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    @Query("SELECT new health.care.medicore.ResponseDTO.Patient.PatientWorkout(w) FROM Workout w WHERE w.users = :users AND w.workoutDate = :workoutDate")
    List<PatientWorkout> getWorkoutForToday(@Param("users") Users users, @Param("workoutDate") LocalDate workoutDate);
}
