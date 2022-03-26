package com.yoh.backend.repository;

import com.yoh.backend.entity.Question;
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
public class QuestionRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createQuestion(Question question) {
        try (Session session = sessionFactory.openSession()) {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(question);

            //End transaction
            session.getTransaction().commit();
        }
    }

    public Question getQuestionByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Question.class)
                .add(Restrictions.eq("id", id));
        List<Question> questions = criteria.list();
        return questions.isEmpty() ? null : questions.get(0);
    }
}