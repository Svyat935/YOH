package com.yoh.backend.repository;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Organization;
import com.yoh.backend.entity.Patient;
import com.yoh.backend.entity.User;
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

    public List<Patient> getAllPatientsByOrganization(Organization organization) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Patient.class)
                    .add(Restrictions.eq("organization", organization));
            List<Patient> patientsList = criteria.list();
            return patientsList;
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
