package com.ucredit.hermes.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BaseSearchDAO {

    @Autowired
    protected SessionFactory sessionFactory;

    public void addEntity(final Object entity) {
        this.sessionFactory.getCurrentSession().save(entity);
    }

    public void updateEntity(Object entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    public Object getById(Serializable id, Class<?> clazz) {
        return this.sessionFactory.getCurrentSession().get(clazz, id);
    }

    public List<?> selectByHQL(String hql, Map<String, Object> parameters) {
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                query.setParameter(key, parameters.get(key));
            }
        }
        return query.list();
    }
}
