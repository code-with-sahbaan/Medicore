package health.care.medicore.Services.Patient;

import health.care.medicore.RequestDTO.Patient.AiUserMessage;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.AiAssistantResponse;
import health.care.medicore.ResponseDTO.Patient.GetAllChatMessages;

import java.util.List;

public interface ChatService {

    BaseResponse<AiAssistantResponse> getAnswer(AiUserMessage aiUserMessage) throws  Exception;
    BaseResponse<List<GetAllChatMessages>> getAllChatMessages() throws  Exception;
}
