package com.nixsolutions.usik.model.service.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static com.nixsolutions.usik.utils.PropertiesUtils.getProperties;

public abstract class AbstractJdbcDao {

        private static final Logger logger = LogManager.getLogger(AbstractJdbcDao.class.getName());
    private static BasicDataSource dataSource;
    private static String url;

    private static BasicDataSource getDataSource() throws IOException {
        if (url == null) {
            url = "sql/database.properties";
        }

        if (dataSource == null) {
            BasicDataSource ds = new BasicDataSource();
            ds.setUrl(getProperties(url, "url"));
            ds.setUsername(getProperties(url, "username"));
            ds.setPassword(getProperties(url, "password"));

            ds.setMinIdle(Integer.parseInt(getProperties(url, "idle.min")));
            ds.setMaxIdle(Integer.parseInt(getProperties(url, "idle.max")));
            ds.setMaxOpenPreparedStatements(Integer.parseInt(getProperties(url, "statements.max")));

            logger.info("Create new connection");
            dataSource = ds;
        }

        return dataSource;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        AbstractJdbcDao.url = url;
    }

    public static Connection createConnection() throws SQLException {
        try {
            if (dataSource == null) {
                return getDataSource().getConnection();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return dataSource.getConnection();
    }
}
