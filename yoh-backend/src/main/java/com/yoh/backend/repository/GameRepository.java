package com.yoh.backend.repository;

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
public class GameRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createGame(Game game) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(game);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteGame(Game game) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(game);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Game getGameByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Game.class)
                    .add(Restrictions.eq("id", id));
            List<Game> games = criteria.list();
            return games.isEmpty() ? null : games.get(0);
        }
        finally {
            session.close();
        }
    }

    public Game getGameByName(String name) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Game.class)
                    .add(Restrictions.eq("name", name));
            List<Game> games = criteria.list();
            return games.isEmpty() ? null : games.get(0);
        }
        finally {
            session.close();
        }
    }


    public List<Game> getAllGames(){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(Game.class);
            List<Game> games = criteria.list();
            return games;
//            return games.isEmpty() ? null : games;
        }finally {
            session.close();
        }
    }
}
