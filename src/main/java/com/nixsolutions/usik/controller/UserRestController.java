package com.nixsolutions.usik.controller;

import com.nixsolutions.usik.model.entity.User;
import com.nixsolutions.usik.model.service.hibernate.HibernateUserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin(
        origins = "http://localhost:4200",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE
        }
)
public class UserRestController {

    private static final Logger logger =
            LogManager.getLogger(UserRestController.class.getName());

    @Autowired
    private HibernateUserDao service;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        try {
            if (service.findByLogin(user.getLogin()) != null)
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

            user.setPassword(encoder.encode(user.getPassword()));
            service.create(user);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> read() {
        final List<User> users = service.findAll();

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "{login}")
    public ResponseEntity<User> read(@PathVariable String login) {
        final User user = service.findByLogin(login);

        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody User user) {
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            service.update(user);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping(value = "{login}")
    public ResponseEntity<?> delete(@PathVariable String login) {
        try {
            User user = service.findByLogin(login);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            service.remove(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
}