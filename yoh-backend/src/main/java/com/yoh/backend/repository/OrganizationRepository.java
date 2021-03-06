package com.yoh.backend.repository;

import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Organization;
import com.yoh.backend.entity.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
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

    public List<Organization> getAllOrganizationsOrdered(String order, String regex) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Organization.class);
            if (!regex.equals(""))
                criteria.add(Restrictions.like("name", regex, MatchMode.ANYWHERE).ignoreCase());

            switch (order) {
                case "1" -> criteria.addOrder(Order.asc("name"));
                case "-1" -> criteria.addOrder(Order.desc("name"));
                case "2" -> criteria.addOrder(Order.asc("dateRegistration"));
                case "-2" -> criteria.addOrder(Order.desc("dateRegistration"));
            }
            return criteria.list();
        }
        finally {
            session.close();
        }
    }

    public List<Organization> getAllOrganizationsOrderedPaginated(String order, String regex, int start, int limit) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Organization.class);
            if (!regex.equals(""))
                criteria.add(Restrictions.like("name", regex, MatchMode.ANYWHERE).ignoreCase());

            switch (order) {
                case "1" -> criteria.addOrder(Order.asc("name"));
                case "-1" -> criteria.addOrder(Order.desc("name"));
                case "2" -> criteria.addOrder(Order.asc("dateRegistration"));
                case "-2" -> criteria.addOrder(Order.desc("dateRegistration"));
            }
            criteria.setFirstResult(start);
            criteria.setMaxResults(limit);
            return criteria.list();
        }
        finally {
            session.close();
        }
    }

    public int getAllOrganizationsCount(String regex) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(Organization.class);
            if (!regex.equals(""))
                criteria.add(Restrictions.like("name", regex, MatchMode.ANYWHERE).ignoreCase());
            criteria.setProjection(Projections.rowCount());
            return (int)(long)criteria.uniqueResult();
        }
        finally {
            session.close();
        }
    }
}
