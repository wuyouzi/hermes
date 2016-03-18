/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.OrganTypeInfo;

/**
 * 机构类型:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class OrganTypeInfoDAO extends BaseDAO<OrganTypeInfo, Integer> {

    /**
     * 通过编号查询机构类型
     * 
     * @param code
     * @return OrganTypeInfo
     */
    public OrganTypeInfo getOrganTypeByCode(String code) {
        return (OrganTypeInfo) this.sessionFactory.getCurrentSession()
            .createQuery("from OrganTypeInfo where code=:code ")
            .setString("code", code).uniqueResult();
    }
}
