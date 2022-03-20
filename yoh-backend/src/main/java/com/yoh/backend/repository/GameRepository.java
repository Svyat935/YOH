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

        //Start transaction
        session.beginTransaction();

        //Transaction
        session.saveOrUpdate(game);

        //End transaction
        session.getTransaction().commit();
    }

    public Game getGameByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Game.class)
                .add(Restrictions.eq("id", id));
        List<Game> games = criteria.list();
        return games.isEmpty() ? null : games.get(0);
    }
}
