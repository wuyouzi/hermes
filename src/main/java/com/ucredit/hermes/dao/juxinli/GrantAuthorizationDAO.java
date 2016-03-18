package com.ucredit.hermes.dao.juxinli;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.dao.BaseDAO;
import com.ucredit.hermes.model.juxinli.GrantAuthorizationInfo;

@Repository
public class GrantAuthorizationDAO extends
        BaseDAO<GrantAuthorizationInfo, Integer> {
    /**
     * 获取授权用户信息
     *
     * @param name
     * @param idcard
     * @param phone
     * @return
     */
    public List<GrantAuthorizationInfo> GrantAuthorizationInfo(String name,
            String idcard, String phone) {
        String hql = "from GrantAuthorizationInfo where name=:name and idcard=:idcard and phone=:phone and enabled=true";
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setParameter("name", name).setParameter("idcard", idcard)
        .setParameter("phone", phone);
        return query.list();
    }

    public void updateBeforeGrant(String name, String idcard, String phone) {
        String sql = "update grant_authorization_infos set enabled=0,updateTime=SYSDATE()  where name=:name and idcard=:idcard and phone=:phone and enabled=1";
        Session session = this.sessionFactory.getCurrentSession();
        session.createSQLQuery(sql).setParameter("name", name)
        .setParameter("idcard", idcard).setParameter("phone", phone)
        .executeUpdate();
    }

}
