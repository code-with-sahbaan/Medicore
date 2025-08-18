package health.care.medicore.Controllers.v1.Doctor;

import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Doctor.DoctorDashboard;
import health.care.medicore.Services.Patient.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("doctor")
@Slf4j
public class DoctorDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("v1/getDashboardDataForDoctor")
    public ResponseEntity<BaseResponse<DoctorDashboard>> getDashboardDataForDoctor() throws Exception {
        log.info("Executing getDashboardDataForDoctor in DashboardController");
        BaseResponse<DoctorDashboard> response = dashboardService.getDashboardForDoctor();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
