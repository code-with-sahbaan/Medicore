package health.care.medicore.RequestDTO.Doctor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayoutCredits {

    private String bankToken;
    private long credits;
    private String currency;
    private String country;
    private String account_holder_type;
}
