/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.web.rest;

import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.model.pengyuan.GdSiPersonInfo;
import com.ucredit.hermes.service.GdSiPersonInfoService;

/**
 * 广东个人社保信息
 * 
 * @author caoming
 */
@Controller
@RequestMapping("/rest/gdsi")
public class GdSiPersonInfoRESTController {
    @Autowired
    private GdSiPersonInfoService service;

    /**
     * 专线获取个人社保信息
     * 
     * @param operationName
     *        访问人姓名
     * @param name
     *        被查询者名称，必传
     * @param documentNo
     *        被查询者证件号，必传
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
    public List<GdSiPersonInfo> getGdSiPersonInfo(
            @PathVariable String operationName,
            @RequestParam String name,
            @RequestParam String documentNo,
            @RequestParam(defaultValue = "false", required = false) boolean constraint,
            @RequestParam(defaultValue = "14", required = false) int activeDays,
            @RequestParam(defaultValue = "", required = false) String activeDate,
            Principal principal) throws ParseException {
        name = StringUtils.trimToEmpty(name);
        documentNo = StringUtils.trimToEmpty(documentNo);
        Date newActiveDate = null;
        if (StringUtils.isBlank(name) || StringUtils.isBlank(documentNo)) {
            return null;
        }
        if (activeDays < 1) {
            activeDays = 14;
        }
        if (StringUtils.isNotBlank(activeDate)) {
            newActiveDate = DateUtils.parseDate(activeDate, "yyyy-MM-dd");
        } else {//默认传的在new Date()之前
            newActiveDate = DateUtils.addDays(new Date(), 1);
        }
        return this.service.getGdSiPersonInfo(operationName, name, documentNo,
            constraint, activeDays, newActiveDate, principal);
    }
}
