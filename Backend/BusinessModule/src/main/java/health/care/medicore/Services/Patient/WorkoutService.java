package health.care.medicore.Services.Patient;


import health.care.medicore.Entities.Users;
import health.care.medicore.ResponseDTO.Patient.PatientWorkout;

import java.util.List;

public interface WorkoutService {

    List<PatientWorkout> getTodayWorkoutSchedule(Users users);
}
