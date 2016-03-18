/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.third.jms;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.enums.QueryType;
import com.ucredit.hermes.enums.QueryXmlType;
import com.ucredit.hermes.model.Conditions;
import com.ucredit.hermes.model.Conditions.Condition;
import com.ucredit.hermes.model.Conditions.Item;
import com.ucredit.hermes.model.ThirdQueueTaskResult;

/**
 * @author caoming
 */
@Component
public class ThirdPengYuanGenerator extends ThirdGenerator {
    private static Logger logger = LoggerFactory
            .getLogger(ThirdPengYuanGenerator.class);
    public static final String SUB_REPORT_IDS = "subreportIDs";
    public static final String REF_IDS = "refID";
    public static final String QUERY_REASON_ID = "queryReasonID";
    //企业查询类型
    public static final String COMPANY_QUERY_TYPE = "25123";
    // 专线 个人户籍信息
//    private static final String PERSON_QUERY_TYPE = "33103";
    // 全国学历 学籍信息、个人户籍信息
    public static final String EDUCATION_QUERY_TYPE = "33124";
    //最高学历信息
    public static final String LAST_EDUCATION_QUERY_TYPE = "33125";
    // 全国学籍信息
    public static final String EDUCATION_INSCHOOL_QUERY_TYPE = "33123";
    //广东社保信息
    public static final String GD_SI_QUERY_TYPE = "33102";

    @Autowired
    private Variables variables;

