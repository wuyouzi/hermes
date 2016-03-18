/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.enums.ReportType;
import com.ucredit.hermes.model.pengyuan.SubReportInfo;

/**
 * 子报告:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class SubReportInfoDAO extends BaseDAO<SubReportInfo, Integer> {

    /**
     * 通过编号查询子报告
     * 
     * @param code
     * @param type
     * @return SubReportInfo
     */
    public SubReportInfo getSubReportByCode(String code, ReportType type) {
        return (SubReportInfo) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from SubReportInfo where code=:code and reportType = :type ")
            .setString("code", code).setString("type", type.name())
            .uniqueResult();
    }
}
