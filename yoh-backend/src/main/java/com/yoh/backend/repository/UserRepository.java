package com.yoh.backend.repository;

import com.yoh.backend.entity.*;
import io.swagger.models.auth.In;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
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

    public List<User> getAllUsersByAdmin(Integer role, String order, String regex) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class)
                    .add(Restrictions.ne("role", 0));
            if (role != -1)
                criteria.add(Restrictions.eq("role", role));

            if (!regex.equals(""))
                criteria.add(Restrictions.like("login", regex, MatchMode.ANYWHERE).ignoreCase());

            switch (order) {
                case "1" -> criteria.addOrder(Order.asc("login"));
                case "-1" -> criteria.addOrder(Order.desc("login"));
                case "2" -> criteria.addOrder(Order.asc("dateRegistration"));
                case "-2" -> criteria.addOrder(Order.desc("dateRegistration"));
            }
            List<User> users = criteria.list();
            return users;
        }
        finally {
            session.close();
        }
    }

    public List<User> getAllUsersByAdminPaginated(Integer role, String order, String regex, int start, int count) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class)
                    .add(Restrictions.ne("role", 0));
            if (role != -1)
                criteria.add(Restrictions.eq("role", role));

            if (!regex.equals(""))
                criteria.add(Restrictions.like("login", regex, MatchMode.ANYWHERE).ignoreCase());

            switch (order) {
                case "1" -> criteria.addOrder(Order.asc("login"));
                case "-1" -> criteria.addOrder(Order.desc("login"));
                case "2" -> criteria.addOrder(Order.asc("dateRegistration"));
                case "-2" -> criteria.addOrder(Order.desc("dateRegistration"));
            }
            criteria.setFirstResult(start);
            criteria.setMaxResults(count);
            List<User> users = criteria.list();
            return users;
        }
        finally {
            session.close();
        }
    }

    public int getAllUsersByAdminCount(Integer role, String regex) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class)
                    .add(Restrictions.ne("role", 0));
            if (role != -1)
                criteria.add(Restrictions.eq("role", role));

            if (!regex.equals(""))
                criteria.add(Restrictions.like("login", regex, MatchMode.ANYWHERE).ignoreCase());

            criteria.setProjection(Projections.rowCount());
            return (int)(long)criteria.uniqueResult();
//            List<User> users = criteria.list();
//            return users.size();
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
