/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.CompanyShareholderInfo;

/**
 * 企业股东信息:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class CompanyShareholderInfoDAO extends
        BaseDAO<CompanyShareholderInfo, Integer> {

    /**
     * 通过主表id查询
     * 
     * @param parentId
     * @return List
     */
    public List<CompanyShareholderInfo> getCompanyShareholderById(int parentId) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from CompanyShareholderInfo where companyInfos.id=:parentId ")
            .setInteger("parentId", parentId).list();
    }
}
