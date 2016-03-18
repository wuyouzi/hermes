/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.web.rest;

import java.security.Principal;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.model.pengyuan.EducationInfo;
import com.ucredit.hermes.service.EducationInfoService;

/**
 * 学历信息
 *
 * @author caoming
 */
@Controller
@RequestMapping("/rest/education")
public class EducationInfoRESTController {
    @Autowired
    private EducationInfoService service;

    /**
     * @param operationName
     *        查询人姓名
     * @param name
     *        被查询者的姓名
     * @param lendRequestId
     * @param documentNo
     *        被查询者的证件号
     * @param unitName
     *        查询机构
     * @param queryUserID
     *        操作员登录名
     * @param receiveTime
     *        查询时间
     * @param levelNo
     *        被查询者的学历证编号
     * @param keyid
     * @param graduateYear
     *        被查询者的毕业年份
     * @param college
     *        被查询者的院校
     * @param constraint
     *        是否强制刷新
     * @param activeDays
     *        有效天数
     * @param activeDate
     *        有效时间
     * @param principal
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/{operationName}", method = RequestMethod.GET)
    @ResponseBody
    public EducationInfo getEducationInfo(
            @PathVariable String operationName,
            @RequestParam String name,
            @RequestParam(required = false) String lendRequestId,
            @RequestParam(required = true) String documentNo,
            @RequestParam(required = false) String unitName,
            @RequestParam(required = false) String queryUserID,
            @RequestParam(required = false) String receiveTime,
            @RequestParam(required = false) String levelNo,
            @RequestParam(required = false) String keyid,
            @RequestParam(defaultValue = "0", required = true) int graduateYear,
            @RequestParam(required = false) String college,
            @RequestParam(defaultValue = "false", required = false) boolean constraint,
            @RequestParam(defaultValue = "30", required = false) int activeDays,
            @RequestParam(defaultValue = "", required = false) String activeDate,
            Principal principal) throws ParseException {
        name = StringUtils.trimToEmpty(name);
        documentNo = StringUtils.trimToEmpty(documentNo);
        levelNo = StringUtils.trimToEmpty(levelNo);
        college = StringUtils.trimToEmpty(college);
        Date newActiveDate = null;
        if (StringUtils.isBlank(name)) {
            return null;
        }
        if (activeDays < 1) {
            activeDays = 30;
        }
        if (StringUtils.isNotBlank(activeDate)) {
            newActiveDate = DateUtils.parseDate(activeDate, "yyyy-MM-dd");
        } else {//默认传的在new Date()之前
            newActiveDate = DateUtils.addDays(new Date(), 1);
        }
        Date newReceiveTime = null;
        if (StringUtils.isNotBlank(receiveTime)) {
            newReceiveTime = DateUtils.parseDate(receiveTime, "yyyy-MM-dd");
        }
        EducationInfo edu = null;
        try {
            edu = this.service.getEducationInfo(operationName, name,
                documentNo, unitName, queryUserID, newReceiveTime, levelNo,
                keyid, graduateYear, college, constraint, activeDays,
                newActiveDate, lendRequestId, principal).get(0);
        } catch (Exception e) {
            edu = this.service.updateErrorEducation(name, documentNo);
            e.printStackTrace();
        }
        return edu;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "education_page";
    }
}
