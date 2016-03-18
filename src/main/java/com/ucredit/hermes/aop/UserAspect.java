/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.jasypt.util.password.rfc2307.RFC2307MD5PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ucredit.hermes.model.User;

/**
 * @author caoming
 */
@Component
@Aspect
public class UserAspect {
    @Autowired
    private RFC2307MD5PasswordEncryptor encryptor;
    private static final String MD5_PREFIX = "{MD5}";

    /**
     * @param user
     */
    @Before(
            value = "execution(public * *..UserDAO.add*(*..User)) && args(user)")
    public void preProcessUsers(User user) {
        this.prepareUser(user);
    }

    private void prepareUser(User user) {
        if (user == null) {
            return;
        }

        String passwordString = user.getPassword();
        // delete时有可能出现此情况
        if (passwordString != null
            && !passwordString.startsWith(UserAspect.MD5_PREFIX)) {
            user.setPassword(this.encryptor.encryptPassword(passwordString));
        }
    }

//    public static void main(String[] args) {
//        RFC2307MD5PasswordEncryptor encryptor2 = new RFC2307MD5PasswordEncryptor();
//        String string = encryptor2.encryptPassword("123");
//        System.out.println(string);
//    }
}
