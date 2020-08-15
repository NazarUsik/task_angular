package com.nixsolutions.usik.jdbc;

import com.nixsolutions.usik.model.entity.User;
import com.nixsolutions.usik.model.repository.UserDao;
import com.nixsolutions.usik.model.service.jdbc.AbstractJdbcDao;
import com.nixsolutions.usik.model.service.jdbc.JdbcUserDao;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.nixsolutions.usik.utils.PropertiesUtils.getProperties;
import static org.junit.Assert.assertArrayEquals;

@RunWith(JUnit4.class)
public class JdbcUserDaoTest extends DataSourceBasedDBTestCase {

    private UserDao userDao;

    @Override
    protected DataSource getDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        String url = "src/test/resources/database.properties";
        try {
            dataSource.setURL(
                    getProperties(url, "url") +
                            ";DB_CLOSE_DELAY=-1;" +
                            "init=runscript from 'src/test/resources/create.sql'");
            dataSource.setUser(getProperties(url, "username"));
            dataSource.setPassword(getProperties(url, "password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        try (InputStream resourceAsStream = new FileInputStream(
                "src/test/resources/dataset.xml")) {
            return new FlatXmlDataSetBuilder().build(resourceAsStream);
        }
    }

    @Override
    protected DatabaseOperation getSetUpOperation() {
        return DatabaseOperation.REFRESH;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() {
        return DatabaseOperation.DELETE_ALL;
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        AbstractJdbcDao.setUrl("src/test/resources/database.properties");
        userDao = new JdbcUserDao();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testCreateUser() throws Exception {
        userDao.create(new User(
                "manager",
                "manager",
                "manager@gmail.com",
                "manager",
                "manager",
                Date.valueOf("2000-01-01"),
                2
        ));

        TestUtils.assertTables(
                "USER",
                "src/test/resources/after-create.xml",
                getConnection()
        );
    }

    @Test
    public void testUpdateUser() throws Exception {
        userDao.update(new User(
                1,
                "customer",
                "customer",
                "customer@gmail.com",
                "customer",
                "customer",
                Date.valueOf("2000-01-01"),
                1
        ));

        TestUtils.assertTables(
                "USER",
                "src/test/resources/after-update.xml",
                getConnection()
        );
    }

    @Test
    public void testRemoveUser() throws Exception {
        User user = new User();
        user.setId(1);
        userDao.remove(user);

        TestUtils.assertTables(
                "USER",
                "src/test/resources/after-delete.xml",
                getConnection()
        );
    }

    @Test
    public void testFindAllUsers() {
        List<User> list = new LinkedList<>(Arrays.asList(
                TestUtils.createUser(1),
                TestUtils.createUser(2),
                TestUtils.createUser(3)
        ));

        assertArrayEquals("Array must be equals", list.toArray(), userDao.findAll().toArray());
    }
}