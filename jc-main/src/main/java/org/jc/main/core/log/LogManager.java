package org.jc.main.core.log;

import lombok.Data;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.slf4j.Log4jLogger;
import org.jc.main.core.config.ConfigManager;
import org.jc.main.core.config.StartLogConfig;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

@Data
public class LogManager {
    private static LogManager instance;
    private StartLogConfig startLogConfig;
    private LoggerContext context;

    private LogManager() {
        initialize();
    }

    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    public void initialize() {
        startLogConfig = ConfigManager.getInstance().getStartConfig().getLog();
        try {
            org.apache.logging.slf4j.Log4jLogger log4jLogger = (org.apache.logging.slf4j.Log4jLogger) LoggerFactory.getILoggerFactory().getLogger(LogManager.class.getName());
            Field field = Log4jLogger.class.getDeclaredField("logger");
            field.setAccessible(true);
            org.apache.logging.log4j.core.Logger curentLogger = (org.apache.logging.log4j.core.Logger) field.get(log4jLogger);
            context = curentLogger.getContext();
        } catch (Exception e) {
            throw new RuntimeException("Initialize LogManager failed");
        }
    }

    public void initConfigLevel() {
        setRootLevel(startLogConfig.getLevel());
    }

    public void setRootLevel(String levelStr) {
        setRootLevel(stringToLevel(levelStr));
    }

    public void setRootLevel(Level level) {
        Configuration configuration = context.getConfiguration();
        LoggerConfig loggerConfig = configuration.getRootLogger();
        if (loggerConfig.getLevel().equals(level)) {
            return;
        }
        loggerConfig.setLevel(level);
        context.updateLoggers(configuration);
    }

    public void setLoggerLevel(String loggerName, String levelStr) {
        setLoggerLevel(loggerName, stringToLevel(levelStr));
    }

    public void setLoggerLevel(String loggerName, Level level) {
        Configuration configuration = context.getConfiguration();
        LoggerConfig loggerConfig = configuration.getLoggerConfig(loggerName);
        if (loggerConfig.getLevel().equals(level)) {
            return;
        }
        loggerConfig.setLevel(level);
        context.updateLoggers(configuration);
    }

    public Level stringToLevel(final String levelStr) {
        Level level;
        try {
            level = Level.valueOf(levelStr.toUpperCase());
        } catch (Exception e) {
            level = Level.INFO;
        }
        return level;
    }

    public StartLogConfig getDefaultConfig() {
        return StartLogConfig.builder()
                .level("INFO")
                .build();
    }
}
