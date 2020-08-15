package com.nixsolutions.usik.model.service.hibernate;

import com.nixsolutions.usik.constants.EntityFields;
import com.nixsolutions.usik.model.entity.User;
import com.nixsolutions.usik.model.repository.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

import static com.nixsolutions.usik.utils.HibernateUtils.getSession;

@Service
@Transactional
public class HibernateUserDao implements UserDao {

    private static final Logger logger = LogManager.getLogger(HibernateUserDao.class.getName());

    @Override
    public void create(User user) {
        logger.info("Create new user: {}", user);

        Session session = getSession();

        try {
            session.save(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            logger.error(e.getMessage(), e);
            throw new HibernateException(e.getMessage(), e.getCause());
        } finally {
            session.close();
        }
    }

    @Override
    public void update(User user) {
        logger.info("Update new user: {}", user);

        Session session = getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaUpdate<User> cu = cb.createCriteriaUpdate(User.class);
        Root<User> root = cu.from(User.class);
        cu.where(cb.equal(root.get(EntityFields.ID), user.getId()));
        cu.set(EntityFields.USER_LOGIN, user.getLogin());
        cu.set(EntityFields.USER_PASSWORD, user.getPassword());
        cu.set(EntityFields.USER_EMAIL, user.getEmail());
        cu.set("firstName", user.getFirstName());
        cu.set("lastName", user.getLastName());
        cu.set(EntityFields.USER_BIRTHDAY, user.getBirthday());
        cu.set("roleId", user.getRoleId());

        try {
            session.createQuery(cu).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            logger.error(e.getMessage(), e);
            throw new HibernateException(e.getMessage(), e.getCause());
        } finally {
            session.close();
        }
    }

    @Override
    public void remove(User user) {
        logger.info("Remove user: {}", user);

        Session session = getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<User> cd = cb.createCriteriaDelete(User.class);
        Root<User> root = cd.from(User.class);
        cd.where(cb.equal(root.get(EntityFields.ID), user.getId()));

        try {
            session.createQuery(cd).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            logger.error(e.getMessage(), e);
            throw new HibernateException(e.getMessage(), e.getCause());
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> findAll() {
        logger.info("findAll() method");
        Session session = getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root);

        TypedQuery<User> query = session.createQuery(cr);
        List<User> resultList = query.getResultList();

        session.getTransaction().commit();
        session.close();

        if (resultList.isEmpty()) {
            return null;
        }

        logger.info("All users: {}", resultList);
        return resultList;
    }

    @Override
    public User findByLogin(String login) {
        logger.info("Find user by login: {}", login);

        Session session = getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(cb.equal(root.get(EntityFields.USER_LOGIN), login));

        Query<User> query = session.createQuery(cr);
        List<User> resultList = query.getResultList();

        session.getTransaction().commit();
        session.close();

        if (resultList.isEmpty()) {
            return null;
        }

        User user = resultList.get(0);
        logger.info("User exist: {}", user);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        logger.info("Find user by email: {}", email);

        Session session = getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(cb.equal(root.get(EntityFields.USER_EMAIL), email));

        Query<User> query = session.createQuery(cr);
        List<User> resultList = query.getResultList();

        session.getTransaction().commit();
        session.close();

        if (resultList.isEmpty()) {
            return null;
        }

        User user = resultList.get(0);
        logger.info("User exist: {}", user);
        return user;
    }
}
