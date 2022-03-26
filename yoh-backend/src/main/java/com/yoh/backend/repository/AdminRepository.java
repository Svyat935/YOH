package com.yoh.backend.repository;

import com.yoh.backend.entity.Admin;
import com.yoh.backend.entity.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class AdminRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createAdmin(Admin admin) {
        try (Session session = sessionFactory.openSession()) {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(admin);

            //End transaction
            session.getTransaction().commit();
        }
    }

    public Admin getAdminByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class)
                .add(Restrictions.eq("id", id));
        List<Admin> admins = criteria.list();
        return admins.isEmpty() ? null : admins.get(0);
    }
}
