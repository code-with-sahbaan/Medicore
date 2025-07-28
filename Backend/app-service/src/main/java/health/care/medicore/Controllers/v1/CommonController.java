package health.care.medicore.Controllers.v1;

import health.care.medicore.RequestDTO.PageableRequest;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.GetAllConsultants;
import health.care.medicore.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("patient")
@Slf4j
public class CommonController {

    @Autowired
    private UserService userService;

    @PostMapping("v1/getAllConsultants")
    public ResponseEntity<BaseResponse<Page<GetAllConsultants>>> getAllConsultants(@RequestBody PageableRequest pageableRequest) throws Exception {
        log.info("Executing getAllConsultants in CommonController");
        BaseResponse<Page<GetAllConsultants>> response = userService.getUsersByDoctorRole(pageableRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
