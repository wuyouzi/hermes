package com.ucredit.hermes.dao.yinlianzh;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.dao.BaseDAO;
import com.ucredit.hermes.model.yinlianzh.YinlianzhTransactionDetail;

/**
 * 商户交易流水信息
 *
 * @author zhouwuyuan
 */
@Repository
public class TransactionDetailDAO extends
        BaseDAO<YinlianzhTransactionDetail, Integer> {
    /**
     * 根据code查询对对应的对象信息
     *
     * @param code
     * @param db
     * @param t
     * @return
     */
    public <T> T getObjectByCode(String code, String db, Class<T> t) {
        return (T) this.sessionFactory.getCurrentSession()
            .createQuery("from " + t.getName() + " where " + db + "=:code")
            .setParameter("code", code).setFirstResult(0).setMaxResults(1)
            .uniqueResult();
    }

    /**
     * 根据name查询对对应的对象信息
     *
     * @param name
     * @param db
     * @param t
     * @return
     */

    public <T> T getObjectByName(String name, String db, Class<T> t) {
        return (T) this.sessionFactory.getCurrentSession()
            .createQuery("from " + t.getName() + " where " + db + "=:name")
            .setParameter("name", name).setFirstResult(0).setMaxResults(1)
            .uniqueResult();
    }

    /**
     * 根据商户信息id删除对已的交易流水信息
     *
     * @param transInfoId
     */
    public void deleteTractionsDetail(List<Integer> transInfoId) {
        this.sessionFactory
            .getCurrentSession()
            .createSQLQuery(
                "delete from yinlianzh_transactions_detail where yinlianzh_transactions_info_id in(:transInfoId) and isDowLoad=false")
                .setParameterList("transInfoId", transInfoId).executeUpdate();
    }

    /**
     * 根据条件查询交易流水信息
     *
     * @param transInfoId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<YinlianzhTransactionDetail> getTransDetailByDate(
            List<Integer> transInfoId, Date beginDate, Date endDate) {
        Date newEndDate = endDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.DATE, 1);
        newEndDate = cal.getTime();
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from YinlianzhTransactionDetail where yinlianzhTransactionsInfoId in(:yinlianzhTransactionsInfoId) and transactionDate>=:beginDate and transactionDate<=:endDate and isDowLoad=1")
            .setParameterList("yinlianzhTransactionsInfoId", transInfoId)
            .setParameter("beginDate", beginDate)
            .setParameter("endDate", newEndDate).list();
    }

    /**
     * 根据基础信息uuid查询商户信息集合
     *
     * @param uuid
     * @return
     */
    public List<Integer> getTransIdByBaseUuid(String uuid) {
        return this.sessionFactory
            .getCurrentSession()
            .createSQLQuery(
                "select t.id from yinlianzh_transactions_info t inner join yinlianzh_merchant_bill_base_info b on t.yinlianzh_merchant_bill_base_info_id=b.id and (b.fileUuid=:uuid or fileUuids like :uuids)")
            .setParameter("uuid", uuid).setParameter("uuids", "%" + uuid + "%")
            .list();
    }

    /**
     * 批量插入交易流水信息
     *
     * @param dataInfo
     * @param transId
     * @param trueMid
     * @param isdownload
     */
    public void insertDetails(List<String> dataInfo, int transId,
            String trueMid, boolean isdownload) {
        StringBuilder sql = new StringBuilder(
            "insert into yinlianzh_transactions_detail(carDistinguishCode,transactionDate,AccountDate,yinlianzhTransactionType_id,merchantDistinguishCode,"
                    + "terminalDistinguishCode,cardLocation,income,outcome,yinlianzhCurrencyType_id,"
                    + "yinlianzhResponseCode_id,yinlianzhCardNature_id,yinlianzhCardGrade_id,yinlianzhCardProduct_id,"
                    + "shortMid,trueMid,yinlianzh_transactions_info_id,isDowLoad) values");
        for (String str : dataInfo) {
            String[] dataSingle = str.split(",");
            sql.append("('" + dataSingle[0]);
            if (!"AAAAAAAA".equals(dataSingle[1])) {
                sql.append("',STR_TO_DATE('" + dataSingle[1]
                        + "','%Y-%m-%d %H:%i:%s'),");
            } else {
                sql.append("',STR_TO_DATE('" + "1990-01-01 00:00:00"
                        + "','%Y-%m-%d %H:%i:%s'),");
            }
            if (!"AAAAAAAA".equals(dataSingle[2])) {
                sql.append("STR_TO_DATE('" + dataSingle[2] + "','%Y-%m-%d'),");
            } else {
                sql.append("STR_TO_DATE('" + "1990-01-01" + "','%Y-%m-%d'),");
            }
            sql.append("(select id from yinlianzh_transaction_type where ");
            sql.append(isdownload ? "name" : "code");
            sql.append("='" + dataSingle[5] + "' LIMIT 1),'" + dataSingle[8]
                + "','" + dataSingle[9] + "','" + dataSingle[10] + "',"
                + dataSingle[6] + "," + dataSingle[7]
                + ",(select id from yinlianzh_currency_type where ");
            sql.append(isdownload ? "currencyName" : "currencyCode");
            sql.append("='" + dataSingle[3]
                + "' LIMIT 1),(select id from yinlianzh_response_code where ");
            sql.append(isdownload ? "outDisplay" : "code");
            sql.append("='" + dataSingle[4] + "' LIMIT 1)"
                + ",(select id from yinlianzh_card_nature where ");
            sql.append(isdownload ? "name" : "code");
            sql.append("='" + dataSingle[11]
                + "' LIMIT 1),(select id from yinlianzh_card_grade where ");
            sql.append(isdownload ? "name" : "code");
            sql.append("='" + dataSingle[12]
                + "' LIMIT 1),(select id from yinlianzh_card_product where ");
            sql.append(isdownload ? "name" : "code");
            sql.append("='" + dataSingle[13] + "' LIMIT 1),'" + dataSingle[14]
                + "','" + trueMid + "'," + transId + "," + isdownload + "),");
        }
        this.sessionFactory
        .getCurrentSession()
        .createSQLQuery(
            sql.toString().substring(0, sql.toString().length() - 1))
            .executeUpdate();

    }

    public void updateMid(int baseInfoId) {
        String sql = "update yinlianzh_transactions_info y1,yinlianzh_transactions_info y2 set y1.mid = y2.mid where y2.encryptMid=y1.encryptMid and y2.type=0 and y1.yinlianzh_merchant_bill_base_info_id=:baseInfoId";
        this.sessionFactory.getCurrentSession().createSQLQuery(sql)
            .setParameter("baseInfoId", baseInfoId).executeUpdate();
        String sql1 = "update yinlianzh_transactions_detail y1,yinlianzh_transactions_info y2 set y1.trueMid=y2.mid where y1.trueMid is null and y1.yinlianzh_transactions_info_id=y2.id";
        this.sessionFactory.getCurrentSession().createSQLQuery(sql1)
            .executeUpdate();
    }
}
