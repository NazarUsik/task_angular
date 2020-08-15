package com.nixsolutions.usik.jdbc;

import com.nixsolutions.usik.model.entity.User;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.SQLException;

public class TestUtils {

    public static void assertTables(
            String tableName,
            String datasetPath,
            IDatabaseConnection connection) throws
            SQLException,
            FileNotFoundException,
            DatabaseUnitException {

        IDataSet databaseDataSet = connection.createDataSet();
        ITable actualTable = databaseDataSet.getTable(tableName);

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(
                new FileInputStream(datasetPath));
        ITable expectedTable = expectedDataSet.getTable(tableName);

        Assertion.assertEquals(actualTable, expectedTable);
    }

    public static User createUser(int number) {
        return new User(
                number,
                "user" + number,
                number + "resu",
                "user" + number + "@gmail.com",
                "user",
                "user",
                Date.valueOf("2000-01-01"),
                1
        );
    }

}
