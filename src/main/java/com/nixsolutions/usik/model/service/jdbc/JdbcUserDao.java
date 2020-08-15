package com.nixsolutions.usik.model.service.jdbc;

import com.nixsolutions.usik.constants.EntityFields;
import com.nixsolutions.usik.constants.SqlQuery;
import com.nixsolutions.usik.model.entity.User;
import com.nixsolutions.usik.model.repository.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class JdbcUserDao implements UserDao {

    private static final Logger logger = LogManager.getLogger(JdbcUserDao.class.getName());

    private void setPreparedStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getLogin());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getFirstName());
        statement.setString(5, user.getLastName());
        statement.setDate(6, user.getBirthday());
        statement.setLong(7, user.getRoleId());
    }

    private User createUser(ResultSet results) throws SQLException {
        return new User(
                results.getLong(EntityFields.ID),
                results.getString(EntityFields.USER_LOGIN),
                results.getString(EntityFields.USER_PASSWORD),
                results.getString(EntityFields.USER_EMAIL),
                results.getString(EntityFields.USER_FIRST_NAME),
                results.getString(EntityFields.USER_LAST_NAME),
                results.getDate(EntityFields.USER_BIRTHDAY),
                results.getLong(EntityFields.USER_ROLE_ID)
        );
    }

    public void create(User user) {
        try {
            logger.info("Create new user: {}", user);

            Connection connection = AbstractJdbcDao.createConnection();

            PreparedStatement statement = connection.prepareStatement(SqlQuery.INSERT_USER);
            setPreparedStatement(statement, user);


            statement.execute();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }

    public void update(User user) {
        logger.info("Update user: {}", user);

        Connection connection = null;
        try {
            connection = AbstractJdbcDao.createConnection();
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(SqlQuery.UPDATE_USER);
            setPreparedStatement(statement, user);
            statement.setLong(8, user.getId());

            statement.execute();
            connection.commit();
        } catch (SQLException throwables) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            logger.error(throwables.getMessage(), throwables);
        }
    }

    public void remove(User user) {
        logger.info("Remove user: {}", user);

        try {
            Connection connection = AbstractJdbcDao.createConnection();

            PreparedStatement statement = connection.prepareStatement(SqlQuery.DELETE_USER);
            statement.setLong(1, user.getId());

            statement.execute();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }

    public List<User> findAll() {
        logger.info("findAll() method");

        List<User> list = new LinkedList<>();

        try {
            Connection connection = AbstractJdbcDao.createConnection();

            ResultSet result = connection.createStatement().executeQuery(SqlQuery.FIND_ALL_USERS);

            while (result.next()) {
                list.add(createUser(result));
            }

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        logger.info("All users: {}", list);

        return list;
    }

    public User findByLogin(String login) {
        logger.info("Find user by login: {}", login);

        try {
            Connection connection = AbstractJdbcDao.createConnection();

            PreparedStatement statement = connection.prepareStatement(SqlQuery.FIND_USER_BY_LOGIN);
            statement.setString(1, login);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                User user = createUser(results);
                logger.info("User exist: {}", user);
                return user;
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return null;
    }

    public User findByEmail(String email) {
        logger.info("Find user by email: {}", email);

        try {
            Connection connection = AbstractJdbcDao.createConnection();

            PreparedStatement statement = connection.prepareStatement(SqlQuery.FIND_USER_BY_EMAIL);
            statement.setString(1, email);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                User user = createUser(results);
                logger.info("User exist: {}", user);
                return user;
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return null;
    }

}
