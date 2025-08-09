package org.jc.common.api;

import com.mybatisflex.core.BaseMapper;

import javax.sql.DataSource;

public interface DatabaseInterface {
    DataSource getDataSource();
    <T> T getMapper(Class<T> mapperClass);
}
