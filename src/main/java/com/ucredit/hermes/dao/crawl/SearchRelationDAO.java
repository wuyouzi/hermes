/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao.crawl;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.dao.BaseDAO;
import com.ucredit.hermes.model.crawl.company.SearchRelation;

/**
 * @author Administrator
 */
@Repository
public class SearchRelationDAO extends BaseDAO<SearchRelation, Integer> {

    public SearchRelation getSearchRelationByFkId(int fk_id) {
        return (SearchRelation) this.sessionFactory.getCurrentSession()
                .createQuery("from SearchRelation where fk_id = :fk_id")
                .setInteger("fk_id", fk_id).uniqueResult();
    }
}
