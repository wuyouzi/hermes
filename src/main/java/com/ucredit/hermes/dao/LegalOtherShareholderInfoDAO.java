/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.enums.CompanyStatus;
import com.ucredit.hermes.model.pengyuan.LegalOtherShareholderInfo;

/**
 * 法定代表人股权投资信息:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class LegalOtherShareholderInfoDAO extends
        BaseDAO<LegalOtherShareholderInfo, Integer> {
    /**
     * 通过主表id查询
     * 
     * @param parentId
     * @return List
     */
    public List<LegalOtherShareholderInfo> getLegalOtherShareholderById(
            int parentId) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from LegalOtherShareholderInfo where companyInfos.id=:parentId ")
            .setInteger("parentId", parentId).list();
    }

    /**
     * 通过status='other'查询
     * 
     * @return List
     */
    public List<Integer> getLegalOtherShareholderByStatus() {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "select distinct companyInfos.id from LegalOtherShareholderInfo where status=:status")
            .setString("status", CompanyStatus.OTHER.name()).list();
    }

    /**
     * 修改status值
     * 
     * @param companyId
     * @param info
     */
    public void updateLegalOtherShareholderByCId(int companyId,
            LegalOtherShareholderInfo info) {
        this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "update LegalOtherShareholderInfo set status=:status where companyInfos.id=:companyId and corpName=:corpName and registerNo=:registerNo")
            .setInteger("companyId", companyId)
            .setString("status", info.getStatus().name())
            .setString("corpName", info.getCorpName())
            .setString("registerNo", info.getRegisterNo()).executeUpdate();
    }
}
