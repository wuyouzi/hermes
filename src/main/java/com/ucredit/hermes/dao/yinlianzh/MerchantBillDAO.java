package com.ucredit.hermes.dao.yinlianzh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.dao.BaseDAO;
import com.ucredit.hermes.model.yinlianzh.YinlianzhMerchantBillBaseInfo;
import com.ucredit.hermes.model.yinlianzh.YinlianzhTransactionDetail;
import com.ucredit.hermes.model.yinlianzh.YinlianzhTransactionsInfo;

/**
 * 获取商户账单信息
 *
 * @author zhouwuyuan
 */
@Repository
public class MerchantBillDAO extends
        BaseDAO<YinlianzhMerchantBillBaseInfo, Integer> {
    public YinlianzhMerchantBillBaseInfo getMerchantBillBaseInfo(Integer type,
            String category, String mid, String mName, String posId,
            String businessCode, String name, String mobile, String cid,
            Date beginDate, Date endDate) {
        StringBuilder hql = new StringBuilder(
                "select distinct b from YinlianzhMerchantBillBaseInfo b,YinlianzhTransactionsInfo t,YinlianzhTransactionDetail d where b.enabled=:enabled");
        //商户 基本信息
        StringBuilder hqlTransInfo = new StringBuilder(
                "from YinlianzhTransactionsInfo t where t.yinlianzhMerchantBillBaseInfoId=:yinlianzhMerchantBillBaseInfoId");
        Map<String, Object> parameter = new HashMap<>();
        Map<String, Object[]> parameterList = new HashMap<>();
        if (type != null) {
            hql.append(" and b.type=:type ");
            parameter.put("type", type);
        }
        if (StringUtils.isNotEmpty(category)) {
            hql.append(" and category=:category ");
            parameter.put("category", category);
        } else {
            hql.append(" and t.mid in (:mid)");
            hqlTransInfo.append(" and t.mid in (:mid)");
            parameterList.put("mid", mid.split(","));
            hql.append(" and t.name in(:mName)");
            hqlTransInfo.append(" and t.name in(:mName)");
            parameterList.put("mName", mName.split(","));
        }
        if (StringUtils.isNotEmpty(mid) && "1".equals(category)) {
            hql.append(" and t.mid in (:mid)");
            hqlTransInfo.append(" and t.mid in (:mid)");
            parameterList.put("mid", mid.split(","));
        }
        //商户名称
        if (StringUtils.isNotEmpty(mName) && "2".equals(category)) {
            hql.append(" and t.name in(:mName)");
            hqlTransInfo.append(" and t.name in(:mName)");
            parameterList.put("mName", mName.split(","));
        }

        if (StringUtils.isNotEmpty(name) && category.isEmpty()) {
            hql.append(" and t.name=:name ");
            parameter.put("name", name);
        }
        if (StringUtils.isNotEmpty(posId)) {
            hql.append(" and t.posId in (:posId) ");
            hqlTransInfo.append(" and t.posId in (:posId) ");
            parameterList.put("posId", posId.split(","));
        }
        if (StringUtils.isNotEmpty(businessCode)) {
            hql.append(" and businessCode=:businessCode ");
            parameter.put("businessCode", businessCode);
        }
        if (StringUtils.isNotEmpty(mobile)) {
            hql.append(" and mobile=:mobile ");
            parameter.put("mobile", mobile);
        }
        if (StringUtils.isNotEmpty(cid)) {
            hql.append(" and cid=:cid ");
            parameter.put("cid", cid);
        }
        if (beginDate != null) {
            hql.append(" and d.transactionDate>=:beginDate ");
            parameter.put("beginDate", beginDate);
        }
        Date newEndDate = endDate;
        if (endDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.add(Calendar.DATE, 1);
            newEndDate = cal.getTime();
            hql.append(" and d.transactionDate<=:endDate ");
            parameter.put("endDate", newEndDate);
        }
        hql.append(" and t.yinlianzhMerchantBillBaseInfoId=b.id and t.id=d.yinlianzhTransactionsInfoId");
        Session session = this.sessionFactory.getCurrentSession();
        Query queryBase = session.createQuery(hql.toString());
        Query queryTrans = session.createQuery(hqlTransInfo.toString());
        for (String key : parameter.keySet()) {
            queryBase.setParameter(key, parameter.get(key));
        }
        for (String key : parameterList.keySet()) {
            queryBase.setParameterList(key, parameterList.get(key));
            queryTrans.setParameterList(key, parameterList.get(key));
        }
        YinlianzhMerchantBillBaseInfo info = (YinlianzhMerchantBillBaseInfo) queryBase
                .setBoolean("enabled", true).setFirstResult(0).setMaxResults(1)
                .uniqueResult();
        if (info != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            info.setCreateTimeString(sdf.format(info.getCreateTime()));
            List<YinlianzhTransactionsInfo> transInfos = new ArrayList<>();
            Set<YinlianzhTransactionsInfo> setTrans = new HashSet<>();
            if (info != null) {
                transInfos = queryTrans.setParameter(
                    "yinlianzhMerchantBillBaseInfoId", info.getId()).list();
                if (!transInfos.isEmpty()) {
                    for (YinlianzhTransactionsInfo t : transInfos) {
                        t.setBackTimeString(t.getBackTimeString());
                        //商户信息下的交易流水信息，界面最多只返回50条
                        StringBuilder hqlDetail = new StringBuilder(
                                " from YinlianzhTransactionDetail d where  d.yinlianzhTransactionsInfoId=:yinlianzhTransactionsInfoId and d.transactionDate>:beginDate  and d.transactionDate<=:endDate and isDowLoad=1");
                        Query queryTail = session.createQuery(hqlDetail
                            .toString());
                        queryTail
                            .setParameter("beginDate", beginDate)
                            .setParameter("endDate", newEndDate)
                            .setParameter("yinlianzhTransactionsInfoId",
                                t.getId()).setFirstResult(0).setMaxResults(50);
                        List<YinlianzhTransactionDetail> details = queryTail
                                .list();
                        Set<YinlianzhTransactionDetail> setDe = new HashSet<>();
                        for (YinlianzhTransactionDetail d : details) {
                            d.setTransactionDateString(sdf.format(d
                                .getTransactionDate()));
                            setDe.add(d);
                        }
                        t.setTransactionDetails(setDe);
                        setTrans.add(t);
                    }
                }
            }
            info.setTransactionsInfos(setTrans);
            return info;
        }
        return null;
    }

    /**
     * 根据商户信息表的下载uuid返回主表信息
     *
     * @param uuid
     * @return
     */
    public YinlianzhMerchantBillBaseInfo getMerchantBillBaseInfo(String uuid) {
        String hql = "select b from YinlianzhMerchantBillBaseInfo b,YinlianzhTransactionsInfo t where t.yinlianzhMerchantBillBaseInfoId=b.id and t.fileUuid=:uuid";
        return (YinlianzhMerchantBillBaseInfo) this.sessionFactory
            .getCurrentSession().createQuery(hql).setParameter("uuid", uuid)
            .setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    /**
     * 根据总下载uuid返回主表信息
     *
     * @param uuid
     * @return
     */
    public YinlianzhMerchantBillBaseInfo getMerchantBillBaseInfoFromBaseUuid(
            String uuid) {
        String hql = "from YinlianzhMerchantBillBaseInfo b where b.fileUuid=:uuid";
        return (YinlianzhMerchantBillBaseInfo) this.sessionFactory
            .getCurrentSession().createQuery(hql).setParameter("uuid", uuid)
            .setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    public YinlianzhMerchantBillBaseInfo getMerchantBillBaseInfoById(int id) {
        String hql = "rom YinlianzhMerchantBillBaseInfo  where id=:id";
        return (YinlianzhMerchantBillBaseInfo) this.sessionFactory
            .getCurrentSession().createQuery(hql).setParameter("id", id)
            .setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    public YinlianzhMerchantBillBaseInfo getMerchantBillBaseInfo(
            YinlianzhMerchantBillBaseInfo BaseInfo, String mids)
            throws ParseException {
        String[] midsStr = mids.split(",");
        String sqlCount = "select mid,min(firstTransDate),max(lastTransDate),max(backTime),sum(receipt),sum(transCount) "
            + " from yinlianzh_transactions_info where type=1 and mid in (:mids) and yinlianzh_merchant_bill_base_info_id=:baseId GROUP BY mid";
        List<String[]> transInfos = this.sessionFactory.getCurrentSession()
                .createSQLQuery(sqlCount).setParameterList("mids", midsStr)
                .setParameter("baseId", BaseInfo.getId()).list();
        Set<YinlianzhTransactionsInfo> transactsonsInfos = new HashSet<>();
        for (String mid : midsStr) {
            String sqlTransInfo = "from YinlianzhTransactionsInfo t where t.yinlianzhMerchantBillBaseInfoId=:baseId and t.type=1 and mid=:mid order by backTime desc";
            YinlianzhTransactionsInfo baseIfo = (YinlianzhTransactionsInfo) this.sessionFactory
                    .getCurrentSession().createQuery(sqlTransInfo)
                    .setParameter("baseId", BaseInfo.getId())
                .setParameter("mid", mid).setFirstResult(0).setMaxResults(1)
                .uniqueResult();
            YinlianzhTransactionsInfo baseInfoNew = baseIfo;
            for (Object[] str : transInfos) {
                if (str[0].equals(baseIfo.getMid() + "")) {
                    if (str[1] != null && str[2] != null) {
                        baseInfoNew.setFirstTransDate(DateUtils.parseDate(
                            str[1] + "", "yyyy-MM-dd"));
                        baseInfoNew.setLastTransDate(DateUtils.parseDate(str[2]
                            + "", "yyyy-MM-dd"));
                    }
                    baseInfoNew.setBackTime((Date) str[3]);
                    baseInfoNew.setReceipt(Long.parseLong(str[4] + ""));
                    baseInfoNew.setTransCount(Integer.parseInt(str[5] + ""));
                    transactsonsInfos.add(baseInfoNew);
                }
            }
        }
        BaseInfo.setTransactionsInfos(transactsonsInfos);
        return BaseInfo;

    }
}
