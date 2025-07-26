package health.care.medicore.ServicesImpl.Patient;

import health.care.medicore.Entities.Patient.Workout;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.Patient.WorkoutRepository;
import health.care.medicore.ResponseDTO.Patient.PatientWorkout;
import health.care.medicore.Services.Patient.WorkoutService;
import health.care.medicore.ServicesImpl.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutServiceImpl extends GenericServiceImpl<Workout> implements WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    public WorkoutServiceImpl() {
        super(Workout.class);
    }

    @Override
    public List<PatientWorkout> getTodayWorkoutSchedule(Users users) {
        List<Workout> workouts = workoutRepository.findWorkoutByUsersAndWorkoutDate(users, LocalDate.now());
        List<PatientWorkout> patientWorkouts = new ArrayList<>();
        for (Workout workout : workouts) {
            patientWorkouts.add(convertEntityToDto(workout, PatientWorkout.class));
        }
        return patientWorkouts;
    }
}
