package com.yoh.backend.repository;

import com.yoh.backend.entity.Test;
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
public class TestRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createTest(Test test) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(test);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }

    }

    public Test getTestByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Test.class)
                    .add(Restrictions.eq("id", id));
            List<Test> tests = criteria.list();
            return tests.isEmpty() ? null : tests.get(0);
        }
        finally {
            session.close();
        }
    }
}