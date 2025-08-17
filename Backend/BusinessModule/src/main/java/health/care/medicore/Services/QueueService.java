package health.care.medicore.Services;

import health.care.medicore.RequestDTO.SendEmail;

public interface QueueService {

    void sendEmailToQueue(SendEmail sendEmail) throws Exception;
}
