/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.LogInfo;

/**
 * 日志表:DAO
 *
 * @author liuqianqian
 */
@Repository
public class LogInfoDAO extends BaseDAO<LogInfo, Integer> {
    /**
     * 通过tableName,recordId,dataChannel查询
     *
     * @param recordId
     * @return LogInfo
     */
    public LogInfo getLogInfoByRecordId(int recordId) {
        return (LogInfo) this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        "from LogInfo where tableName='company_infos' and data is not null and dataChannel=:dataChannel and recordId =:recordId order by createTime desc")
                        .setString("dataChannel", DataChannel.PENGYUAN.name())
                        .setInteger("recordId", recordId).setFirstResult(0)
                        .setMaxResults(1).uniqueResult();
    }

    /**
     * 查许指定时间段内查询鹏远系统的log日志
     *
     * @param startDate_
     * @param endDate_
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> getLogInfosSearchFromPentyuanBetweenDays(
            Date startDate_, Date endDate_) {
        String sql = "select data,createTime,id from log_infos where data is not null and dataChannel = 'PENGYUAN' and resultType = 'SUCCESS' and createTime >= :startDate and  createTime <=  :endDate order by id ";
        Session session = this.sessionFactory.getCurrentSession();
        SQLQuery query = session.createSQLQuery(sql);
        query.setDate("startDate", startDate_).setDate("endDate", endDate_);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<LogInfo> getLogInfosBetweenDaysForTypes(Date startDate_,
            Date endDate_, String tableName, DataChannel dataChannel) {
        String sql = "from LogInfo where dataChannel=:dataChannel and resultType = :resultType and tableName =:tableName and createTime between :startDate and :endDate";
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql);
        query.setParameter("dataChannel", dataChannel);
        query.setParameter("resultType", ResultType.SUCCESS);
        query.setParameter("tableName", tableName);
        query.setDate("startDate", startDate_).setDate("endDate", endDate_);
        return query.list();
    }
}
