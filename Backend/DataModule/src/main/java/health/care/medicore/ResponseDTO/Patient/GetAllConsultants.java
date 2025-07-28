package health.care.medicore.ResponseDTO.Patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAllConsultants {

    private long userId;
    private String email;
    private String fullName;
}
