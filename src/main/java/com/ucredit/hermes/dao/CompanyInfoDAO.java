/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.pengyuan.CompanyInfo;

/**
 * 企业相关信息:DAO
 *
 * @author liuqianqian
 */
@Repository
public class CompanyInfoDAO extends BaseDAO<CompanyInfo, Integer> {

    /**
     * 获取企业相关信息
     *
     * @param companyName
     * @param orgCode
     * @param registerNo
     * @return CompanyInfo
     */
    public CompanyInfo getCompanyInfosFilter(String companyName,
            String orgCode, String registerNo) {
        StringBuilder sb = new StringBuilder(" from CompanyInfo c where ");
        Map<String, String> params = new HashMap<>();

        if (StringUtils.isNotBlank(companyName)) {
            sb.append(" (c.companyName=:companyName_ or c.pengYuanCompanyName =:companyName_ ) and ");
            params.put("companyName_", companyName);
        }
        if (StringUtils.isNotBlank(orgCode)) {
            sb.append(" c.orgCode=:orgCode_ and ");
            params.put("orgCode_", orgCode);
        }
        if (StringUtils.isNotBlank(registerNo)) {
            sb.append(" c.registerNo=:registerNo_ and ");
            params.put("registerNo_", registerNo);
        }
        sb.append(" c.enabled = :enabled order by c.createTime desc");
        Query q = this.sessionFactory.getCurrentSession().createQuery(
            sb.toString());
        for (Entry<String, String> e : params.entrySet()) {
            String key = e.getKey();
            q.setString(key, e.getValue());
        }
        CompanyInfo ret = (CompanyInfo) q.setBoolean("enabled", true)
                .setFirstResult(0).setMaxResults(1).uniqueResult();

        return ret;
    }

    /**
     * 根据企业名称模糊查询
     *
     * @param companyName
     * @return List
     */
    public List<Map<String, Object>> getCompanyByNames(String companyName) {
        return this.sessionFactory
                .getCurrentSession()
                .createQuery(
                    "select new map(id as id,companyName as name) from "
                            + "CompanyInfo where (companyName like :companyName or pengYuanCompanyName like :companyName)"
                            + " and resultType=:type and enabled = true ")
                            .setString("companyName", companyName + "%")
                            .setParameter("type", ResultType.SUCCESS).setMaxResults(5).list();

    }

    /**
     * 查询一定时间内的记录数
     *
     * @param companyName
     * @param startDate
     * @param endDate
     * @return Long
     */
    public Long getCompanyByName2Count(String companyName, Date startDate,
            Date endDate) {
        return (Long) this.sessionFactory
                .getCurrentSession()
                .createQuery(
                    "select count(*) from CompanyInfo where createTime >=:startDate "
                            + " and createTime < :endDate and md5 is not null"
                            + " and companyName = :companyName")
                            .setString("companyName", companyName)
                            .setDate("startDate", startDate).setDate("endDate", endDate)
                            .uniqueResult();
    }

    /**
     * 根据时间和企业名称，查询一定时间内不止一条的条数
     *
     * @param companyName
     * @param startDate
     * @param endDate
     * @param number
     * @return BigInteger
     */
    public BigInteger getCompanyByNameOrDate(String companyName,
            Date startDate, Date endDate, Integer number) {
        return (BigInteger) this.sessionFactory
                .getCurrentSession()
                .createSQLQuery(
                    "select count(*) from  company_infos "
                            + " where createTime >= date_add((select min(createTime) from company_infos "
                            + " where companyName=:companyName and createTime >= :startDate "
                            + " and createTime < :endDate and md5 is not null),interval :number day)"
                            + " and companyName=:companyName and createTime >= :startDate and createTime < :endDate and md5 is not null")
                            .setString("companyName", companyName)
                            .setDate("startDate", startDate).setDate("endDate", endDate)
                            .setInteger("number", number).uniqueResult();
    }

    /**
     * 统计2014年12月及以后月份的查询次数
     * 12月以后使用queryTime来标识发请求时间
     * 查询每个月查询到的信息，由于有打包计算的的情况所以修改为如下全部查询的方法
     * 将来要优化成分页查询的方法
     *
     * @param begin
     * @param end
     * @return List<Map<String, Object>>
     *         add by xubaoyong
     */
    public List<Map<String, Object>> getReportAfter201412(Date begin, Date end) {
        String sql = "select PengYuanReportRecord.subReportCode, PengYuanReportRecord.pengYuanRePortId, "
                + "PengYuanReportRecord.mainId,PengYuanReportRecord.count "
                + " from company_infos,PengYuanReportRecord"
                + " where company_infos.refId = PengYuanReportRecord.mainId"
                + " and PengYuanReportRecord.queryType = 'CHECK_COMPANY'"
                + " and PengYuanReportRecord.dataChannel='PENGYUAN'"
                + " and company_infos.queryTime >= ? and company_infos.queryTime <= ?"
                + " and company_infos.resultType= 'SUCCESS'";

        //sql.append(" group by PengYuanReportRecord.mainId,PengYuanReportRecord.subReportCode");
        return this.sessionFactory.getCurrentSession().createSQLQuery(sql)
                .setDate(0, begin).setDate(1, end)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 查询指定日期间新增的企业数
     *
     * @param begin
     * @param end
     * @return int
     */
    public BigInteger getCompanyNewAddsBetweenDays(Date begin, Date end) {
        return (BigInteger) this.sessionFactory
                .getCurrentSession()
                .createSQLQuery(
                    "select count(id) from company_infos where createTime between ? "
                            + "and ?  and datachannel = 'PENGYUAN' and resultType = 'SUCCESS' ")
                            .setDate(0, begin).setDate(1, end).uniqueResult();
    }

    /**
     * 查询指定时间段内的且查询类型为指定类型的查询refId
     *
     * @param begin
     * @param end
     * @param resultType
     * @return List<Map<String, String>>
     */
    public List<Map<String, String>> getSearchDetailBetweenDays(Date begin,
            Date end, ResultType resultType) {
        return this.sessionFactory
                .getCurrentSession()
                .createSQLQuery(
                    "select company_infos.refId from company_infos where queryTime "
                            + "between ? and ? and resultType = ? order by id  ")
                            .setDate(0, begin).setDate(1, end).setString(2, resultType.name())
                            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public CompanyInfo getEntityByDbId(int dbID) {

        return (CompanyInfo) this.sessionFactory.getCurrentSession().get(
            CompanyInfo.class, dbID);
    }

    public CompanyInfo getEntityByDbName(String companyname) {

        return (CompanyInfo) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from CompanyInfo where companyName=:companyname and enabled=:enabled and resultType=:resultType  order by createTime desc")
            .setParameter("companyname", companyname)
            .setBoolean("enabled", true)
            .setParameter("resultType", ResultType.QUERY).setFirstResult(0)
            .setMaxResults(1).uniqueResult();
    }

}
