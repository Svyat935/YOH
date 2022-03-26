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
public class TutorRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createTutor(Tutor tutor) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(tutor);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Tutor getTutorByUser(User user) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Tutor.class)
                    .add(Restrictions.eq("user", user));
            List<Tutor> tutorList = criteria.list();
            return tutorList.isEmpty() ? null : tutorList.get(0);
        }
        finally {
            session.close();
        }
    }

//    public List<Patient> getPatientsByTutor(Tutor tutor) {
//        Session session = sessionFactory.openSession();
//        Criteria criteria = session.createCriteria(Tutor.class)
//                .add(Restrictions.eq("user", tutor));
//        List<Patient> tutorList = criteria.list();
//        return tutorList.isEmpty() ? null : tutorList.get(0).getTutor().getPatients();
//    }

    public Tutor getTutorByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Tutor.class)
                    .add(Restrictions.eq("id", id));
            List<Tutor> tutors = criteria.list();
            return tutors.isEmpty() ? null : tutors.get(0);
        }
        finally {
            session.close();
        }
    }
}
