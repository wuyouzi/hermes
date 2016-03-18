/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.CompanyContactOperateInfo;

/**
 * 企业注册或经营地址及电话信息:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class CompanyContactOperateInfoDAO extends
        BaseDAO<CompanyContactOperateInfo, Integer> {

    /**
     * 通过主表id,类型查询
     * 
     * @param parentId
     * @param isRegister
     * @return List
     */
    public List<CompanyContactOperateInfo> getCompanyContactOperateById(
            int parentId, String isRegister) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from CompanyContactOperateInfo where companyInfos.id=:parentId and type=:isRegister ")
            .setInteger("parentId", parentId)
            .setString("isRegister", isRegister).list();
    }

}
