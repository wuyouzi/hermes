/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao.tongdun;

import java.lang.reflect.Field;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.dao.BaseDAO;
import com.ucredit.hermes.model.tongdun.Condition;
import com.ucredit.hermes.model.tongdun.FraudApiResponse;
import com.ucredit.hermes.model.tongdun.TongdunFraudRecord;

/**
 * @author zhouwuyuan
 */
@Repository
public class FraudApiResponseDAO extends BaseDAO<FraudApiResponse, Integer> {
    /**
     * 查询该条件是否已查询过
     *
     * @param condition
     * @return
     */
    public TongdunFraudRecord searchFraudApiResponse(Condition condition) {
        String hql = "";
        try {
            hql = "from TongdunFraudRecord where "
                    + this.conditionParams(condition) + " enabled=true";
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        TongdunFraudRecord td = (TongdunFraudRecord) this.sessionFactory
            .getCurrentSession().createQuery(hql).setFirstResult(0)
            .setMaxResults(1).uniqueResult();
        if (td != null) {
            return td;
        }
        return null;
    }

    /**
     * 将condition的null值过滤
     *
     * @param condition
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public String conditionParams(Condition condition)
            throws IllegalArgumentException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        Field[] fields = condition.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            String name = f.getName();
            Object value = f.get(condition);
            if (value != null) {
                sb.append("condition." + name + "='" + value + "' and ");
            }
        }
        return sb.toString();
    }
}