package health.care.medicore.ServicesImpl.Patient;

import health.care.medicore.Entities.Users;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.Dashboard;
import health.care.medicore.Services.AppointmentService;
import health.care.medicore.Services.Patient.DashboardService;
import health.care.medicore.Services.UserService;
import health.care.medicore.ServicesImpl.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl extends GenericServiceImpl<DashboardServiceImpl> implements DashboardService {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    public DashboardServiceImpl() {
        super(DashboardServiceImpl.class);
    }

    @Override
    public BaseResponse<Dashboard> getDashboard() throws Exception {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userService.getUserByEmail(email).get();
        Dashboard dashboard = new Dashboard();
        dashboard.setPatientAppointments(appointmentService.getTop1AppointmentsByPatientId(users.getUserId()));
        dashboard.setCredits(users.getCredits());
        dashboard.setTotalAppointments(appointmentService.totalNumberOfAppointmentsByPatientId(users.getUserId()));
        return new BaseResponse<>("Dashboard Data has been fetched successfully", dashboard);
    }
}
