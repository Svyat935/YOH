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
public class TestStatusRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createTestStatus(TestStatus testStatus) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(testStatus);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public TestStatus getTestStatusByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(TestStatus.class)
                    .add(Restrictions.eq("id", id));
            List<TestStatus> testStatuses = criteria.list();
            return testStatuses.isEmpty() ? null : testStatuses.get(0);
        }
        finally {
            session.close();
        }
    }

    public TestStatus getTestStatusByTestAndPatient(Test test, Patient patient) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GameStatus.class)
                    .add(Restrictions.eq("test", test))
                    .add(Restrictions.eq("patient", patient));
            List<TestStatus> testStatuses = criteria.list();
            return testStatuses.isEmpty() ? null : testStatuses.get(0);
        }
        finally {
            session.close();
        }
    }
}
