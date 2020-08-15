package com.nixsolutions.usik.service;


import com.nixsolutions.usik.model.entity.User;
import com.nixsolutions.usik.model.service.hibernate.HibernateUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    HibernateUserDao userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        User byLogin = userRepository.findByLogin(login);

        System.out.println("find user, login: " + login);
        System.out.println("user: " + byLogin);

        if (byLogin == null) {
            throw new UsernameNotFoundException("User " + login + " was not found in the database");
        }

        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(byLogin.getRoleId() == 1 ? "ADMIN" : "USER"));

        return new org.springframework.security.core.userdetails.User(
                byLogin.getLogin(),
                byLogin.getPassword(),
                roles
        );
    }


}
