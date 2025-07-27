package health.care.medicore.ServicesImpl.Patient;

import health.care.medicore.Entities.Users;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.Dashboard;
import health.care.medicore.Services.AppointmentService;
import health.care.medicore.Services.Patient.DashboardService;
import health.care.medicore.Services.Patient.WorkoutService;
import health.care.medicore.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkoutService workoutService;

    @Override
    public BaseResponse<Dashboard> getDashboard() throws Exception {
        try{
            Users users = userService.getCurrentUser();
            Dashboard dashboard = new Dashboard();
            dashboard.setPatientAppointments(appointmentService.getTop1AppointmentsByPatientId(users.getUserId()));
            dashboard.setCredits(users.getCredits());
            dashboard.setTotalAppointments(appointmentService.totalNumberOfAppointmentsByPatientId(users.getUserId()));
            dashboard.setPatientWorkouts(workoutService.getTodayWorkoutSchedule(users));
            return new BaseResponse<>("Dashboard Data has been fetched successfully", dashboard);
        }catch (Exception e){
            throw new Exception("Failed to fetch Dashboard Data");
        }
    }
}
