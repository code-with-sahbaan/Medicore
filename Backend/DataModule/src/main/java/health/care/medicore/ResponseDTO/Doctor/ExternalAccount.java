package health.care.medicore.ResponseDTO.Doctor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalAccount {

    private String last4;
    private String routingNumber;
    private String country;
    private String currency;

}
