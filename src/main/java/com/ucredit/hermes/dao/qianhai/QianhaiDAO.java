package com.ucredit.hermes.dao.qianhai;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.dao.BaseDAO;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.qianhai.QianhaiSearchRecord;

@Repository
public class QianhaiDAO extends BaseDAO<QianhaiSearchRecord, Integer> {
    public QianhaiSearchRecord getQianhaiSearchRecord(String name,
            String idType, String idNo, String apply_id) {
        return (QianhaiSearchRecord) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from QianhaiSearchRecord where name = :name and idType=:idType "
                            + "and idNo=:idNo and apply_id=:apply_id and resultType=:type and enabled=true")
            .setString("name", name).setString("idType", idType)
                            .setString("idNo", idNo).setString("apply_id", apply_id)
            .setParameter("type", ResultType.QUERY).uniqueResult();
    }

    public QianhaiSearchRecord getExistRecord(String name, String idType,
            String idNo, String apply_id) {
        return (QianhaiSearchRecord) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from QianhaiSearchRecord where name = :name and idType=:idType "
                            + "and idNo=:idNo and apply_id=:apply_id and enabled=true")
            .setString("name", name).setString("idType", idType)
                            .setString("idNo", idNo).setString("apply_id", apply_id)
            .setFirstResult(0).setMaxResults(1).uniqueResult();
    }

}
