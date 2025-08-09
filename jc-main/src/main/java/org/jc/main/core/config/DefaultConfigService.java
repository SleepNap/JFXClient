package org.jc.main.core.config;

import com.alibaba.fastjson2.JSONObject;
import org.jc.common.api.ConfigInterface;

public class DefaultConfigService implements ConfigInterface {
    private JSONObject ymlConfig;
    private JSONObject settingConfig;

    @Override
    public JSONObject getYmlConfig() {
        return ymlConfig;
    }

    @Override
    public JSONObject getSettingConfig() {
        return settingConfig;
    }

    protected void setYmlConfig(JSONObject ymlConfig) {
        this.ymlConfig = ymlConfig;
    }

    protected void setSettingConfig(JSONObject settingConfig) {
        this.settingConfig = settingConfig;
    }
}
