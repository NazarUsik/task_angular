package com.nixsolutions.usik.constants;

public final class SqlQuery {

    public static final String INSERT_ROLE =
            "INSERT INTO TRAINING.ROLE VALUES (DEFAULT, ?)";

    public static final String INSERT_USER =
            "INSERT INTO TRAINING.USER VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)";

    public static final String DELETE_ROLE =
            "DELETE FROM TRAINING.ROLE WHERE ID = ?";

    public static final String DELETE_USER =
            "DELETE FROM TRAINING.USER WHERE ID = ?";

    public static final String UPDATE_ROLE =
            "UPDATE TRAINING.ROLE SET NAME = ? WHERE ID = ?";

    public static final String UPDATE_USER =
            "UPDATE TRAINING.USER SET " +
                    "LOGIN = ?, " +
                    "PASSWORD = ?, " +
                    "EMAIL = ?, " +
                    "FIRST_NAME = ?, " +
                    "LAST_NAME = ?, " +
                    "BIRTHDAY = ?, " +
                    "ROLE_ID = ? " +
                    "WHERE ID = ?";

    public static final String FIND_ROLE_BY_NAME =
            "SELECT * FROM TRAINING.ROLE WHERE NAME = ?";

    public static final String FIND_ALL_USERS =
            "SELECT * FROM TRAINING.USER";

    public static final String FIND_USER_BY_LOGIN =
            "SELECT * FROM TRAINING.USER WHERE LOGIN = ?";

    public static final String FIND_USER_BY_EMAIL =
            "SELECT * FROM TRAINING.USER WHERE EMAIL = ?";
}
