/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.TradeInfo;

/**
 * 行业类型:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class TradeInfoDAO extends BaseDAO<TradeInfo, Integer> {

    /**
     * 通过编号查询行业类型
     * 
     * @param code
     * @return TradeInfo
     */
    public TradeInfo getTradeByCode(String code) {
        return (TradeInfo) this.sessionFactory.getCurrentSession()
            .createQuery("from TradeInfo where code=:code ")
            .setString("code", code).uniqueResult();
    }
}
