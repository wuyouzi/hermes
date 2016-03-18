/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service;

import javax.persistence.EntityExistsException;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.dao.UserDAO;
import com.ucredit.hermes.dao.UserGroupDAO;
import com.ucredit.hermes.model.User;
import com.ucredit.hermes.model.UserGroup;

/**
 * @author caoming
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class UserService {
    @Autowired
    private UserDAO dao;
    @Autowired
    private UserGroupDAO groupDao;

    /**
     * 添加用户
     *
     * @param user
     */
    public void addUser(User user) {
        if (user == null) {
            throw new NullPointerException("userInfo can not null");
        }
        String username = user.getUsername();
        if (username != null) {
            User oldUser = this.dao.getUserByUserName(username);
            if (oldUser != null) {
                throw new EntityExistsException("userInfo is existing");
            }
            UserGroup group = user.getGroup();
            if (group == null) {
                UserGroup userGroup = this.groupDao
                    .getUserGroupInfoByName(HermesConsts.DEFAULT_GROUP_NAME);
                user.setGroup(userGroup);
            }
            this.dao.addUser(user);
        }
    }
}
