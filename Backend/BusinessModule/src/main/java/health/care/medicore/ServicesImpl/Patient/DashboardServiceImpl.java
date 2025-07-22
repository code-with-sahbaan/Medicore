package health.care.medicore.ServicesImpl.Patient;

import health.care.medicore.Services.AppointmentService;
import health.care.medicore.Services.Patient.DashboardService;
import health.care.medicore.ServicesImpl.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl extends GenericServiceImpl<DashboardServiceImpl> implements DashboardService {

    @Autowired
    private AppointmentService appointmentService;

    public DashboardServiceImpl() {
        super(DashboardServiceImpl.class);
    }
    
}
