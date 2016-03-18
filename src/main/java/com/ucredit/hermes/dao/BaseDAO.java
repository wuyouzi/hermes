/**
 * Copyright(c) 2011-2012 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.BaseModel;
import com.ucredit.hermes.utils.ClassUtils;

/**
 * @author ijay
 * @param <T>
 * @param <S>
 */
@Repository
public abstract class BaseDAO<T extends BaseModel<S>, S extends Serializable> {
    @Autowired
    protected SessionFactory sessionFactory;

    public S addEntity(T entity) {
        return entity == null ? null : (S) this.sessionFactory
            .getCurrentSession().save(entity);
    }

    public int batchAddEntities(List<T> entities, int batchSize) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }

        Session session = this.sessionFactory.getCurrentSession();
        int i = 0;
        for (T e : entities) {
            session.save(e);

            if (++i % batchSize == 0) {
                session.flush();
                session.clear();
            }
        }

        return i;
    }

    public int batchUpdateEntities(List<T> entities, int batchSize) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }

        Session session = this.sessionFactory.getCurrentSession();
        int i = 0;
        for (T e : entities) {
            session.update(e);

            if (++i % batchSize == 0) {
                session.flush();
                session.clear();
            }
        }

        return i;
    }

    public T updateEntity(T entity) {
        return entity == null ? null : (T) this.sessionFactory
            .getCurrentSession().merge(entity);
    }

    public void deleteEntity(T entity) {
        if (entity != null) {
            this.sessionFactory.getCurrentSession().delete(entity);
        }
    }

    public T getEntityByID(S id) {
        return (T) this.sessionFactory.getCurrentSession().get(
            ClassUtils.getActualGenericType(this.getClass(), 0), id);
    }

    public List<T> getAllEntities() {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from "
                    + ClassUtils.getActualGenericType(this.getClass(), 0)
                        .getSimpleName()).list();
    }

    public int deleteAllEntities() {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "delete "
                    + ClassUtils.getActualGenericType(this.getClass(), 0)
                        .getSimpleName()).executeUpdate();
    }

    public List<T> getEntitiesByPage(int from, int n) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from "
                    + ClassUtils.getActualGenericType(this.getClass(), 0)
                        .getSimpleName()).setMaxResults(n).setFirstResult(from)
            .list();
    }

    public long getAllEntitiesCount() {
        return (Long) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "select count(*) from "
                    + ClassUtils.getActualGenericType(this.getClass(), 0)
                        .getSimpleName()).uniqueResult();
    }

    public T saveOrMerge(T entity) {
        if (entity != null) {
            if (entity.getId() == null) {
                this.addEntity(entity);
            } else {
                this.updateEntity(entity);
            }
        }

        return entity;
    }
}
