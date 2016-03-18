/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.web.rest;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.service.BlackListService;

/**
 * 黑名单Controller
 *
 * @author Liuhao
 */
@Controller
@RequestMapping("/rest/blackList")
public class BlackListRESTController {
    @Autowired
    private BlackListService blackListservice;

    /**
     * 本公司查询是否黑名单
     *
     * @param identityNo
     * @param phone
     * @param companyName
     * @param workTel
     * @param homeTel
     * @param lendRequestId
     * @param principal
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/queryFromUcredit", method = RequestMethod.GET)
    public List<Map<String, String>> queryFromUcredit(@RequestParam(
            required = false) String identityNo,
            @RequestParam(required = false) String phone, @RequestParam(
                    required = false) String companyName, @RequestParam(
                    required = false) String workTel, @RequestParam(
                    required = false) String homeTel, @RequestParam(
                    required = true) String lendRequestId, Principal principal) {
        List<Map<String, String>> blackLists = new ArrayList<>();
        try {
            blackLists = this.blackListservice.queryFromUcredit(identityNo,
                phone, companyName, workTel, homeTel, lendRequestId, principal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blackLists;
    }

    /**
     * 外部公司查询是否黑名单
     *
     * @param queryParams
     * @param principal
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public List<Map<String, String>> query(
            @RequestBody(required = false) JSONObject queryParams,
            Principal principal) {
        List<Map<String, String>> result = new ArrayList<>();
        try {
            result = this.blackListservice.queryFromOthers(queryParams,
                principal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 本公司添加黑名单
     *
     * @param identityNo
     * @param phone
     * @param companyName
     * @param workTel
     * @param homeTel
     * @param lendRequestId
     * @param effectTime
     * @param principal
     * @return
     * @throws ParseException
     */

    @ResponseBody
    @RequestMapping(value = "/addBlackList", method = RequestMethod.GET)
    public boolean addBlackList(
            @RequestParam(required = false) String identityNo, @RequestParam(
                    required = false) String phone, @RequestParam(
                    required = false) String companyName, @RequestParam(
                    required = false) String workTel, @RequestParam(
                    required = false) String homeTel, @RequestParam(
                    required = true) String lendRequestId, @RequestParam(
                    required = true) String effectTime, Principal principal)
            throws ParseException {
        Date time = new SimpleDateFormat("yyyy-MM-dd").parse(effectTime);
        return this.blackListservice.addBlackList(identityNo, phone,
            companyName, workTel, homeTel, lendRequestId, time, principal);
    }
}