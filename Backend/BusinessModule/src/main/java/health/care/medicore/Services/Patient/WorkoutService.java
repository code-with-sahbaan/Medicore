package health.care.medicore.Services.Patient;


import health.care.medicore.Entities.Patient.Workout;
import health.care.medicore.Entities.Users;
import health.care.medicore.RequestDTO.Patient.AddWorkout;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.PatientWorkout;

import java.util.List;

public interface WorkoutService {

    List<PatientWorkout> getTodayWorkoutSchedule(Users users);

    BaseResponse<PatientWorkout> addWorkout(AddWorkout workout) throws  Exception;
}
