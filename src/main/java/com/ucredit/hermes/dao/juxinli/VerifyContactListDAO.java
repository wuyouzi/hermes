package com.ucredit.hermes.dao.juxinli;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.dao.BaseDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.model.juxinli.VerifyContactList;

@Repository
public class VerifyContactListDAO extends BaseDAO<VerifyContactList, Integer> {
    public VerifyContactList getVerifyContactListInfo(int grantAuthorizationId) {
        VerifyContactList vc = (VerifyContactList) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from VerifyContactList where grant_authorization_id =:grant_authorization_id and errorCode is null ")
            .setParameter("grant_authorization_id", grantAuthorizationId)
            .setFirstResult(0).setMaxResults(1).uniqueResult();
        if (vc == null) {
            vc = (VerifyContactList) this.sessionFactory
                .getCurrentSession()
                .createQuery(
                    "from VerifyContactList where grant_authorization_id =:grant_authorization_id and  errorCode<>:errorCode")
                .setParameter("grant_authorization_id", grantAuthorizationId)
                .setParameter("errorCode", AsyncCode.FAILURE_CONNECTION_REFUSED)
                .setFirstResult(0).setMaxResults(1).uniqueResult();
        }
        return vc;

    }
}
