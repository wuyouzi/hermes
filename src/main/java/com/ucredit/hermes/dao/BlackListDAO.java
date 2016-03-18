/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.blacklist.BlackList;
import com.ucredit.hermes.model.blacklist.BlackListValueType;

/**
 * 黑名单DAO
 *
 * @author Liuhao
 */
@Repository
public class BlackListDAO extends BaseDAO<BlackList, Integer> {

    /**
     * 根据黑名单类型和值查询数据库
     *
     * @param blackListValueType
     * @param value
     * @return
     */
    public BlackList getBlackListByTypeAndValue(
            BlackListValueType blackListValueType, String value) {
        return (BlackList) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                " from BlackList where type=:blackListValueType and value=:value and enabled = true")
            .setParameter("blackListValueType", blackListValueType)
            .setParameter("value", value).setMaxResults(1).uniqueResult();
    }

    /**
     * 根据参数模糊查询黑名单
     *
     * @param blackListValueType
     * @param value
     * @return
     */
    public List<BlackList> getBlackList4Others(
            BlackListValueType blackListValueType, String value) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                " from BlackList where type=:blackListValueType and value like :value and enabled = true")
            .setParameter("blackListValueType", blackListValueType)
            .setParameter("value", value).list();
    }
}
