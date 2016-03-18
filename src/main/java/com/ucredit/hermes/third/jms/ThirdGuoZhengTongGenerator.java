/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.third.jms;

import java.net.URL;

import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.utils.DESUtils;
import com.ucredit.hermes.webservice.gzt.QueryValidatorServices;
import com.ucredit.hermes.webservice.gzt.QueryValidatorServicesService;

/**
 * @author caoming
 */
@Component
public class ThirdGuoZhengTongGenerator extends ThirdGenerator {
    @Autowired
    private Variables variables;
    private static final QName SERVICE_NAME = new QName(
        "http://app.service.validator.businesses.gboss.id5.cn",
            "QueryValidatorServicesService");

    @Override
    protected String buildConditionsXML(ThirdQueueTaskResult result) {
        return null;
    }

    /**
     * @param query
     *        icardName,icardNo
     *        姓名,身份证号
     */
    @Override
    public String sendXML(String query) throws Exception {
        URL wsdlURL = QueryValidatorServicesService.WSDL_LOCATION;
        //用https 访问接口
        System.setProperty("javax.net.ssl.trustStore", "CheckID.keystore");
        QueryValidatorServicesService ss = new QueryValidatorServicesService(
            wsdlURL, ThirdGuoZhengTongGenerator.SERVICE_NAME);
        QueryValidatorServices port = ss.getQueryValidatorServices();
        String result = port.querySingle(DESUtils.encode(
            HermesConsts.GZT_DES_KEY, this.variables.getGuoZhengTongUsername()
            .getBytes(HermesConsts.GZT_CHARSET)), DESUtils.encode(
                HermesConsts.GZT_DES_KEY, this.variables.getGuoZhengTongPassword()
                .getBytes(HermesConsts.GZT_CHARSET)), DESUtils.encode(
                    HermesConsts.GZT_DES_KEY,
                    HermesConsts.GZT_QUERY_TYPE.getBytes(HermesConsts.GZT_CHARSET)),
                    DESUtils.encode(HermesConsts.GZT_DES_KEY,
                        query.getBytes(HermesConsts.GZT_CHARSET)));
        return DESUtils.decode(HermesConsts.GZT_DES_KEY,
            result.getBytes(HermesConsts.GZT_CHARSET));
    }
}
