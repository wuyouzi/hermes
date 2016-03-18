/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.web.rest;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.service.UpdateCompanyInfoDataService;

/**
 * 修正数据错误接口
 * 
 * @author liuqianqian
 */
@Controller
@RequestMapping("/rest/info/companyInfo")
public class UpdateCompanyInfoDataRESTController {
    @Autowired
    private UpdateCompanyInfoDataService service;

    /**
     * 修正数据错误接口
     * 
     * @throws DocumentException
     */
    @RequestMapping(value = "/update ", method = RequestMethod.PUT)
    @ResponseBody
    public void updateCompanyInfoDate() throws DocumentException {
        this.service.updateCompanyInfoData();
    }

}
