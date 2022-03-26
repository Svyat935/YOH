package com.yoh.backend.repository;

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
public class UserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public User getUserByLogin(String login) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class)
                    .add(Restrictions.eq("login", login));
            List<User> users = criteria.list();
            return users.isEmpty() ? null : users.get(0);
        }
        finally {
            session.close();
        }
    }

    public User getUserByEmail(String email) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class)
                    .add(Restrictions.eq("email", email));
            List<User> users = criteria.list();
            return users.isEmpty() ? null : users.get(0);
        }
        finally {
            session.close();
        }
    }

    public User getUserByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class)
                    .add(Restrictions.eq("id", id));
            List<User> users = criteria.list();
            return users.isEmpty() ? null : users.get(0);
        }
        finally {
            session.close();
        }
    }

    public void createUser(User user) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(user);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
}
