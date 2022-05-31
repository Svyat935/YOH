package com.yoh.backend.repository;

import com.yoh.backend.entity.Game;
import com.yoh.backend.enums.GameStatus;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
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
            session.delete(session.contains(game) ? game : session.merge(game));
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


    public List<Game> getAllActiveGames(String order, String regex, String typeRegex){
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Game.class)
                    .add(Restrictions.eq("gameStatus", GameStatus.ACTIVE));
            if (!regex.equals(""))
                criteria.add(Restrictions.like("name", regex, MatchMode.ANYWHERE).ignoreCase());
            if (!typeRegex.equals(""))
                criteria.add(Restrictions.like("type", typeRegex, MatchMode.ANYWHERE).ignoreCase());
            switch (order) {
                case "1" -> criteria.addOrder(Order.asc("name"));
                case "-1" -> criteria.addOrder(Order.desc("name"));
                case "2" -> criteria.addOrder(Order.asc("dateAdding"));
                case "-2" -> criteria.addOrder(Order.desc("dateAdding"));
                case "3" -> criteria.addOrder(Order.asc("type"));
                case "-3" -> criteria.addOrder(Order.desc("type"));
            }
            List<Game> games = criteria.list();
            return games;
//            return games.isEmpty() ? null : games;
        }finally {
            session.close();
        }
    }


    public List<Game> getAllGames(String order){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(Game.class);
            switch (order) {
                case "1" -> criteria.addOrder(Order.asc("name"));
                case "-1" -> criteria.addOrder(Order.desc("name"));
                case "2" -> criteria.addOrder(Order.asc("dateAdding"));
                case "-2" -> criteria.addOrder(Order.desc("dateAdding"));
                case "3" -> criteria.addOrder(Order.asc("type"));
                case "-3" -> criteria.addOrder(Order.desc("type"));
            }
            List<Game> games = criteria.list();
            return games;
//            return games.isEmpty() ? null : games;
        }finally {
            session.close();
        }
    }
}
