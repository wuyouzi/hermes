/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.DegreeCollegeInfo;
import com.ucredit.hermes.model.pengyuan.EducationInfo;
import com.ucredit.hermes.utils.IdentificationCodeUtil;

/**
 * 学历信息DAO
 *
 * @author caoming
 */
@Repository
public class EducationInfoDAO extends BaseDAO<EducationInfo, Integer> {
    private static final Comparator<EducationInfo> COMPARATOR_EDUCATIONINFO = new Comparator<EducationInfo>() {
        @Override
        public int compare(EducationInfo o1, EducationInfo o2) {
            if (o1.getGraduateTime() > o2.getGraduateTime()) {
                return 1;
            } else if (o1.getGraduateTime() < o2.getGraduateTime()) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    public List<EducationInfo> getEducationInfo(String name, String documentNo,
        String unitName, String queryUserID, Date newReceiveTime,
            String levelNo, Integer graduateYear, String college)
            throws IllegalArgumentException {
        List<EducationInfo> infos = new LinkedList<>();
        String hql = "from EducationInfo where baseReportType.enabled = true and educationType='EDUCATION'";
        String edHql = "from DegreeCollegeInfo where educationInfos_id=:educationInfos_id";
        Map<String, Object> parameter = new HashMap<>();
        Map<String, Object> parameterEd = new HashMap<>();
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
        if (StringUtils.isNotEmpty(unitName)) {
            hql += " and unitName=:unitName ";
            parameter.put("unitName", unitName);
        }
        if (StringUtils.isNotEmpty(queryUserID)) {
            hql += " and queryUserID=:queryUserID ";
            parameter.put("queryUserID", queryUserID);
        }
        if (newReceiveTime != null) {
            hql += " and ReceiveTime=:newReceiveTime ";
            parameter.put("newReceiveTime", newReceiveTime);
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
        if (StringUtils.isNotEmpty(levelNo)) {
            edHql += " and levelNo =:levelNo ";
            parameterEd.put("levelNo", levelNo);
        }
        if (StringUtils.isNotEmpty(college)) {
            edHql += " and   college =:college ";
            parameterEd.put("college", college);
        }
        Session session = this.sessionFactory.getCurrentSession();
        Query countQuery = session.createQuery(hql);

        for (String key : parameter.keySet()) {
            countQuery.setParameter(key, parameter.get(key));
        }
        infos = countQuery.list();
        for (EducationInfo ed : infos) {
            List<DegreeCollegeInfo> degreeCollegeInfos = new LinkedList<>();

            if (StringUtils.isNotEmpty(levelNo)) {
                edHql += " and levelNo=:levelNo";
            }

            Query edQuery = session.createQuery(edHql);
            edQuery.setParameter("educationInfos_id", ed.getId());
            for (String key : parameterEd.keySet()) {
                edQuery.setParameter(key, parameterEd.get(key));
            }
            degreeCollegeInfos = edQuery.list();
            //Set<DegreeCollegeInfo> setDe = new HashSet(Arrays.asList(degreeCollegeInfos));
            Set<DegreeCollegeInfo> setDe = new HashSet<>();
            for (DegreeCollegeInfo d : degreeCollegeInfos) {
                setDe.add(d);
            }
            ed.setDegreeCollegeInfos(setDe);
        }
        Collections.sort(infos, EducationInfoDAO.COMPARATOR_EDUCATIONINFO);
        return infos;
    }

    public EducationInfo getErrorEducationInfo(String name, String documentNo) {
        return (EducationInfo) this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        "from EducationInfo where name=:name and documentNo=:documentNo and enabled=:enabled and resultType=:resultType  order by createTime desc")
                        .setParameter("name", name).setParameter("documentNo", documentNo)
                        .setBoolean("enabled", true).setParameter("resultType", "QUERY")
            .setFirstResult(0).setMaxResults(1).uniqueResult();
    }
}
