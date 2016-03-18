/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.AreaCode;

/**
 * @author Administrator
 */
@Repository
public class AreaCodeDAO extends BaseDAO<AreaCode, Integer> {

    public AreaCode getAreaCodeByCode(String code) {
        return (AreaCode) this.sessionFactory.getCurrentSession()
            .createQuery("from AreaCode where code = :code")
            .setString("code", code).uniqueResult();
    }
}
