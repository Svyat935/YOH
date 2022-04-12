package com.yoh.backend.repository;

import com.yoh.backend.entity.Game;
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
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(question);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteQuestion(Question question) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(question);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Question getQuestionByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Question.class)
                    .add(Restrictions.eq("id", id));
            List<Question> questions = criteria.list();
            return questions.isEmpty() ? null : questions.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<Question> getAllQuestions(){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(Question.class);
            List<Question> questionList = criteria.list();
            return questionList.isEmpty() ? null : questionList;
        }finally {
            session.close();
        }
    }
}