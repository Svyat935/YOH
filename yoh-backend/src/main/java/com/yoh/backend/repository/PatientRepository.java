package com.yoh.backend.repository;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Organization;
import com.yoh.backend.entity.Patient;
import com.yoh.backend.entity.User;
import org.aspectj.weaver.ast.Or;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class PatientRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createPatient(Patient patient) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(patient);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deletePatient(Patient patient) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(patient);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Patient getPatientByUser(User user) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Patient.class)
                    .add(Restrictions.eq("user", user));
            List<Patient> patientsList = criteria.list();
            return patientsList.isEmpty() ? null : patientsList.get(0);
        }
        finally {
            session.close();
        }
    }

    public Patient getPatientByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Patient.class)
                    .add(Restrictions.eq("id", id));
            List<Patient> patients = criteria.list();
            return patients.isEmpty() ? null : patients.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<Patient> getAllPatientsByOrganization(Organization organization, String order, String regex) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Patient.class)
                    .add(Restrictions.eq("organization", organization));
            if (!regex.equals("")) {
                Criterion name = Restrictions.like("name", regex, MatchMode.ANYWHERE).ignoreCase();
                Criterion surname = Restrictions.like("surname", regex, MatchMode.ANYWHERE).ignoreCase();
                Criterion secondName = Restrictions.like("secondName", regex, MatchMode.ANYWHERE).ignoreCase();
                criteria.add(Restrictions.or(name, surname, secondName));
            }
            switch (order) {
                case "1" -> criteria.addOrder(Order.asc("surname"));
                case "-1" -> criteria.addOrder(Order.desc("surname"));

//              Фио
                case "2" -> {
                    criteria.addOrder(Order.asc("surname"));
                    criteria.addOrder(Order.asc("name"));
                    criteria.addOrder(Order.asc("secondName"));
                }
                case "-2" -> {
                    criteria.addOrder(Order.desc("surname"));
                    criteria.addOrder(Order.desc("name"));
                    criteria.addOrder(Order.desc("secondName"));
                }
                case "3" -> criteria.addOrder(Order.asc("birthDate"));
                case "-3" -> criteria.addOrder(Order.desc("birthDate"));
            }
            List<Patient> patientsList = criteria.list();
            return patientsList;
        }
        finally {
            session.close();
        }
    }

    public int getAllPatientsByOrganizationFilteredCount(Organization organization, String regex) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Patient.class)
                    .add(Restrictions.eq("organization", organization));
            if (!regex.equals("")) {
                Criterion name = Restrictions.like("name", regex, MatchMode.ANYWHERE).ignoreCase();
                Criterion surname = Restrictions.like("surname", regex, MatchMode.ANYWHERE).ignoreCase();
                Criterion secondName = Restrictions.like("secondName", regex, MatchMode.ANYWHERE).ignoreCase();
                criteria.add(Restrictions.or(name, surname, secondName));
            }
            criteria.setProjection(Projections.rowCount());
            return (int)(long)criteria.uniqueResult();
        }
        finally {
            session.close();
        }
    }

    public List<Patient> getAllPatientsByOrganizationFilteredPaginated(Organization organization, String regex, String order, int start, int limit) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Patient.class)
                    .add(Restrictions.eq("organization", organization));
//            if (!regex.equals("")) {
//                Criterion name = Restrictions.like("name", regex, MatchMode.ANYWHERE).ignoreCase();
//                Criterion surname = Restrictions.like("surname", regex, MatchMode.ANYWHERE).ignoreCase();
//                Criterion secondName = Restrictions.like("secondName", regex, MatchMode.ANYWHERE).ignoreCase();
//                criteria.add(Restrictions.or(name, surname, secondName));
//            }
//            switch (order) {
//                case "1" -> criteria.addOrder(Order.asc("surname"));
//                case "-1" -> criteria.addOrder(Order.desc("surname"));
//
////              Фио
//                case "2" -> {
//                    criteria.addOrder(Order.asc("surname"));
//                    criteria.addOrder(Order.asc("name"));
//                    criteria.addOrder(Order.asc("secondName"));
//                }
//                case "-2" -> {
//                    criteria.addOrder(Order.desc("surname"));
//                    criteria.addOrder(Order.desc("name"));
//                    criteria.addOrder(Order.desc("secondName"));
//                }
//                case "3" -> criteria.addOrder(Order.asc("birthDate"));
//                case "-3" -> criteria.addOrder(Order.desc("birthDate"));
//            }
//            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
//            System.out.println(criteria.list());
//            criteria.setFirstResult(start);
//            System.out.println(criteria.list());
//            criteria.setMaxResults(limit);
//            System.out.println(criteria.list());
//            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            return criteria.list();
        }
        finally {
            session.close();
        }
    }

    public List<Patient> getAllPatients(){
        Session session = sessionFactory.openSession();
        try{
            Criteria criteria = session.createCriteria(Patient.class);
            List<Patient> patientList = criteria.list();
            return patientList.isEmpty() ? null : patientList;
        }finally {
            session.close();
        }
    }

}
