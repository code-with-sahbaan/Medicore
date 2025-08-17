package health.care.medicore.Services;

import health.care.medicore.Entities.AppConfigs;

public interface AppConfigService {

    AppConfigs getAppConfigsByName(String name);
}
