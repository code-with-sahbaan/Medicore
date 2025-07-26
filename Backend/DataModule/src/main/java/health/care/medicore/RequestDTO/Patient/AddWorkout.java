package health.care.medicore.RequestDTO.Patient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddWorkout {

    private String title;
    private long start;
    private long end;
}
