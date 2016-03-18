/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.PositionInfo;

/**
 * 职务:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class PositionInfoDAO extends BaseDAO<PositionInfo, Integer> {

    /**
     * 通过编号查询子报告
     * 
     * @param code
     * @return PositionInfo
     */
    public PositionInfo getPositionByCode(String code) {
        return (PositionInfo) this.sessionFactory.getCurrentSession()
            .createQuery("from PositionInfo where code=:code ")
            .setString("code", code).uniqueResult();
    }
}
