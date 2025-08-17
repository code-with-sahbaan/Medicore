package health.care.medicore.RequestDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SendEmail {

    private String toEmail;
    private String subject;
    private String emailTemplateName;
    private Map<String, String> content;
}
