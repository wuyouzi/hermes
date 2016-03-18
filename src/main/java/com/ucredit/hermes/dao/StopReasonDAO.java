/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.StopReason;

/**
 * 中止原因
 * 
 * @author caoming
 */
@Repository
public class StopReasonDAO extends BaseDAO<StopReason, Integer> {

    public StopReason getStopReasonByCode(String code) {
        return (StopReason) this.sessionFactory.getCurrentSession()
            .createQuery("from StopReason where code = :code")
            .setString("code", code).uniqueResult();
    }
}
