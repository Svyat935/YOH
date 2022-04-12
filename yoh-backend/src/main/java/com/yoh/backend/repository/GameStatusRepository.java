package com.yoh.backend.repository;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.GameStatistic;
import com.yoh.backend.entity.GameStatus;
import com.yoh.backend.entity.Patient;
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
public class GameStatusRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createGameStatus(GameStatus gameStatus) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(gameStatus);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteGameStatus(GameStatus gameStatus) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(gameStatus);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public GameStatus getGameStatusByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GameStatus.class)
                    .add(Restrictions.eq("id", id));
            List<GameStatus> gameStatuses = criteria.list();
            return gameStatuses.isEmpty() ? null : gameStatuses.get(0);
        }
        finally {
            session.close();
        }
    }

    public GameStatus getGameStatusByGameAndPatient(Game game, Patient patient) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GameStatus.class)
                    .add(Restrictions.eq("game", game))
                    .add(Restrictions.eq("patient", patient));
            List<GameStatus> gameStatuses = criteria.list();
            return gameStatuses.isEmpty() ? null : gameStatuses.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<GameStatus> getAllGameStatuses(){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(GameStatus.class);
            List<GameStatus> gameStatusList = criteria.list();
            return gameStatusList.isEmpty() ? null : gameStatusList;
        }finally {
            session.close();
        }
    }
}
