/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.CurrencyInfo;

/**
 * 币种:DAO
 *
 * @author liuqianqian
 */
@Repository
public class CurrencyInfoDAO extends BaseDAO<CurrencyInfo, Integer> {

    /**
     * 通过数字编号查询币种
     *
     * @param ncode
     * @return CurrencyInfo
     */
    public CurrencyInfo getCurrencyByNcode(int ncode) {
        return (CurrencyInfo) this.sessionFactory.getCurrentSession()
            .createQuery("from CurrencyInfo where ncode=:ncode ")
            .setInteger("ncode", ncode).uniqueResult();
    }

    /**
     * 通过币种编号查询币种
     *
     * @param code
     * @return CurrencyInfo
     */
    public CurrencyInfo getCurrencyByCode(String code) {
        return (CurrencyInfo) this.sessionFactory.getCurrentSession()
            .createQuery("from CurrencyInfo where code=:code ")
            .setString("code", code).uniqueResult();
    }

    /**
     * 通过名称查询币种
     *
     * @param name
     * @return CurrencyInfo
     */
    public CurrencyInfo getCurrencyByName(String name) {
        return (CurrencyInfo) this.sessionFactory.getCurrentSession()
            .createQuery("from CurrencyInfo where name=:name ")
            .setString("name", name).uniqueResult();
    }
}
