package health.care.medicore.Controllers.v1;

import health.care.medicore.RequestDTO.Patient.GetAvailableTimeSlots;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.GetAllTimeSlots;
import health.care.medicore.Services.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("appointment")
@Slf4j
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("v1/getAvailableSlots")
    public ResponseEntity<BaseResponse<List<GetAllTimeSlots>>> getAvailableSlots(@RequestBody GetAvailableTimeSlots getAvailableTimeSlots) throws Exception {
        log.info("Executing getAvailableSlots in AppointmentController");
        BaseResponse<List<GetAllTimeSlots>> response = appointmentService.getAvailableTimeSlots(getAvailableTimeSlots);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
