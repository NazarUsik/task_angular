package com.nixsolutions.usik;

import com.nixsolutions.usik.model.entity.Role;
import com.nixsolutions.usik.model.repository.RoleDao;
import com.nixsolutions.usik.model.repository.UserDao;
import com.nixsolutions.usik.model.service.hibernate.HibernateRoleDao;
import com.nixsolutions.usik.model.service.hibernate.HibernateUserDao;
import com.nixsolutions.usik.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String startQuery = readQuery("https://raw.githubusercontent.com/" +
                "NazarUsik/OnlineShop/master/sql/insert.sql");

        Session session = HibernateUtils.getSession();
        NativeQuery sqlQuery = session.createSQLQuery(startQuery);
        sqlQuery.executeUpdate();
        session.getTransaction().commit();


        RoleDao roleDao = new HibernateRoleDao();
        System.out.println(roleDao.findByName("admin"));

        roleDao.remove(new Role(1, "admin"));
        System.out.println(roleDao.findByName("admin"));

        roleDao.update(new Role(2, "us"));
        System.out.println(roleDao.findByName("us"));

        roleDao.create(new Role(3, "manager"));
        System.out.println(roleDao.findByName("manager"));

        UserDao userDao = new HibernateUserDao();
        userDao.findAll().forEach(System.out::println);

    }

    public static String readQuery(String path) {
        StringBuilder builder = new StringBuilder();
        try {
            Scanner in = new Scanner(new BufferedInputStream(new URL(path).openStream()));
            while (in.hasNextLine()) {
                builder.append(in.nextLine()).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString().trim();
    }

}
