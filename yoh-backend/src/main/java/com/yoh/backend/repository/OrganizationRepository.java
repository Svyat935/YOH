package com.yoh.backend.repository;

import com.yoh.backend.entity.Organization;
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

        //Start transaction
        session.beginTransaction();

        //Transaction
        session.saveOrUpdate(organization);

        //End transaction
        session.getTransaction().commit();
    }

    public Organization getOrganizationByUUID(UUID id) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Organization.class)
                .add(Restrictions.eq("id", id));
        List<Organization> organizations = criteria.list();
        return organizations.isEmpty() ? null : organizations.get(0);
    }
}
