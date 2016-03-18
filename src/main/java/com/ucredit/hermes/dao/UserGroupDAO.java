/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.UserGroup;

/**
 * @author caoming
 */
@Repository
public class UserGroupDAO extends BaseDAO<UserGroup, Integer> {

    public UserGroup getUserGroupInfoByName(String name) {
        return (UserGroup) this.sessionFactory.getCurrentSession()
            .createQuery(" from UserGroup where name=:name")
            .setString("name", name).uniqueResult();
    }

}
