package health.care.medicore.ServicesImpl;

import health.care.medicore.Entities.AppConfigs;
import health.care.medicore.RequestDTO.SendEmail;
import health.care.medicore.Services.AppConfigService;
import health.care.medicore.Services.QueueService;
import health.care.medicore.Utils.QueueConstants;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AppConfigService appConfigService;

    @Value("${medicore.email.sender}")
    private String emailSender;

    @Value("${spring.application.name}")
    private String appName;

    @RabbitListener(queues = QueueConstants.EMAIL_QUEUE)
    public void sendEmail(SendEmail sendEmail) throws Exception {
        try{
            /* Getting Template FROM DB*/
            AppConfigs appConfigs = appConfigService.getAppConfigsByName(sendEmail.getEmailTemplateName());
            String template = appConfigs.getValue();

            /* Preparing Email Object */
            String toEmail = sendEmail.getToEmail();
            String subject = sendEmail.getSubject();
            String fromEmail = emailSender;
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(fromEmail, appName);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(toEmail);

            /* Replacing email values with content */
            for(String names: sendEmail.getContent().keySet()){
                String templateVar = "[[" + names + "]]";
                template = template.replace(templateVar, sendEmail.getContent().get(names));
            }

            /* Sending Email */
            mimeMessageHelper.setText(template, true);
            mailSender.send(mimeMessage);
        }catch(Exception e){
            throw new Exception("Failed to send email");
        }
    }

    @Override
    public void sendEmailToQueue(SendEmail sendEmail) throws Exception {
        rabbitTemplate.convertAndSend(QueueConstants.EMAIL_QUEUE, sendEmail);
    }
}
