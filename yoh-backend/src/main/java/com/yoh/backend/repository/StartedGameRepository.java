package com.yoh.backend.repository;

import com.yoh.backend.entity.*;
import com.yoh.backend.request.GameStartRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class StartedGameRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void saveStartedGame(StartedGame startedGame) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(startedGame);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public String getLatestDetailsByGamePatient(GamePatient gamePatient) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(StartedGame.class)
                    .add(Restrictions.eq("gamePatient", gamePatient))
                    .addOrder(Order.desc("dateEnd"));
            List<StartedGame> startedGames = criteria.list();
            return startedGames.isEmpty() ? null : startedGames.get(0).getDetails();
        }
        finally {
            session.close();
        }
    }

    public StartedGame getLatestStartedGameByGamePatient(GamePatient gamePatient) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(StartedGame.class)
                    .add(Restrictions.eq("gamePatient", gamePatient))
                    .addOrder(Order.desc("dateEnd"));
            List<StartedGame> startedGames = criteria.list();
            return startedGames.isEmpty() ? null : startedGames.get(0);
        }
        finally {
            session.close();
        }
    }

    public StartedGame getUnfinishedStartedGameByGamePatient(GamePatient gamePatient) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(StartedGame.class)
                    .add(Restrictions.eq("gamePatient", gamePatient))
                    .add(Restrictions.isNull("dateEnd"));
//                    .add(Restrictions.eq("dateEnd", null));
            List<StartedGame> startedGames = criteria.list();
            return startedGames.isEmpty() ? null : startedGames.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<StartedGame> getStartedGamesByGamePatient(GamePatient gamePatient) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(StartedGame.class)
                    .add(Restrictions.eq("gamePatient", gamePatient));
            return criteria.list();
        }
        finally {
            session.close();
        }
    }
}
