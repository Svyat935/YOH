package com.yoh.backend.repository;

import com.yoh.backend.entity.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

    public List<GamePatient> getAllGamesPatientByPatientOrdered(Patient patient, String order) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(GamePatient.class)
                    .add(Restrictions.eq("patient", patient));

//            if (order.equals("1")) {
//                Criteria criteria1 = criteria.createCriteria("game");
//                criteria1.addOrder(Order.asc("name"));
//            }
//            if (order.equals("-1")) {
//                Criteria criteria1 = criteria.createCriteria("game");
//                criteria1.addOrder(Order.desc("name"));
//            }
//            if (order.equals("2")) {
//                Criteria criteria1 = criteria.createCriteria("game");
//                criteria1.addOrder(Order.asc("type"));
//            }
//            if (order.equals("-2")) {
//                Criteria criteria1 = criteria.createCriteria("game");
//                criteria1.addOrder(Order.desc("type"));
//            }
//            if (order.equals("3")) {
//                criteria.addOrder(Order.asc("gamePatientStatus"));
//            }
//            if (order.equals("-3")) {
//                criteria.addOrder(Order.desc("gamePatientStatus"));
//            }
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
