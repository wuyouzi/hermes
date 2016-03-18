/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.PoliceResidenceInfo;

/**
 * 专线个人户籍DAO
 * 
 * @author caoming
 */
@Repository
public class PoliceResidenceInfoDAO extends
        BaseDAO<PoliceResidenceInfo, Integer> {

    /**
     * 根据name和documentNo查询个人户籍信息
     * 
     * @param name
     * @param documentNo
     * @return
     */
    public PoliceResidenceInfo getPoliceResidenceInfo(String name,
            String documentNo) {
        return (PoliceResidenceInfo) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                " from PoliceResidenceInfo where name =:name "
                    + "and documentNo=:documentNo and baseReportType.enabled = true "
                    + "order by baseReportType.createTime desc")
            .setString("name", name).setString("documentNo", documentNo)
            .setFirstResult(0).setMaxResults(1).uniqueResult();
    }

}
