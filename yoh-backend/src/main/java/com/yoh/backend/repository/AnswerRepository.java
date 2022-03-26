package com.yoh.backend.repository;

import com.yoh.backend.entity.Answer;
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
public class AnswerRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createAnswer(Answer answer) {
        try (Session session = sessionFactory.openSession()) {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(answer);

            //End transaction
            session.getTransaction().commit();
        }
    }

    public Answer getAnswerByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Answer.class)
                .add(Restrictions.eq("id", id));
        List<Answer> answers = criteria.list();
        return answers.isEmpty() ? null : answers.get(0);
    }
}