    /**
     * 拼接鹏元查询xml
     *
     * @author caoming Email:caoming@ucredit.com
     * @throws Exception
     */
    @Override
    protected String buildConditionsXML(ThirdQueueTaskResult result)
            throws Exception {
        Conditions conditions = new Conditions();
        Condition condition = new Condition();
        List<Item> items = new LinkedList<>();

        //查询类型
        JMSType type = result.getType();

        if (StringUtils.isNotBlank(result.getName())) {//名称
            Item item = new Item();
            // 个人还是企业
            item.setName(type == JMSType.THIRD_JMS_QUEUE ? QueryXmlType.CORP_NAME
                .toString() : QueryXmlType.PERSON_NAME.toString());
            item.setValue(result.getName());
            items.add(item);
        }
        if (StringUtils.isNotBlank(result.getCode())) {// 企业组织注册号
            Item item = new Item();
            item.setName(QueryXmlType.REGISTER_NO.toString());
            item.setValue(result.getCode());
            items.add(item);
        }
        if (StringUtils.isNotBlank(result.getNumber())) {// 证件号
            Item item = new Item();
            // 个人还是企业
            item.setName(type == JMSType.THIRD_JMS_QUEUE ? QueryXmlType.ORG_CODE
                .toString() : QueryXmlType.DOCUMENT_NO.toString());
            item.setValue(result.getNumber());
            items.add(item);
        }
        if (StringUtils.isNotBlank(result.getLevelNo())) {
            Item item = new Item();
            item.setName(QueryXmlType.LEVEL_NO.toString());
            item.setValue(result.getLevelNo());
            items.add(item);
        }
        if (StringUtils.isNotBlank(result.getCollege())) {
            Item item = new Item();
            item.setName(QueryXmlType.COLLEGE.toString());
            item.setValue(result.getCollege());
            items.add(item);
        }
        if (result.getGraduateYear() > 0) {
            Item item = new Item();
            item.setName(QueryXmlType.GRADUATE_YEAR.toString());
            item.setValue(String.valueOf(result.getGraduateYear()));
            items.add(item);
        }
        Item item3 = new Item();
        item3.setName(ThirdPengYuanGenerator.SUB_REPORT_IDS);
        QueryType queryType = null;
        switch (type) {
            case THIRD_JMS_QUEUE:
                item3.setValue(HermesConsts.PENG_YUAN_QUERY_TYPE);
                condition
                .setQueryType(ThirdPengYuanGenerator.COMPANY_QUERY_TYPE);

                queryType = QueryType.CHECK_COMPANY;
                break;
                // 全国个人户籍信息
            case POLICE_JMS_QUEUE:
                item3.setValue("10600");
                condition
                .setQueryType(ThirdPengYuanGenerator.EDUCATION_QUERY_TYPE);
                queryType = QueryType.CHECK_POLICE;
                break;
                // 全国个人学历信息
            case EDUCATION_JMS_QUEUE:
                item3.setValue("11104");
                condition
                .setQueryType(ThirdPengYuanGenerator.EDUCATION_QUERY_TYPE);
                queryType = QueryType.PERSON_EDUCATION;
                break;
                // 全国个人最高学历信息
            case LAST_EDUCATION_JMS_QUEUE:
                item3.setValue("11103");
                condition
                .setQueryType(ThirdPengYuanGenerator.LAST_EDUCATION_QUERY_TYPE);
                queryType = QueryType.PERSON_EDUCATION;
                break;
                // 全国个人学籍信息
            case IN_SCHOOL_EDUCATION_JMS_QUEUE:
                item3.setValue("11107");
                condition
                .setQueryType(ThirdPengYuanGenerator.EDUCATION_INSCHOOL_QUERY_TYPE);
                queryType = QueryType.PERSON_EDUCATION_INSCHOOL;
                break;
                // 广东省社保信息
            case GD_SI_JMS_QUEUE:
                item3.setValue("90018");
                condition.setQueryType(ThirdPengYuanGenerator.GD_SI_QUERY_TYPE);
                queryType = QueryType.GDSI_PERSION;
                break;
            default:
                break;
        }

        Item queryReasonItem = new Item();
        queryReasonItem.setName(ThirdPengYuanGenerator.QUERY_REASON_ID);
        queryReasonItem.setValue(HermesConsts.QUERY_REASON_TYPE);

        Item item4 = new Item();
        item4.setName(ThirdPengYuanGenerator.REF_IDS);

        if (queryType == null) {
            throw new Exception("代码需要完善，暂不支持,需要完善 queryType");
        }
        String refId = ThirdGenerator.getBatchNumber(queryType,
            DataChannel.PENGYUAN);
        item4.setValue(refId);
        result.setRefId(refId);

        items.add(item3);
        items.add(queryReasonItem);
        items.add(item4);
        condition.setItem(items);
        conditions.setCondition(condition);
        String data = "";
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(Conditions.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty("jaxb.encoding", "GB2312");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            /*
             * jaxbMarshaller
             * .setProperty("com.sun.xml.bind.xmlDeclaration", false);
             */

            @SuppressWarnings("resource")
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(conditions, baos);
            String version = "<?xml version=\"1.0\" encoding=\"GBK\" ?>";
            data = version + new String(baos.toByteArray(), "GBK");
            ThirdPengYuanGenerator.logger.debug("查询xml" + data);
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 查询第三方数据
     *
     * @throws Exception
     */
    @Override
    public String sendXML(String query) throws Exception {
//        Client client = new Client(
//            new URL(
//                "http://www.pycredit.com:9001/services/WebServiceSingleQueryOfUnzip?wsdl"));

        /**
         * 线上
         */
        String url = this.variables.getPengYuanURL();
        String username = this.variables.getPengYuanUsername();
        String userpassword = this.variables.getPengYuanPassword();
        /**
         * 本地测试
         */
//        String url = "http://211.211.1.69:3129/services/WebServiceSingleQuery?wsdl";
//        String username = "yzxywsquery";
//        String userpassword = "{MD5}ACYqZyEUjVmBejOx9sNEDg==";

        /**
         * old
         */
//        String url = "http://www.pycredit.com:9001/services/WebServiceSingleQueryOfUnzip?wsdl";
//        String username = "yzhxywsquery";
//        String userpassword = "yaB58LPXEuLzm/CUVyslCg==";

        Client client = new Client(new URL(url));
        //设置返回超时时间
        HttpClientParams params = new HttpClientParams();
        params
        .setParameter(HttpMethodParams.USE_EXPECT_CONTINUE, Boolean.FALSE);
        params.setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler(2, true));
        params.setParameter(HttpMethodParams.SO_TIMEOUT, 180000);
        ThirdPengYuanGenerator.logger.info("鹏元HttpClientParams查询"
                + HttpMethodParams.SO_TIMEOUT + ","
                + HttpMethodParams.RETRY_HANDLER);
        client.setProperty(CommonsHttpMessageSender.HTTP_CLIENT_PARAMS, params);
        Object[] objects = client.invoke("queryReport", new Object[] {
            username, userpassword, query, "xml" });
        ThirdPengYuanGenerator.logger.debug("返回的xml：" + objects[0].toString());
        if (objects[0] instanceof String) {
            return objects[0].toString();
        } else if (objects[0] instanceof org.w3c.dom.Document) {
            org.w3c.dom.Document document = (org.w3c.dom.Document) objects[0];
            document.getDocumentElement().getChildNodes().item(0)
            .getNodeValue();
            ThirdPengYuanGenerator.logger.debug("输出日志...."
                    + document.getChildNodes().item(0).getNodeValue());
            DOMSource domSource = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return DocumentHelper.parseText(writer.toString()).getRootElement()
                    .getStringValue();
        }
        return null;
    }

    /*
     * public static void main(String[] args) throws Exception {
     * // ThirdQueueTaskResult result = new ThirdQueueTaskResult();
     * // result.setName("深圳市腾讯计算机系统有限公司");
     * // ThirdPengYuanGenerator generator = new ThirdPengYuanGenerator();
     * // String xml = generator.buildConditionsXML(result);
     * // String string = generator.sendXML(xml);
     * // System.out.println(string);
     * String companyName = "武汉市美妲莉妍服饰有限公司";
     * String orgCode = null;
     * String registerNo = null;
     * String refId = "PY_CC141126110247920";
     * String xml = ThirdQueueListener.buildConditionsXML(companyName,
     * orgCode, registerNo, refId);
     * System.out.println(xml);
     * ThirdPengYuanGenerator sender = new ThirdPengYuanGenerator();
     * String result = sender.sendXML(xml);
     * System.out.println("返回结果");
     * System.out.println(result);
     * }
     */

}
