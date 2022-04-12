package com.yoh.backend.repository;

import com.yoh.backend.entity.Game;
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
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(gameStatistic);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteGameStatistic(GameStatistic gameStatistic) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(gameStatistic);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public GameStatistic getGameStatisticByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GameStatistic.class)
                    .add(Restrictions.eq("id", id));
            List<GameStatistic> gameStatistics = criteria.list();
            return gameStatistics.isEmpty() ? null : gameStatistics.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<GameStatistic> getAllGameStatistics(){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(GameStatistic.class);
            List<GameStatistic> gameStatisticList = criteria.list();
            return gameStatisticList.isEmpty() ? null : gameStatisticList;
        }finally {
            session.close();
        }
    }
}
