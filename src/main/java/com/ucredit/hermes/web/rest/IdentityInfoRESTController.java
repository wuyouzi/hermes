/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.ucredit.hermes.model.guozhengtong.IdentityInfo;
import com.ucredit.hermes.service.IdentityInfoService;

/**
 * 身份信息接口
 *
 * @author liuqianqian
 */
@Controller
@RequestMapping("/rest/info/identityInfo")
public class IdentityInfoRESTController {
    @Autowired
    private IdentityInfoService service;

    /**
     * 根据企业名称，注册号，组织机构代码查询 支持同步异步
     *
     * @param operationName
     *        第三方操作人员
     * @param name
     * @param identitycard
     *        身份证号
     * @param keyid
     * @param isSynch
     *        true:同步 false：异步
     * @param constraint
     *        强制刷新
     * @param activeDays
     *        有效天数
     * @param activeDate
     *        有效日期
     * @param principal
     * @return IdentityInfo
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    // @PreAuthorize("hasRole('****')")
    @RequestMapping(value = "/list/{operationName}", method = RequestMethod.GET)
    @ResponseBody
    public IdentityInfo getIdentityInfosList(
            @PathVariable String operationName,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) String identitycard,
            @RequestParam(required = false) String keyid,
            @RequestParam(defaultValue = "false", required = false) boolean isSynch,
            @RequestParam(defaultValue = "false", required = false) boolean constraint,
            @RequestParam(defaultValue = "90", required = false) int activeDays,
            @RequestParam(defaultValue = "", required = false) String activeDate,
            Principal principal) throws ParseException,
            UnsupportedEncodingException {
        name = URLDecoder.decode(name, "UTF-8");
        Date newActiveDate = null;
        if (StringUtils.isBlank(name) || StringUtils.isBlank(identitycard)) {
            IdentityInfo identityInfo = new IdentityInfo();
            identityInfo.setErrorMessage("身份证号码和姓名不能为空！");
            return identityInfo;
        }
        //限制传过来的值为正数，防止用户有意强制刷新
        if (activeDays < 1) {
            activeDays = 90;
        }
        if (StringUtils.isNotBlank(activeDate)) {
            newActiveDate = DateUtils.parseDate(activeDate, "yyyy-MM-dd");
        } else {// 当activeDate为空时，默认newActiveDate为有效时间
            newActiveDate = DateUtils.addDays(new Date(), 1);
        }
        return this.service.getIdentityInfos(operationName, name, identitycard,
            keyid, isSynch, constraint, activeDays, newActiveDate, principal);
    }
}
