/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.model.pengyuan.CompanyInfo;
import com.ucredit.hermes.service.CompanyInfoService;
import com.ucredit.hermes.utils.BCConvertUtils;

/**
 * 企业相关信息接口
 *
 * @author liuqianqian
 */
@Controller
@RequestMapping("/rest/info/companyInfo")
public class CompanyInfoRESTController {
    private static Logger logger = LoggerFactory
            .getLogger(CompanyInfoRESTController.class);
    @Autowired
    private CompanyInfoService service;

    /**
     * 根据企业名称，注册号，组织机构代码查询
     *
     * @param operationName
     *        当前操作人
     * @param companyName
     *        公司名称
     * @param keyid
     * @param constraint
     *        强制刷新
     * @param activeDays
     *        有效天数
     * @param activeDate
     *        有效日期
     * @param lendRequestId
     * @param principal
     * @return CompanyInfo
     * @throws Exception
     */
    // @PreAuthorize("hasRole('****')")
    @RequestMapping(value = "/list/{operationName}", method = RequestMethod.GET)
    @ResponseBody
    public CompanyInfo getCompanyInfosList(
            @PathVariable String operationName,
            @RequestParam(defaultValue = "", required = false) String companyName,
            @RequestParam(defaultValue = "", required = false) String keyid,
            // @RequestParam(defaultValue = "", required = false) String
            // orgCode,
            // @RequestParam(defaultValue = "", required = false) String
            // registerNo,
            @RequestParam(defaultValue = "false", required = false) boolean constraint,
            @RequestParam(defaultValue = "30", required = false) int activeDays,
            @RequestParam(defaultValue = "", required = false) String activeDate,
            @RequestParam(defaultValue = "", required = false) String lendRequestId,
            Principal principal) throws Exception {

        // 相应的日志

        StringBuilder sb = new StringBuilder(companyName);
        CompanyInfoRESTController.logger.debug(sb
            .append(": <强制查询的参数：constraint=").append(constraint)
            .append("; activeDays=").append(activeDays).append("; activeDate=")
            .append(activeDate).append(">").toString());

        companyName = StringUtils.trimToEmpty(companyName);
        Date newActiveDate = null;
        if (StringUtils.isBlank(companyName)) {

            return null;
        }
        if (activeDays < 1) {
            activeDays = 30;
        }
        if (StringUtils.isNotBlank(activeDate)) {
            // 判断是否符合日期格式yyyy-MM-dd
            if (!com.ucredit.hermes.utils.DateUtils.isValidFormat(activeDate)) {
                return null;
            }
            newActiveDate = DateUtils.parseDate(activeDate, "yyyy-MM-dd");
        } else {// 默认传的在new Date()之前
            newActiveDate = DateUtils.addDays(new Date(), 1);
        }
        CompanyInfo com = null;
        try {
            com = this.service.getCompanyInfos(operationName, companyName,
                keyid, null, null, constraint, activeDays, newActiveDate,
                lendRequestId, principal);
        } catch (Exception e) {
            com = this.service.updateErrorCompany(companyName);
            e.printStackTrace();

        }
        return com;

        // 全角转化为半角
        // companyName = BCConvertUtils.qj2bj(companyName);
        // orgCode = BCConvertUtils.qj2bj(orgCode);
        // registerNo = BCConvertUtils.qj2bj(registerNo);
        // return this.service.getCompanyInfos(operationName, companyName,
        // orgCode, registerNo, constraint, activeDays, newActiveDate,
        // principal);

    }

    /**
     * 根据企业名称模糊查询
     *
     * @param companyName
     * @param principal
     * @return List
     */
    // @PreAuthorize("hasRole('****')")
    @RequestMapping(value = "/byName ", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getCompanyByName(@RequestParam(
        required = true) String companyName, Principal principal) {
        String name = StringUtils.trimToEmpty(companyName);

        try {
            name = URLDecoder.decode(name, "utf8");
        } catch (UnsupportedEncodingException ex) {
            CompanyInfoRESTController.logger
            .warn("Cannot decode url compantName <" + name + ">");
        }

        CompanyInfoRESTController.logger.debug(name);

        if (StringUtils.containsAny(name, new char[] { '%', '_' })) {
            // 防止查询中含有SQL通配符
            return new ArrayList<>(0);
        }

        // 全角转化为半角
        name = BCConvertUtils.qj2bj(name);
        return this.service.getCompanyByNames(name, principal);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "companyInfo";
    }
}
