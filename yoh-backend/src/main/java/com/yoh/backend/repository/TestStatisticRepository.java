package com.yoh.backend.repository;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.TestStatistic;
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
public class TestStatisticRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createTestStatistic(TestStatistic testStatistic) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(testStatistic);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteTestStatistic(TestStatistic testStatistic) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(testStatistic);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public TestStatistic getTestStatisticByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(TestStatistic.class)
                    .add(Restrictions.eq("id", id));
            List<TestStatistic> testStatistics = criteria.list();
            return testStatistics.isEmpty() ? null : testStatistics.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<TestStatistic> getAllTestStatistics(){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(TestStatistic.class);
            List<TestStatistic> testStatisticList = criteria.list();
            return testStatisticList.isEmpty() ? null : testStatisticList;
        }finally {
            session.close();
        }
    }
}

