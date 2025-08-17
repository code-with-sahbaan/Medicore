package health.care.medicore.ServicesImpl;

import health.care.medicore.Entities.AppConfigs;
import health.care.medicore.Repositories.AppConfigRepository;
import health.care.medicore.Services.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppConfigServiceImpl extends GenericServiceImpl<AppConfigs> implements AppConfigService {


    public AppConfigServiceImpl() {
        super(AppConfigs.class);
    }

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Override
    public AppConfigs getAppConfigsByName(String name) {
        return appConfigRepository.findByName(name);
    }
}
