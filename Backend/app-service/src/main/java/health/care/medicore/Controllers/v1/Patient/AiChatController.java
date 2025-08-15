package health.care.medicore.Controllers.v1.Patient;

import health.care.medicore.RequestDTO.Patient.AiUserMessage;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.AiAssistantResponse;
import health.care.medicore.ResponseDTO.Patient.PatientWorkout;
import health.care.medicore.Services.Patient.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("aiChat")
@Slf4j
public class AiChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("v1/askAi")
    public ResponseEntity<BaseResponse<AiAssistantResponse>> askAi(@RequestBody AiUserMessage aiUserMessage) throws Exception {
        log.info("Executing askAi in AiChatController");
        BaseResponse<AiAssistantResponse> savedWorkout = chatService.getAnswer(aiUserMessage);
        return new ResponseEntity<>(savedWorkout, HttpStatus.OK);
    }
}
