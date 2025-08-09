package org.jc.common.api;

import com.alibaba.fastjson2.JSONObject;

public interface ConfigInterface {
    JSONObject getYmlConfig();
    JSONObject getSettingConfig();
}
