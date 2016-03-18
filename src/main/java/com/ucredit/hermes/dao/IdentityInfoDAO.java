/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.guozhengtong.IdentityInfo;

/**
 * 身份信息表:DAO
 *
 * @author liuqianqian
 */
@Repository
public class IdentityInfoDAO extends BaseDAO<IdentityInfo, Integer> {

    /**
     * 根据姓名和身份证号查询数据
     *
     * @param name
     * @param number
     * @return
     */
    public IdentityInfo getIdentityInfosByName(String name, String number) {
        return (IdentityInfo) this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        " from IdentityInfo where name=:name and identitycard=:number and enabled = true")
                        .setString("name", name).setString("number", number)
            .setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    /**
     * 根据身份证查询数据
     *
     * @param number
     * @return
     */
    public IdentityInfo getIdentityInfosByCard(String number) {
        return (IdentityInfo) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                " from IdentityInfo where identitycard=:number and enabled = true and resultType=:resultType")
                        .setString("number", number)
                        .setParameter("resultType", ResultType.SUCCESS).setFirstResult(0)
                        .setMaxResults(1).uniqueResult();
    }
}
