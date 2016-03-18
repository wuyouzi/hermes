package com.ucredit.hermes.dao.yinlianzh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.dao.BaseDAO;
import com.ucredit.hermes.model.yinlianzh.YinlianzhTransactionsInfo;

/**
 * 获取商户账单信息
 *
 * @author zhouwuyuan
 */
@Repository
public class TransactionInfoDAO extends
        BaseDAO<YinlianzhTransactionsInfo, Integer> {
    /**
     * 更具uuid查询YinlianzhTransactionsInfo，和下面方法可合并
     *
     * @param uuid
     * @return
     */
    public YinlianzhTransactionsInfo getTransactionInfoByUuid(String uuid) {
        return (YinlianzhTransactionsInfo) this.sessionFactory
                .getCurrentSession()
                .createQuery("from YinlianzhTransactionsInfo where fileUuid=:uuid")
                .setParameter("uuid", uuid).setFirstResult(0).setMaxResults(1)
                .uniqueResult();
    }

    /**
     * 更具encryptMid查询YinlianzhTransactionsInfo
     *
     * @param encryptMid
     * @return
     */
    public YinlianzhTransactionsInfo getTransactionInfoByEncryptMid(
            String encryptMid) {
        return (YinlianzhTransactionsInfo) this.sessionFactory
                .getCurrentSession()
                .createQuery(
                "from YinlianzhTransactionsInfo where encryptMid=:encryptMid")
                        .setParameter("encryptMid", encryptMid).setFirstResult(0)
            .setMaxResults(1).uniqueResult();
    }

    public YinlianzhTransactionsInfo getTransactionInfoByshortMid(
            String shortMid, int baseInfoId) {
        String midLike = shortMid.replace('*', '%');
        return (YinlianzhTransactionsInfo) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from YinlianzhTransactionsInfo where (mid like:midLike or shortMid=:shortMid) and type=1 and yinlianzhMerchantBillBaseInfoId=:baseInfoId")
            .setParameter("midLike", midLike)
                        .setParameter("shortMid", shortMid)
                        .setParameter("baseInfoId", baseInfoId).setFirstResult(0)
                        .setMaxResults(1).uniqueResult();
    }

    public YinlianzhTransactionsInfo getTransactionInfoByUuid(int baseId,
            String fileUuid) {
        return (YinlianzhTransactionsInfo) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from YinlianzhTransactionsInfo where fileUuid=:fileUuid and yinlianzhMerchantBillBaseInfoId=:baseId")
            .setParameter("fileUuid", fileUuid).setParameter("baseId", baseId)
                        .setFirstResult(0).setMaxResults(1).uniqueResult();
    }

    public YinlianzhTransactionsInfo getTransactionInfoByFileUuid(
            String fileUuid) {
        return (YinlianzhTransactionsInfo) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from YinlianzhTransactionsInfo where fileUuid=:fileUuid")
            .setParameter("fileUuid", fileUuid).setFirstResult(0)
            .setMaxResults(1).uniqueResult();
    }

    public YinlianzhTransactionsInfo getTransactionInfoByshortEncryptMid(
            String encryptMid) {
        return (YinlianzhTransactionsInfo) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from YinlianzhTransactionsInfo where encryptMid=:encryptMid and type=0")
            .setParameter("encryptMid", encryptMid).setFirstResult(0)
                        .setMaxResults(1).uniqueResult();
    }

    public List<String> getUuidsByBaseInfoId(int baseInfoId) {
        String sql = "select fileUuid from yinlianzh_transactions_info where yinlianzh_merchant_bill_base_info_id=:baseInfoId and fileUuid<>'' and fileUuid is not null";
        List<String> aa = this.sessionFactory.getCurrentSession()
                .createSQLQuery(sql).setParameter("baseInfoId", baseInfoId).list();
        return aa;
    }
}
