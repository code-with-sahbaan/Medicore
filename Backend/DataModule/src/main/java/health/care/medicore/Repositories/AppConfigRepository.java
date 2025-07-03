package health.care.medicore.Repositories;

import health.care.medicore.Entities.AppConfigs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigRepository extends JpaRepository<AppConfigs, Long> {


    AppConfigs findByName(String name);
}
