package com.nixsolutions.usik.model.service.jdbc;

import com.nixsolutions.usik.constants.EntityFields;
import com.nixsolutions.usik.constants.SqlQuery;
import com.nixsolutions.usik.model.entity.Role;
import com.nixsolutions.usik.model.repository.RoleDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcRoleDao implements RoleDao {

    private static final Logger logger = LogManager.getLogger(JdbcRoleDao.class.getName());

    public void create(Role role) {
        logger.info("Create new role: {}", role);

        try {
            Connection connection = AbstractJdbcDao.createConnection();

            PreparedStatement statement = connection.prepareStatement(SqlQuery.INSERT_ROLE);
            statement.setString(1, role.getName());

            statement.execute();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }

    public void update(Role role) {
        logger.info("Update role: {}", role);

        try {
            Connection connection = AbstractJdbcDao.createConnection();


            PreparedStatement statement = connection.prepareStatement(SqlQuery.UPDATE_ROLE);
            statement.setString(1, role.getName());
            statement.setLong(2, role.getId());


            statement.execute();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }

    public void remove(Role role) {
        logger.info("Remove role: {}", role);

        try {
            Connection connection = AbstractJdbcDao.createConnection();

            PreparedStatement statement = connection.prepareStatement(SqlQuery.DELETE_ROLE);
            statement.setLong(1, role.getId());

            statement.execute();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }

    public Role findByName(String name) {
        logger.info("Find role by name: {}", name);

        try {
            Connection connection = AbstractJdbcDao.createConnection();

            PreparedStatement statement = connection.prepareStatement(SqlQuery.FIND_ROLE_BY_NAME);
            statement.setString(1, name);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                Role role = new Role(
                        results.getLong(EntityFields.ID),
                        results.getString(EntityFields.ROLE_NAME)
                );

                return role;
            }

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return null;
    }

}
