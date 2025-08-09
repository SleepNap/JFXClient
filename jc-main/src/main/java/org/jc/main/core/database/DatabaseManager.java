package org.jc.main.core.database;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.MybatisFlexBootstrap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.jc.common.AppContext;
import org.jc.common.api.DatabaseInterface;
import org.jc.common.factory.DatabaseFactory;
import org.jc.common.util.ZipUtil;
import org.jc.main.core.config.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
public class DatabaseManager {
    private static DatabaseManager instance;
    private File databaseDir;
    private HikariDataSource dataSource;
    private DatabaseInterface databaseService;

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
        databaseService = new DefaultDatabaseService();
        AppContext.INSTANCE.databaseFactory = new DatabaseFactory(databaseService);
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
        StartDatabaseConfig startDatabaseConfig = ConfigManager.getInstance().getStartConfig().getDb();
        beforeInitDataSource(startDatabaseConfig);

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(startDatabaseConfig.getUrl());
        config.setUsername(startDatabaseConfig.getUsername());
        config.setPassword(startDatabaseConfig.getPassword());
        dataSource = new HikariDataSource(config);
        FlexGlobalConfig.getDefaultConfig().setPrintBanner(false);
        MybatisFlexBootstrap.getInstance()
                .setDataSource(dataSource)
                .start();
    }

    private void beforeInitDataSource(StartDatabaseConfig config) {
        if (config == null || config.getUrl() == null || config.getUrl().isBlank()) {
            throw new RuntimeException("database config is null or empty");
        }
        Pattern pattern = Pattern.compile("jdbc:postgresql://[^/]+/([^?]+)");
        Matcher matcher = pattern.matcher(config.getUrl());
        if (!matcher.find()) {
            throw new RuntimeException("database url is not postgresql");
        }
        String oriDbName = matcher.group(1);
        String baseUrl = config.getUrl().replaceAll("(jdbc:postgresql://[^/]+/)[^?]+", "$1postgres");
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(baseUrl, config.getUsername(), config.getPassword());
            stmt = conn.createStatement();
            stmt.executeQuery("select 1 from pg_database where datname = '" + oriDbName + "'");
            ResultSet resultSet = stmt.getResultSet();
            if (resultSet.next()) {
                return;
            }
            String createDbSQL = "create database " + oriDbName;
            stmt.executeUpdate(createDbSQL);
            log.debug("数据库 {} 创建成功", oriDbName);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public <T> T getMapper(Class<T> mapperClass) {
        Configuration configuration = MybatisFlexBootstrap.getInstance().getConfiguration();
        if (!configuration.getMapperRegistry().hasMapper(mapperClass)) {
            configuration.addMapper(mapperClass);
        }
        return MybatisFlexBootstrap.getInstance().getMapper(mapperClass);
    }
}
