package org.jc.main.core.database;

import org.jc.common.api.DatabaseInterface;

import javax.sql.DataSource;

public class DefaultDatabaseService implements DatabaseInterface {

    @Override
    public DataSource getDataSource() {
        return DatabaseManager.getInstance().getDataSource();
    }

    @Override
    public <T> T getMapper(Class<T> mapperClass) {
        return DatabaseManager.getInstance().getMapper(mapperClass);
    }

}
