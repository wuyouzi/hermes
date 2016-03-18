package com.ucredit.hermes.dao.juxinli;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SaveEntityDAO {
    @Autowired
    protected SessionFactory sessionFactory;

    public void addEntity(Object entity) {
        this.sessionFactory.getCurrentSession().save(entity);
    }

}
