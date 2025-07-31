package org.jc.common.util;

import org.jc.common.AppContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class I18nUtil {
    private static final Map<String, ResourceBundle> bundles = new HashMap<>();

    static {
        initialize();
    }

    /**
     * If you have setted locale into config, your config locale might be different from the system locale. So, you need to reload the bundles.
     * <br />
     * 当你手动设置了语言时，系统语言可能会和你的设置不同，需要重新加载语言包
     */
    public static void initialize() {
        bundles.clear();
        File resources = new File("resources/i18n");
        if (!resources.exists()) {
            resources.mkdirs();
        }
        File[] files = resources.listFiles();
        if (files != null) {
            for (File file : files) {
                loadBundle(file);
            }
        }
    }

    private static void loadBundle(File file) {
        if (!file.isFile() || !file.getName().endsWith(".properties")) {
            return;
        }
        int index = file.getName().indexOf("_");
        String bundleName = file.getName().substring(0, index);
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, AppContext.INSTANCE.locale);
        bundles.put(bundleName, bundle);
    }

    public static ResourceBundle getBundle(String bundleName) {
        return bundles.get(bundleName);
    }

    public static String getString(String key) {
        for (Map.Entry<String, ResourceBundle> entry : bundles.entrySet()) {
            ResourceBundle bundle = entry.getValue();
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        }
        return null;
    }

    public static String getString(String key, String defaultValue) {
        String str = getString(key);
        return str == null ? defaultValue : str;
    }

    public static String getBundleString(String bundleName, String key) {
        ResourceBundle bundle = bundles.get(bundleName);
        if (bundle == null) {
            return null;
        }
        return bundle.getString(key);
    }

    public static String getBundleString(String bundleName, String key, String defaultValue) {
        String str = getBundleString(bundleName, key);
        return str == null? defaultValue : str;
    }
}
