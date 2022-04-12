package com.yoh.backend.repository;

import com.yoh.backend.entity.*;
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

    public List<User> getAllUsers() {
        //TODO подогнать под общий вид
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            List<User> users = criteria.list();
            return users;
        }
        finally {
            session.close();
        }
    }

    public void deleteUser(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

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

//    public Integer getRoleByUUID(UUID id){
//        Session session = sessionFactory.openSession();
//        try{
//            Criteria criteria = session.createCriteria(Admin.class).add(Restrictions.eq("user.id", id));
//            List<Admin> admins = criteria.list();
//            if (!admins.isEmpty()){
//                return 0;
//            }
//            criteria = session.createCriteria(Patient.class).add(Restrictions.eq("user.id", id));
//            List<Tutor> patients = criteria.list();
//            if (!patients.isEmpty()){
//                return 1;
//            }
//            criteria = session.createCriteria(Researcher.class).add(Restrictions.eq("user.id", id));
//            List<Tutor> researches = criteria.list();
//            if (!researches.isEmpty()){
//                return 2;
//            }
//            criteria = session.createCriteria(Tutor.class).add(Restrictions.eq("user.id", id));
//            List<Tutor> tutors = criteria.list();
//            if (!tutors.isEmpty()){
//                return 3;
//            }
//            return null;
//        }
//        finally {
//            session.close();
//        }
//    }

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
