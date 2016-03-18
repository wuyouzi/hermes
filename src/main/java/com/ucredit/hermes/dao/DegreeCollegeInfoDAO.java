/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.DegreeCollegeInfo;
import com.ucredit.hermes.utils.IdentificationCodeUtil;


@Repository
public class DegreeCollegeInfoDAO extends BaseDAO<DegreeCollegeInfo, Integer> {

    public List<DegreeCollegeInfo> getEducationalInfo(String name, String documentNo,
            String levelNo, Integer graduateYear, String college)
            throws IllegalArgumentException {
        List<DegreeCollegeInfo> infos = new LinkedList<>();
        String hql = "from EducationalInfo where  baseReportType.enabled = true ";
        Map<String, Object> parameter = new HashMap<>();
        if (StringUtils.isNotEmpty(name)) {
            hql += " and name=:name ";
            parameter.put("name", name);
        }
        if (StringUtils.isNotEmpty(documentNo)) {
            int length = documentNo.length();
            String documentNo15 = documentNo;
            String documentNo18 = documentNo;

            if (length == 15) {
                documentNo15 = documentNo;
                int century = Integer.parseInt(documentNo.substring(6, 8));
                documentNo18 = IdentificationCodeUtil.from15to18(century,
                    documentNo15);
            } else if (length == 18) {
                documentNo18 = documentNo;
                documentNo15 = IdentificationCodeUtil.from18to15(documentNo);
            }

            hql += " and (documentNo =:documentNo15 or  documentNo =:documentNo18)";
            parameter.put("documentNo15", documentNo15);
            parameter.put("documentNo18", documentNo18);
        }
        if (graduateYear != null && graduateYear.intValue() != 0) {
            /**
             * 被查询者毕业年份，不能为空，2003年及以后的年份(包含2003年)可以不是准确年份，但是必须大于等于2003(
             * 因为2003年及以后取得的学历信息是通过姓名+证件号码进行查询的，年份只起到一个判断的作用。)
             */
            if (graduateYear.intValue() < 2003) {
                hql += " and graduateTime =:graduateYear ";
                parameter.put("graduateYear", graduateYear.intValue());
            }

            if (StringUtils.isNotEmpty(levelNo)
                && graduateYear.intValue() <= 2002) {
                /**
                 * 被查询者学历证编号，2002年及以前(包含2002年)取得的学历，学历证编号不能为空
                 */
                hql += " and levelNo =:levelNo ";
                parameter.put("levelNo", levelNo);
            }
        }
        if (StringUtils.isNotEmpty(college)) {
            hql += "and   college =:college ";
            parameter.put("college", college);
        }
        Session session = this.sessionFactory.getCurrentSession();
        Query countQuery = session.createQuery(hql);

        for (String key : parameter.keySet()) {
            countQuery.setParameter(key, parameter.get(key));
        }

        infos = countQuery.list();
        return infos;
    }
}
