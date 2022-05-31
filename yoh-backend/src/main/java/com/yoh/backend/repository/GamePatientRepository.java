package com.yoh.backend.repository;

import com.yoh.backend.entity.*;
import com.yoh.backend.enums.GamePatientStatus;
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
public class GamePatientRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createGamePatient(GamePatient gamePatient) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(gamePatient);
            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteGamePatient(GamePatient gamePatient) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.delete(gamePatient);
            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

//    public void deactivateGame(Game game) {
//        Session session = sessionFactory.openSession();
//        try {
//            Criteria criteria = session.createCriteria(GamePatient.class)
//                    .add(Restrictions.eq("game", game));
//            List<GamePatient> gamePatientList = criteria.list();
//            session.beginTransaction();
//            for (GamePatient gamePatient: gamePatientList){
//                gamePatient.setGamePatientStatus(GamePatientStatus.DELETED);
//                session.update(gamePatient);
//            }
//            session.update(game);
//            session.getTransaction().commit();
//        }
//        finally {
//            session.close();
//        }
//    }

    public GamePatient getGamePatientByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GamePatient.class)
                    .add(Restrictions.eq("id", id));
            List<GamePatient> gamePatientList = criteria.list();
            return gamePatientList.isEmpty() ? null : gamePatientList.get(0);
        }
        finally {
            session.close();
        }
    }

    public GamePatient getGamePatientByGameAndPatient(Game game, Patient patient) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GamePatient.class)
                    .add(Restrictions.eq("game", game))
                    .add(Restrictions.eq("patient", patient));
            List<GamePatient> gamePatientList = criteria.list();
            return gamePatientList.isEmpty() ? null : gamePatientList.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<GamePatient> getAllGamesPatientByPatientOrdered(Patient patient, String order, String typeRegex, String regex) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GamePatient.class)
                    .add(Restrictions.eq("patient", patient));
            if (!typeRegex.equals("")){
                Criteria criteriaTypeRegex = criteria.createCriteria("game");
                criteriaTypeRegex.add(Restrictions.like("type", typeRegex, MatchMode.ANYWHERE).ignoreCase());
            }
            if (!regex.equals("")){
                Criteria criteriaRegex = criteria.createCriteria("game");
                criteriaRegex.add(Restrictions.like("name", regex, MatchMode.ANYWHERE).ignoreCase());
            }

            switch (order) {
                case "1" -> {
                    Criteria criteria1 = criteria.createCriteria("game");
                    criteria1.addOrder(Order.asc("name"));
                }
                case "-1" -> {
                    Criteria criteria1 = criteria.createCriteria("game");
                    criteria1.addOrder(Order.desc("name"));
                }
                case "2" -> {
                Criteria criteria1 = criteria.createCriteria("game");
                criteria1.addOrder(Order.asc("type"));
                }
                case "-2" -> {
                Criteria criteria1 = criteria.createCriteria("game");
                criteria1.addOrder(Order.desc("type"));
                }
                case "3" -> criteria.addOrder(Order.asc("gamePatientStatus"));
                case "-3" -> criteria.addOrder(Order.desc("gamePatientStatus"));
            }
            List<GamePatient> gamePatientList = criteria.list();
            return gamePatientList.isEmpty() ? List.of() : gamePatientList;
        }
        finally {
            session.close();
        }
    }

    public List<GamePatient> getAllActiveGamesPatientByPatientOrdered(Patient patient, String order, String typeRegex, String regex) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GamePatient.class)
                    .add(Restrictions.eq("patient", patient))
                    .add(Restrictions.eq("gamePatientStatus", GamePatientStatus.ACTIVE));
            if (!typeRegex.equals("")){
                Criteria criteriaTypeRegex = criteria.createCriteria("game");
                criteriaTypeRegex.add(Restrictions.like("type", typeRegex, MatchMode.ANYWHERE).ignoreCase());
            }
            if (!regex.equals("")){
                Criteria criteriaRegex = criteria.createCriteria("game");
                criteriaRegex.add(Restrictions.like("name", regex, MatchMode.ANYWHERE).ignoreCase());
            }
            switch (order) {
                case "1" -> {
                    Criteria criteria1 = criteria.createCriteria("game");
                    criteria1.addOrder(Order.asc("name"));
                }
                case "-1" -> {
                    Criteria criteria1 = criteria.createCriteria("game");
                    criteria1.addOrder(Order.desc("name"));
                }
                case "2" -> {
                    Criteria criteria1 = criteria.createCriteria("game");
                    criteria1.addOrder(Order.asc("type"));
                }
                case "-2" -> {
                    Criteria criteria1 = criteria.createCriteria("game");
                    criteria1.addOrder(Order.desc("type"));
                }
                case "3" -> criteria.addOrder(Order.asc("gamePatientStatus"));
                case "-3" -> criteria.addOrder(Order.desc("gamePatientStatus"));
            }
            List<GamePatient> gamePatientList = criteria.list();
            return gamePatientList.isEmpty() ? List.of() : gamePatientList;
        }
        finally {
            session.close();
        }
    }

    public List<GamePatient> getAllGamesPatientByPatient(Patient patient) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GamePatient.class)
                    .add(Restrictions.eq("patient", patient));
            List<GamePatient> gamePatientList = criteria.list();
            return gamePatientList.isEmpty() ? List.of() : gamePatientList;
        }
        finally {
            session.close();
        }
    }


    public List<GamePatient> getAllPatientByGame(Game game) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GamePatient.class)
                    .add(Restrictions.eq("game", game));
            List<GamePatient> gamePatientList = criteria.list();
            return gamePatientList.isEmpty() ? List.of() : gamePatientList;
        }
        finally {
            session.close();
        }
    }

    public List<GamePatient> getAllGamePatients(){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(GamePatient.class);
            List<GamePatient> gamePatientList = criteria.list();
            return gamePatientList;
        }finally {
            session.close();
        }
    }


}
