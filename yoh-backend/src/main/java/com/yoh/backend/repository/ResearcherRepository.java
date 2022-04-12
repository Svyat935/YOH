package com.yoh.backend.repository;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Researcher;
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
public class ResearcherRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createResearcher(Researcher researcher) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(researcher);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }

    }

    public void deleteResearcher(Researcher researcher) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(researcher);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Researcher getResearcherByUser(User user) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Researcher.class)
                    .add(Restrictions.eq("user", user));
            List<Researcher> researcherList = criteria.list();
            return researcherList.isEmpty() ? null : researcherList.get(0);
        }
        finally {
            session.close();
        }
    }

    public Researcher getResearcherByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Researcher.class)
                    .add(Restrictions.eq("id", id));
            List<Researcher> researchers = criteria.list();
            return researchers.isEmpty() ? null : researchers.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<Researcher> getAllResearchers(){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(Researcher.class);
            List<Researcher> researcherList = criteria.list();
            return researcherList.isEmpty() ? null : researcherList;
        }finally {
            session.close();
        }
    }
}