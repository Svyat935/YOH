package com.yoh.backend.repository;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Organization;
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
public class OrganizationRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void createOrganization(Organization organization) {
        Session session = sessionFactory.openSession();
        try {
            //Start transaction
            session.beginTransaction();

            //Transaction
            session.saveOrUpdate(organization);

            //End transaction
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteOrganization(Organization organization) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(organization);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Organization getOrganizationByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Organization.class)
                    .add(Restrictions.eq("id", id));
            List<Organization> organizations = criteria.list();
            return organizations.isEmpty() ? null : organizations.get(0);
        }
        finally {
            session.close();
        }
    }

    public Organization getOrganizationByName(String name) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Organization.class)
                    .add(Restrictions.eq("name", name));
            List<Organization> organizations = criteria.list();
            return organizations.isEmpty() ? null : organizations.get(0);
        }
        finally {
            session.close();
        }
    }

    public List<Organization> getAllOrganizations() {
        //TODO подогнать под общий вид
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Organization.class);
            List<Organization> organizations = criteria.list();
            return organizations;
        }
        finally {
            session.close();
        }
    }
}
