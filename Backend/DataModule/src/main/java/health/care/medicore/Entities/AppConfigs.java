package health.care.medicore.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_CONFIGS")
@Entity
@Getter
@Setter
public class AppConfigs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "APP_CONFIG_ID")
    private long appConfigId;

    @Column(name = "CONFIG_NAME")
    private String name;

    @Column(name = "CONFIG_VALUE", length = 65535)
    private String value;
}
