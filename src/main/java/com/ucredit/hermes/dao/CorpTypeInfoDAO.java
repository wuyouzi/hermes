/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.CorpTypeInfo;

/**
 * 企业类型:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class CorpTypeInfoDAO extends BaseDAO<CorpTypeInfo, Integer> {

    /**
     * 通过编号查询企业类型
     * 
     * @param code
     * @return List
     */
    public CorpTypeInfo getCorpTypeByCode(String code) {
        return (CorpTypeInfo) this.sessionFactory.getCurrentSession()
            .createQuery("from CorpTypeInfo where code=:code ")
            .setString("code", code).uniqueResult();
    }
}
