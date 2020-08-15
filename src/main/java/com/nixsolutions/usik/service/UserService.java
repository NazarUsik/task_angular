package com.nixsolutions.usik.service;

import com.nixsolutions.usik.exception.UserServiceException;
import com.nixsolutions.usik.model.entity.User;

import java.util.List;

public interface UserService {

    boolean create(User user) throws UserServiceException;

    List<User> readAll() throws UserServiceException;

    User read(String login) throws UserServiceException;

    boolean update(User user) throws UserServiceException;

    boolean delete(String login) throws UserServiceException;

}
