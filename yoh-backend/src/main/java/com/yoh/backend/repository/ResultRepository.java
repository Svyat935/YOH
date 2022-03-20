package com.yoh.backend.repository;

import com.yoh.backend.entity.Result;
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
public class ResultRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createResult(Result result) {
        Session session = sessionFactory.openSession();

        //Start transaction
        session.beginTransaction();

        //Transaction
        session.saveOrUpdate(result);

        //End transaction
        session.getTransaction().commit();
    }

    public Result getResultByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Result.class)
                .add(Restrictions.eq("id", id));
        List<Result> results = criteria.list();
        return results.isEmpty() ? null : results.get(0);
    }
}