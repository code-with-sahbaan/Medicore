package health.care.medicore.ServicesImpl.Patient;

import health.care.medicore.Entities.Patient.Workout;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.Patient.WorkoutRepository;
import health.care.medicore.RequestDTO.Patient.AddWorkout;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.PatientWorkout;
import health.care.medicore.Services.Patient.WorkoutService;
import health.care.medicore.Services.UserService;
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

    @Autowired
    private UserService userService;

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

    @Override
    public BaseResponse<PatientWorkout> addWorkout(AddWorkout workout) throws  Exception {
        try{
            Workout newWorkout;
            Users users = userService.getCurrentUser();
            newWorkout = convertDtoToEntity(workout);
            newWorkout.setWorkoutDate(LocalDate.now());
            newWorkout.setUsers(users);
            PatientWorkout patientWorkout =  convertEntityToDto(workoutRepository.save(newWorkout), PatientWorkout.class);
            return new BaseResponse<>("Workout Added Successfully", patientWorkout);
        } catch (Exception e) {
            throw new Exception("Failed to add workout");
        }
    }
}
