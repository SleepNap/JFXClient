package org.jc.main.core.config;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jc.common.AppContext;
import org.jc.common.factory.ConfigFactory;
import org.jc.main.core.database.DatabaseManager;
import org.jc.main.core.log.LogManager;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;

@Slf4j
@Data
public class ConfigManager {
    private static ConfigManager instance;
    private File configFile;
    private StartConfig startConfig;
    private DefaultConfigService configService;

    private ConfigManager() {
        initialize();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void initialize() {
        // 如果后续有需要可以在这里添加从其他地方加载启动配置
        configFile = new File("application.yml");
        configService = new DefaultConfigService();
        AppContext.INSTANCE.configFactory = new ConfigFactory(configService);
    }

    public void loadStartConfig() {
        if (!configFile.exists()) {
            loadDefaultConfig();
            return;
        }
        try {
            Yaml yaml = new Yaml();
            JSONObject jsonObject = yaml.loadAs(new FileReader(configFile), JSONObject.class);
            configService.setYmlConfig(jsonObject);
            startConfig = jsonObject.getJSONObject("jc").to(StartConfig.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        if (startConfig == null) {
            loadDefaultConfig();
        }
    }

    private void loadDefaultConfig() {
        startConfig = StartConfig.builder()
                .db(DatabaseManager.getInstance().getDefaultConfig())
                .log(LogManager.getInstance().getDefaultConfig())
                .build();
    }

    public void loadContextConfig() {

    }
}
