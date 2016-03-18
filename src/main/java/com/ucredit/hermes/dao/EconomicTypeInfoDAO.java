/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.EconomicTypeInfo;

/**
 * 经济类型:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class EconomicTypeInfoDAO extends BaseDAO<EconomicTypeInfo, Integer> {

    /**
     * 通过编号查询经济类型
     * 
     * @param code
     * @return EconomicTypeInfo
     */
    public EconomicTypeInfo getEconomicTypeByCode(String code) {
        return (EconomicTypeInfo) this.sessionFactory.getCurrentSession()
            .createQuery("from EconomicTypeInfo where code=:code ")
            .setString("code", code).uniqueResult();
    }
}
