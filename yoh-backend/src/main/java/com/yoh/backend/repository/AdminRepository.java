package com.yoh.backend.repository;

import com.yoh.backend.entity.Admin;
import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Tutor;
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
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(admin);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
           session.close();
        }
    }

    public void deleteAdmin(Admin admin) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(admin);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Admin getAdminByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class)
                    .add(Restrictions.eq("id", id));
            List<Admin> admins = criteria.list();
            return admins.isEmpty() ? null : admins.get(0);
        }
        finally {
            session.close();
        }
    }

    public Admin getAdminByUser(User user) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Admin.class)
                    .add(Restrictions.eq("user", user));
            List<Admin> adminList = criteria.list();
            return adminList.isEmpty() ? null : adminList.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<Admin> getAllAdmins(){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(Admin.class);
            List<Admin> adminList = criteria.list();
            return adminList.isEmpty() ? null : adminList;
        }finally {
            session.close();
        }
    }
}
