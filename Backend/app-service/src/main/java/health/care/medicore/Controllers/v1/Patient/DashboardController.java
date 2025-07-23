package health.care.medicore.Controllers.v1.Patient;

import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.Dashboard;
import health.care.medicore.Services.Patient.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("patient")
@Slf4j
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("getDashboardData")
    public ResponseEntity<BaseResponse<Dashboard>> getDashboardData() throws Exception {
        log.info("Executing getDashboardData in DashboardController");
        BaseResponse<Dashboard> response = dashboardService.getDashboard();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
