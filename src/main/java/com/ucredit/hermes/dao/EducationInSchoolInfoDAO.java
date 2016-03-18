/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.DegreeCollegeInfo;
import com.ucredit.hermes.model.pengyuan.EducationInfo;

/**
 * 学籍信息DAO
 *
 * @author caoming
 */
@Repository
public class EducationInSchoolInfoDAO extends BaseDAO<EducationInfo, Integer> {

    /**
     * 根据姓名和证件号查询学籍信息
     *
     * @param name
     * @param documentNo
     * @return
     */
    public EducationInfo getEducationInSchoolInfo(String name, String documentNo) {
        EducationInfo educationSchoolInfo = new EducationInfo();
        educationSchoolInfo = (EducationInfo) this.sessionFactory
            .getCurrentSession()
            .createQuery(
                " from EducationInfo where name =:name "
                    + "and documentNo=:documentNo and baseReportType.enabled = true and educationType='EDUCATION_INSCHOOL'"
                    + "order by baseReportType.createTime desc")
            .setString("name", name).setString("documentNo", documentNo)
            .setFirstResult(0).setMaxResults(1).uniqueResult();
        if (educationSchoolInfo != null) {
            String edHql = "from DegreeCollegeInfo where educationInfos_id=:educationInfos_id";
            Query edQuery = this.sessionFactory.getCurrentSession()
                .createQuery(edHql);
            edQuery.setParameter("educationInfos_id",
                educationSchoolInfo.getId());
            List<DegreeCollegeInfo> degreeCollegeInfos = edQuery.list();
            Set<DegreeCollegeInfo> setDe = new HashSet<>();
            for (DegreeCollegeInfo d : degreeCollegeInfos) {
                setDe.add(d);
            }
            educationSchoolInfo.setDegreeCollegeInfos(setDe);
        }
        return educationSchoolInfo;
    }

}
