/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao.crawl;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.dao.BaseDAO;
import com.ucredit.hermes.model.crawl.SchoolInfo;
import com.ucredit.hermes.model.pengyuan.AreaCode;

/**
 * @author Administrator
 */
@Repository
public class SchoolInfoDAO extends BaseDAO<AreaCode, Integer> {

    public SchoolInfo getSchoolInfoByName(String name) {
        return (SchoolInfo) this.sessionFactory.getCurrentSession()
                .createQuery("from SchoolInfo where name = :name")
                .setString("name", name.trim()).uniqueResult();
    }
}
