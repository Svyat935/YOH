package com.yoh.backend.repository;

import com.yoh.backend.entity.GameStatistic;
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
public class GameStatisticRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createGameStatistic(GameStatistic gameStatistic) {
        try (Session session = sessionFactory.openSession()) {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(gameStatistic);

            //End transaction
            session.getTransaction().commit();
        }
    }

    public GameStatistic getGameStatisticByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(GameStatistic.class)
                .add(Restrictions.eq("id", id));
        List<GameStatistic> gameStatistics = criteria.list();
        return gameStatistics.isEmpty() ? null : gameStatistics.get(0);
    }
}
