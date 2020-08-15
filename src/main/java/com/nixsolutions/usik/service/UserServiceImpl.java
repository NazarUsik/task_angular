package com.nixsolutions.usik.service;

import com.nixsolutions.usik.exception.UserServiceException;
import com.nixsolutions.usik.model.entity.User;
import com.nixsolutions.usik.model.repository.UserDao;
import com.nixsolutions.usik.model.service.hibernate.HibernateUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao dao;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(HibernateUserDao dao, BCryptPasswordEncoder encoder) {
        this.dao = dao;
        this.encoder = encoder;
    }


    @Override
    public boolean create(User user) throws UserServiceException {
        try {
            if (dao.findByLogin(user.getLogin()) != null)
                throw new UserServiceException(
                        "Login already exist!",
                        HttpStatus.NOT_MODIFIED
                );

            user.setPassword(encoder.encode(user.getPassword()));
            dao.create(user);

            return true;
        } catch (Throwable ex) {
            throw new UserServiceException(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    ex
            );
        }
    }

    @Override
    public List<User> readAll() throws UserServiceException {
        final List<User> users = dao.findAll();

        if (users == null || users.isEmpty()) {
            throw new UserServiceException(
                    "Users do not exist!",
                    HttpStatus.NOT_FOUND
            );
        }

        return users;
    }

    @Override
    public User read(String login) throws UserServiceException {
        final User user = dao.findByLogin(login);

        if (user == null) {
            throw new UserServiceException(
                    "User[" + login + "] do not exist!",
                    HttpStatus.NOT_FOUND
            );
        }

        return user;
    }

    @Override
    public boolean update(User user) throws UserServiceException {
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            dao.update(user);

            return true;
        } catch (Throwable ex) {
            throw new UserServiceException(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    ex
            );
        }
    }

    @Override
    public boolean delete(String login) throws UserServiceException {
        try {
            User user = dao.findByLogin(login);

            if (user == null)
                throw new UserServiceException(
                        "User[" + login + "] do not exist!",
                        HttpStatus.NOT_FOUND
                );

            dao.remove(user);

            return true;
        } catch (Throwable ex) {
            throw new UserServiceException(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    ex
            );
        }
    }
}
