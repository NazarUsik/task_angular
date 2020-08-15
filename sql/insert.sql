CREATE SCHEMA IF NOT EXISTS TRAINING;
SET SCHEMA TRAINING;

CREATE TABLE IF NOT EXISTS TRAINING.USER
(
    id         NUMBER  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    login      varchar NOT NULL,
    password   varchar NOT NULL,
    email      varchar NOT NULL UNIQUE,
    first_name varchar NOT NULL,
    last_name  varchar NOT NULL,
    birthday   DATE    NOT NULL,
    role_id    NUMBER  NOT NULL
);

CREATE TABLE IF NOT EXISTS TRAINING.ROLE
(
    id   NUMBER  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR NOT NULL
);

ALTER TABLE IF EXISTS TRAINING.USER
    ADD CONSTRAINT IF NOT EXISTS FKUser FOREIGN KEY (role_id) REFERENCES TRAINING.ROLE (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

INSERT INTO TRAINING.ROLE
VALUES (DEFAULT, 'admin'),
       (DEFAULT, 'user');

INSERT INTO TRAINING.USER
VALUES (DEFAULT, 'user', '$2a$10$ZotEfi9LbLZAn2OPy9JizO4pVgt23wNcCVmJgLvGtWHAQIFc7Beki', 'user1@example.mail.com', 'user', 'user', '2000-10-20', 2),
       (DEFAULT, 'user2', '$2a$10$WQ6kvNUAyC4DV.GaVrDyg.pxEvwfJKdF91QkCWdI91DojyMYjRYgu', 'user2@example.mail.com', 'user2', 'user2', '1980-10-20', 2),
       (DEFAULT, 'user3', '$2a$10$2BxEd4KLwsiT9RHCJWzxCuohdyTQq2x4tyMiFmBa1XuABb9/yyI6e', 'user3@example.mail.com', 'user3', 'user3', '1990-10-20', 2),
       (DEFAULT, 'admin', '$2a$10$gwTzOPvm7ce/mm7OE2g5Iu6Kh/NLjlKnmUncVfV24qgb16AsHTF7.', 'admin@example.mail.com', 'admin', 'admin', '2010-10-20', 1);