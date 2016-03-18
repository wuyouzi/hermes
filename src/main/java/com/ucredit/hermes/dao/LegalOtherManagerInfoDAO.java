/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.enums.CompanyStatus;
import com.ucredit.hermes.model.pengyuan.LegalOtherManagerInfo;

/**
 * 法定代表人在其他机构任职信息:DAO
 * 
 * @author liuqianqian
 */
@Repository
public class LegalOtherManagerInfoDAO extends
        BaseDAO<LegalOtherManagerInfo, Integer> {
    /**
     * 通过主表id查询
     * 
     * @param parentId
     * @return List
     */
    public List<LegalOtherManagerInfo> getLegalOtherManagerById(int parentId) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from LegalOtherManagerInfo where companyInfos.id=:parentId ")
            .setInteger("parentId", parentId).list();
    }

    /**
     * 通过status='other'查询
     * 
     * @return List
     */
    public List<Integer> getLegalOtherManagerByStatus() {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "select distinct companyInfos.id from LegalOtherManagerInfo where status=:status")
            .setString("status", CompanyStatus.OTHER.name()).list();
    }

    /**
     * 修改status值
     * 
     * @param companyId
     * @param positionId
     * @param info
     */
    public void updateLegalOtherManagerByCId(int companyId, int positionId,
            LegalOtherManagerInfo info) {

        StringBuilder sb = new StringBuilder(
            " update LegalOtherManagerInfo set");
        Map<String, String> params = new HashMap<>();

        sb.append(" status=:status where ");
        params.put("status", info.getStatus().name());

        sb.append(" companyInfos.id=:companyId and corpName=:corpName and registerNo=:registerNo ");
        params.put("companyId", Integer.toString(companyId));
        params.put("corpName", info.getCorpName());
        params.put("registerNo", info.getRegisterNo());

        if (positionId != 0) {
            sb.append(" and position.id=:positionId ");
            params.put("positionId", Integer.toString(positionId));
        }
        Query q = this.sessionFactory.getCurrentSession().createQuery(
            sb.toString());
        for (Entry<String, String> e : params.entrySet()) {
            String key = e.getKey();
            q.setString(key, e.getValue());
        }
        q.executeUpdate();
    }
}
