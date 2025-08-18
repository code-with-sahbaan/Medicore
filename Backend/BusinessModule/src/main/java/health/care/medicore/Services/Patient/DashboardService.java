package health.care.medicore.Services.Patient;

import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Doctor.DoctorDashboard;
import health.care.medicore.ResponseDTO.Patient.Dashboard;

public interface DashboardService {

    BaseResponse<Dashboard> getDashboard() throws Exception;

    BaseResponse<DoctorDashboard> getDashboardForDoctor() throws Exception;

}
