package org.jc.core.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.jc.common.util.ZipUtil;
import org.jc.core.config.ConfigManager;
import org.jc.core.config.StartDatabaseConfig;

import java.io.File;
import java.io.IOException;

@Slf4j
public class DatabaseManager {
    private static DatabaseManager instance;
    private File databaseDir;
    private HikariDataSource pgDataSource;

    private DatabaseManager() {
        initialize();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initialize() {
        StartDatabaseConfig databaseConfig = ConfigManager.getInstance().getStartConfig().getDb();
        if (databaseConfig.isRemote()) {
            return;
        }

        databaseDir = new File("database/pgsql");
        if (databaseDir.exists()) {
            startDatabase();
            return;
        }

        File parentDir = databaseDir.getParentFile();
        if (!parentDir.exists() || !parentDir.isDirectory()) {
            throw new RuntimeException("database directory does not exist");
        }
        File[] files = parentDir.listFiles();
        if (files == null || files.length == 0) {
            throw new RuntimeException("database directory is empty");
        }

        try {
            log.debug("未找到pg数据库目录，正在尝试寻找并解压pg压缩包");
            File zipFile = null;
            for (File file : files) {
                if (!file.getName().endsWith(".zip")) {
                    continue;
                }
                zipFile = file;
            }
            if (zipFile == null) {
                throw new RuntimeException("zip file does not exist");
            }
            ZipUtil.unzip(zipFile, parentDir);
            log.debug("pg数据库目录解压完成");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        startDatabase();
    }

    public void startDatabase() {
        log.debug("正在启动pg数据库");
        try {
            boolean started = PostgresOperator.startPG(databaseDir);
            if (!started) {
                throw new RuntimeException("start pg failed");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        log.debug("pg数据库启动完成");
    }

    public void stopDatabase() {
        log.debug("正在关闭pg数据库");
        try {
            boolean stopped = PostgresOperator.stopPG(databaseDir);
            if (!stopped) {
                throw new RuntimeException("stop pg failed");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        log.debug("pg数据库关闭完成");
    }

    public void initDataSource() {

    }
}
