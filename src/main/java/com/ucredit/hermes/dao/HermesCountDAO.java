/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.HermesCount;

/**
 * @author caoming
 */
@Repository
public class HermesCountDAO extends BaseDAO<HermesCount, Integer> {

    /**
     * 统计某一事件段内的查询次数
     *
     * @param startDate_
     * @param endDate_
     * @return List<Map<String, Object>>
     *         add by xubaoyong
     */
    public List<Map<String, Object>> statisticsBetweenDays(Date startDate_,
            Date endDate_) {

        String sql = "select count(`count`) searchcount ,type from hermes_counts   where createTime >=   ? AND  createTime <= ? and channel = 'PENGYUAN'  and count >=1 group by type  ";
        Session session = this.sessionFactory.getCurrentSession();
        SQLQuery query = session.createSQLQuery(sql);
        query.setDate(0, startDate_).setDate(1, endDate_);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

}
