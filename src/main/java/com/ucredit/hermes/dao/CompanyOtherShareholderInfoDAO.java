/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.enums.CompanyStatus;
import com.ucredit.hermes.model.pengyuan.CompanyOtherShareholderInfo;

/**
 * 企业对外股权投资信息:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class CompanyOtherShareholderInfoDAO extends
        BaseDAO<CompanyOtherShareholderInfo, Integer> {
    /**
     * 通过主表id查询
     * 
     * @param parentId
     * @return List
     */
    public List<CompanyOtherShareholderInfo> getCompanyOtherShareholderById(
            int parentId) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from CompanyOtherShareholderInfo where companyInfos.id=:parentId ")
            .setInteger("parentId", parentId).list();
    }

    /**
     * 通过status='other'查询
     * 
     * @return List
     */
    public List<Integer> getCompanyOtherShareholderByStatus() {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "select distinct companyInfos.id from CompanyOtherShareholderInfo where status=:status")
            .setString("status", CompanyStatus.OTHER.name()).list();
    }

    /**
     * 修改status值
     * 
     * @param companyId
     * @param cos
     */
    public void updateCompanyOtherShareholderByCId(int companyId,
            CompanyOtherShareholderInfo cos) {
        this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "update CompanyOtherShareholderInfo set status=:status where companyInfos.id=:companyId and corpName=:corpName and registerNo=:registerNo")
            .setInteger("companyId", companyId)
            .setString("status", cos.getStatus().name())
            .setString("corpName", cos.getCorpName())
            .setString("registerNo", cos.getRegisterNo()).executeUpdate();
    }
}
