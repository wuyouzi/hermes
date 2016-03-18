/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.InformationSourceUnitType;

/**
 * 信息来源单位所属类型DAO
 * 
 * @author caoming
 */
@Repository
public class InformationSourceUnitTypeDAO extends
        BaseDAO<InformationSourceUnitType, Integer> {

    /**
     * 根据code获取数据
     * 
     * @param code
     * @return
     */
    public InformationSourceUnitType getInformationSourceUnitTypeByCode(
            String code) {
        return (InformationSourceUnitType) this.sessionFactory
            .getCurrentSession()
            .createQuery(" from InformationSourceUnitType where code = :code")
            .setString("code", code).uniqueResult();
    }
}
