package com.nixsolutions.usik.jdbc;


import com.nixsolutions.usik.model.entity.Role;
import com.nixsolutions.usik.model.repository.RoleDao;
import com.nixsolutions.usik.model.service.jdbc.AbstractJdbcDao;
import com.nixsolutions.usik.model.service.jdbc.JdbcRoleDao;
import org.dbunit.Assertion;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
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

import static com.nixsolutions.usik.utils.PropertiesUtils.getProperties;

@RunWith(JUnit4.class)
public class JdbcRoleDaoTest extends DataSourceBasedDBTestCase {

    private RoleDao roleDao;

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
        roleDao = new JdbcRoleDao();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testEqualsTable() throws Exception {
        IDataSet expectedDataSet = getDataSet();
        ITable actualTable = expectedDataSet.getTable("ROLE");

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable expectedTable = databaseDataSet.getTable("ROLE");

        Assertion.assertEquals(actualTable, expectedTable);
    }


    @Test
    public void testCreateRole() throws Exception {
        roleDao.create(new Role("manager"));

        TestUtils.assertTables(
                "ROLE",
                "src/test/resources/after-create.xml",
                getConnection()
        );
    }

    @Test
    public void testUpdateRole() throws Exception {
        roleDao.update(new Role(1, "customer"));

        TestUtils.assertTables(
                "ROLE",
                "src/test/resources/after-update.xml",
                getConnection()
        );
    }


    @Test
    public void testRemoveRole() throws Exception {
        roleDao.remove(new Role(1, "user"));

        TestUtils.assertTables(
                "ROLE",
                "src/test/resources/after-delete.xml",
                getConnection()
        );
    }

    @Test
    public void testFindRoleByName() {
        Role expectedRole = new Role(1, "user");

        assertEquals("Role must be equals", expectedRole, roleDao.findByName("user"));
    }
}