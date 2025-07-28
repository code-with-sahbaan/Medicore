package health.care.medicore.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableRequest {

    private int page;
    private int size;
    private String sort;
    private int order;
}
