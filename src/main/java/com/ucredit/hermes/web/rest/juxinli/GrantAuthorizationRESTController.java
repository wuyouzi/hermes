/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.web.rest.juxinli;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.model.juxinli.GrantAuthorizationInfo;
import com.ucredit.hermes.service.juxinli.GrantAuthorizationService;

/**
 * 聚信立
 *
 * @author zhouwuyuan
 */
@Controller
@RequestMapping("/rest/juxinli")
public class GrantAuthorizationRESTController {
    @Autowired
    private GrantAuthorizationService grantAuthorizationService;

    /**
     * 接收app端授权数据
     *
     * @param id
     * @param name
     * @param idcard
     * @param phone
     * @param principal
     * @return
     */
    //改方法由于需求变动暂时不用，后面可能会还原
    /*
     * @RequestMapping(value = "/grantAuthorization", method =
     * RequestMethod.GET)
     * @ResponseBody
     * public Map<String, Object> GrantAuthorizationFromApp(@RequestParam(
     * defaultValue = "", required = false) String id, @RequestParam(
     * defaultValue = "", required = true) String name, @RequestParam(
     * defaultValue = "", required = true) String idcard, @RequestParam(
     * defaultValue = "", required = true) String phone,
     * Principal principal) {
     * Map<String, Object> result = new HashMap<>();
     * if (name.isEmpty()) {
     * result.put(HermesConsts.MSG, "参数name不能为空！");
     * return result;
     * } else if (idcard.isEmpty()) {
     * result.put(HermesConsts.MSG, "参数idcard不能为空！");
     * return result;
     * } else if (phone.isEmpty()) {
     * result.put(HermesConsts.MSG, "参数phone不能为空！");
     * return result;
     * }
     * this.grantAuthorizationService.saveGrantAuthorizationInfo(id, name,
     * idcard, phone, principal);
     * return null;
     * }
     */

    /**
     * * 发起聚信立查询报告2
     *
     * @param name
     *        姓名
     * @param idcard
     *        身份证号
     * @param phone
     *        电话
     * @param home_tel
     *        住宅电话
     * @param home_addr
     *        住宅的地址
     * @param work_tel
     *        公司电话
     * @param couple_phone_num
     *        配偶电话
     * @param contact_list
     *        联系人数组
     * @param keyid
     *        唯一标识，接收什么值后面返回就什么值
     * @param activeDays
     *        有效期
     * @param constraint
     *        是否强制刷新
     * @param principal
     * @return
     */
    @RequestMapping(value = "/searchReport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> searchReport(
        @RequestParam(required = true) String name,
        @RequestParam(required = true) String idcard,
        @RequestParam(required = true) String phone,
        @RequestParam(required = false) String home_tel,
        @RequestParam(required = true) String home_addr,
        @RequestParam(required = false) String work_tel,
        @RequestParam(required = false) String couple_phone_num,
        @RequestParam(required = false) String contact_list,
        @RequestParam(required = false) String keyid,
        @RequestParam(defaultValue = "30", required = false) int activeDays,
        @RequestParam(defaultValue = "false", required = false) boolean constraint,
        Principal principal) {
        Map<String, Object> result = new HashMap<>();

        //后期改了，不接受app是否授权都需要查询，为了后面的主外键统一，这里对用户信息做一个保存
        this.grantAuthorizationService.saveGrantAuthorizationInfo("", name,
            idcard, phone, home_tel, home_addr, work_tel, couple_phone_num,
            contact_list, keyid, principal);
        //检查用户是否授权
        List<GrantAuthorizationInfo> grantAuthorizationInfos = this.grantAuthorizationService
                .GrantAuthorizationInfo(name, idcard, phone);
        if (grantAuthorizationInfos.isEmpty()) {
            result.put(HermesConsts.MSG, "用户未授权，App未返回用户信息");
            return result;
        } else {
            return this.grantAuthorizationService.searchReport(name, idcard,
                phone, activeDays, constraint, grantAuthorizationInfos.get(0),
                home_tel, home_addr, work_tel, couple_phone_num, contact_list,
                keyid, principal);
        }
    }
}
