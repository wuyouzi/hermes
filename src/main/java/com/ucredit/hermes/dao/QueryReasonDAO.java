/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.QueryReason;

/**
 * @author Administrator
 */
@Repository
public class QueryReasonDAO extends BaseDAO<QueryReason, Integer> {
    public QueryReason getQueryReasonByCode(String code) {
        return (QueryReason) this.sessionFactory.getCurrentSession()
            .createQuery("from QueryReason where code = :code")
            .setString("code", code).uniqueResult();
    }
}
