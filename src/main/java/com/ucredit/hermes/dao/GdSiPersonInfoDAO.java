/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.GdSiPersonInfo;

/**
 * 获取广东个人社保信息DAO
 * 
 * @author caoming
 */
@Repository
public class GdSiPersonInfoDAO extends BaseDAO<GdSiPersonInfo, Integer> {

    public List<GdSiPersonInfo> getGdSiPersonInfo(String name, String documentNo) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                " from GdSiPersonInfo where name =:name "
                    + "and documentNo=:documentNo and baseReportType.enabled = true "
                    + "order by baseReportType.createTime desc")
            .setString("name", name).setString("documentNo", documentNo).list();
    }

}
