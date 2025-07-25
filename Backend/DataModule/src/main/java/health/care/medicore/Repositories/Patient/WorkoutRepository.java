package health.care.medicore.Repositories.Patient;

import health.care.medicore.Entities.Patient.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findWorkoutByWorkoutDate(LocalDate workoutDate);
}
