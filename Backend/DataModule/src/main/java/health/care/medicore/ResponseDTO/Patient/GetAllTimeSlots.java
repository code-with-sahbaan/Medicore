package health.care.medicore.ResponseDTO.Patient;

import health.care.medicore.Utils.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class GetAllTimeSlots {

    private String label;
    private LocalTime value;

    public GetAllTimeSlots(LocalTime label, LocalTime value) {
        this.label = DateTimeUtil.formatLocalTime(label);
        this.value = value;
    }
}
