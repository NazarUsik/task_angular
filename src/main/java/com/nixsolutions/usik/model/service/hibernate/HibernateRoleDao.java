package com.nixsolutions.usik.model.service.hibernate;

import com.nixsolutions.usik.constants.EntityFields;
import com.nixsolutions.usik.model.entity.Role;
import com.nixsolutions.usik.model.repository.RoleDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;

import static com.nixsolutions.usik.utils.HibernateUtils.getSession;

@Service
@Transactional
public class HibernateRoleDao implements RoleDao {

    private static final Logger logger = LogManager.getLogger(HibernateRoleDao.class.getName());

    @Override
    public void create(Role role) {
        logger.info("Create new role: {}", role);

        Session session = getSession();

        try {
            session.save(role);
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
    public void update(Role role) {
        logger.info("Update role: {}", role);

        Session session = getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaUpdate<Role> cu = cb.createCriteriaUpdate(Role.class);
        Root<Role> root = cu.from(Role.class);
        cu.set(EntityFields.ROLE_NAME, role.getName());
        cu.where(cb.equal(root.get(EntityFields.ID), role.getId()));

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
    public void remove(Role role) {
        logger.info("Remove role: {}", role);

        Session session = getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<Role> cd = cb.createCriteriaDelete(Role.class);
        Root<Role> root = cd.from(Role.class);
        cd.where(cb.equal(root.get(EntityFields.ID), role.getId()));

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
    public Role findByName(String name) {
        logger.info("Find role by name: {}", name);

        Session session = getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Role> cr = cb.createQuery(Role.class);
        Root<Role> root = cr.from(Role.class);
        cr.select(root).where(cb.equal(root.get(EntityFields.ROLE_NAME), name));

        Query<Role> query = session.createQuery(cr);

        List<Role> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            session.getTransaction().commit();
            return null;
        }

        Role role = resultList.get(0);
        session.getTransaction().commit();
        session.close();
        return role;
    }

}
