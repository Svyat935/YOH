package com.yoh.backend.repository;

import com.yoh.backend.entity.Answer;
import com.yoh.backend.entity.Game;
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
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(answer);
            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteAnswer(Answer answer) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(answer);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Answer getAnswerByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Answer.class)
                    .add(Restrictions.eq("id", id));
            List<Answer> answers = criteria.list();
            return answers.isEmpty() ? null : answers.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<Answer> getAllAnswers(){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(Answer.class);
            List<Answer> answerList = criteria.list();
            return answerList.isEmpty() ? null : answerList;
        }finally {
            session.close();
        }
    }

}
