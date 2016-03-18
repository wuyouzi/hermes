/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.CompanyManagerInfo;

/**
 * 企业高管信息:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class CompanyManagerInfoDAO extends BaseDAO<CompanyManagerInfo, Integer> {
    /**
     * 通过主表id查询
     * 
     * @param parentId
     * @return List
     */
    public List<CompanyManagerInfo> getCompanyManagerById(int parentId) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from CompanyManagerInfo where companyInfos.id=:parentId ")
            .setInteger("parentId", parentId).list();
    }
}
