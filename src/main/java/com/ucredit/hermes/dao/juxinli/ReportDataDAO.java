package com.ucredit.hermes.dao.juxinli;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.dao.BaseDAO;
import com.ucredit.hermes.model.juxinli.ReportData;

@Repository
public class ReportDataDAO extends BaseDAO<ReportData, Integer> {
    public ReportData getReportData(int grantAuthorizationId, int reportNum) {
        String hql = "from ReportData where grant_authorization_id=:grantAuthorizationId and enabled=true and reportNum=:reportNum";
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setParameter("grantAuthorizationId", grantAuthorizationId)
            .setParameter("reportNum", reportNum);
        return (ReportData) query.setFetchSize(0).setMaxResults(1)
            .uniqueResult();

    }

}
