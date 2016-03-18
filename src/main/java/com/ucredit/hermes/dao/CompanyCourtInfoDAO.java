/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.CompanyCourtInfo;

/**
 * 企业法院被执行信息:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class CompanyCourtInfoDAO extends BaseDAO<CompanyCourtInfo, Integer> {
    /**
     * 通过主表id查询
     * 
     * @param parentId
     * @return List
     */
    public List<CompanyCourtInfo> getCompanyCourtById(int parentId) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from CompanyCourtInfo where companyInfos.id=:parentId ")
            .setInteger("parentId", parentId).list();
    }
}
