/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.web.rest.juxinli;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.service.juxinli.MiGuanService;

/**
 * 聚信立
 *
 * @author zhouwuyuan
 */
@Controller
@RequestMapping("/rest/miguan")
public class MiguanRESTController {
    @Autowired
    private MiGuanService miGuanService;

    /**
     * 蜜罐访问接口
     *
     * @param name
     * @param idcard
     * @param phone
     * @param principal
     * @return
     */
    @RequestMapping(value = "/searchReport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> searchReport(
            @RequestParam(required = false) String name, @RequestParam(
                    required = false) String idcard, @RequestParam(
                    required = false) String phone, @RequestParam(
                    required = false) String apply_id, @RequestParam(
                    required = false) String contactType, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        if (name == null || "".equals(name)) {
            result.put("msg", "姓名不能为空");
            return result;
        } else if (idcard == null || "".equals(idcard)) {
            result.put("msg", "身份证不能为空");
            return result;
        } else if (phone == null || "".equals(phone)) {
            result.put("msg", "电话不能为空");
            return result;
        }
        return this.miGuanService.searchReport(name, idcard, phone, apply_id,
            contactType, principal);
    }

}
