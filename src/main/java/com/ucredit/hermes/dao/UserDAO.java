/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.User;

/**
 * @author caoming
 */
@Repository
public class UserDAO extends BaseDAO<User, Integer> {

    public User getUserByUserName(String username) {
        return (User) this.sessionFactory.getCurrentSession()
            .createQuery(" from User where username=:username")
            .setString("username", username).uniqueResult();
    }

    public Integer addUser(User user) {
        return this.addEntity(user);
    }

}
