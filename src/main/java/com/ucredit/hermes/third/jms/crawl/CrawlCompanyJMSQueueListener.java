package com.ucredit.hermes.third.jms.crawl;

import java.lang.reflect.Field;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucredit.hermes.common.RESTTemplate;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.dao.CompanyInfoDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.pengyuan.CompanyInfo;
import com.ucredit.hermes.service.crawl.PengyuanCompanyServace;
import com.ucredit.hermes.utils.JmsUtils;

/**
 * 发起爬取平台查询，如访问接口失败进行鹏元查询
 *
 * @author zhouwuyuan
 */
@Component("crawlCompanyJMSQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class CrawlCompanyJMSQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
            .getLogger(CrawlCompanyJMSQueueListener.class);
    @Autowired
    private CompanyInfoDAO companyInfoDao;
    @Autowired
    private RESTTemplate restTemplate;
    @Autowired
    private Variables variables;
    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;
    @Autowired
    private PengyuanCompanyServace pengyuanCompanyServace;

    @Override
    public void onMessage(Message message) {
        CompanyInfo dbcompanyInfo = null;
        String lendRequestId = "";
        String companyname = "";
        String operationName = "";
        String ip = "";
        String systemId = "";
        String gsonStr = "";
        int companyid = 0;
        try {
            systemId = message
                    .getStringProperty(JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY);
            Object[] vars = (Object[]) ((ObjectMessage) message).getObject();
            companyid = (int) vars[0];
            operationName = (String) vars[1];
            ip = (String) vars[3];
            lendRequestId = (String) vars[4];
        } catch (Exception e) {
            CrawlCompanyJMSQueueListener.logger.error("工商信息mq获取查询条件出错"
                + e.getMessage());
            e.printStackTrace();
            return;
        }
        dbcompanyInfo = this.companyInfoDao.getEntityByDbId(companyid);
        String errorMessage = "hermes调用爬取平台失败：";
        try {
            if (dbcompanyInfo == null) {
                errorMessage += "根据id=%d没有查询到公司";
                throw new Exception(String.format("根据id=%d没有查询到公司", companyid));
            }
            String url = this.variables.getCrawlCompanyUrl();
            companyname = dbcompanyInfo.getCompanyName();
            CrawlCompanyJMSQueueListener.logger
                .info("------------------工商调用爬取平台获取信息--------------");
            gsonStr = this.restTemplate.getEntity(url + "?company="
                    + companyname + "&lendid=" + lendRequestId + "&companyid="
                    + companyid + "&systemid=" + systemId + "&operationName="
                    + operationName + "&ip=" + ip);
            CrawlCompanyJMSQueueListener.logger
                .info("------------------工商调用爬取平台返回信息：--------------："
                    + gsonStr);
        } catch (Exception e) {
            errorMessage += "访问爬取平台异常";
            //重置爬取的companyinfo信息
            if (dbcompanyInfo != null) {
                dbcompanyInfo.setEnabled(false);
                dbcompanyInfo.setResultType(ResultType.FAILURE);
                this.companyInfoDao.updateEntity(dbcompanyInfo);
                dbcompanyInfo.setErrorMessage(errorMessage);
            }

            e.printStackTrace();
        }
        if (dbcompanyInfo != null) {
            //调用爬取平台接口有效，则等待爬取mq返回进行解析
            if (!"".equals(gsonStr) && gsonStr.contains("ok")) {
                CrawlCompanyJMSQueueListener.logger.info("调用爬取平台接口成功（工商）");
            } else {//爬取接口返回失败
                CrawlCompanyJMSQueueListener.logger
                    .info("调用爬取平台接口失败（工商），发起鹏元查询");
                //重置爬取的companyinfo信息
                dbcompanyInfo.setEnabled(false);
                dbcompanyInfo.setResultType(ResultType.FAILURE);
                dbcompanyInfo.setErrorMessage(errorMessage);
                this.companyInfoDao.updateEntity(dbcompanyInfo);
                //重新发起查询，走鹏元流程
                this.pengyuanCompanyServace.searchFromPengyuan(operationName,
                    null, ip, lendRequestId, systemId, companyname,
                    dbcompanyInfo.getKeyid());
            }
        }

    }

    /**
     * 将爬取过来的company对象和本地存储对象相同属性做一个映射
     *
     * @param crawl
     *        爬取的对象
     * @param local
     *        本地存储对象
     * @return
     */
    public T convertConpany(Object crawl, T local) {
        //获得爬取对象的属性
        Field[] fieldsCrawl = crawl.getClass().getDeclaredFields();
        //获得本地存储对象的属性
        Field[] fieldsCom = local.getClass().getDeclaredFields();
        for (Field field : fieldsCrawl) {
            String fieldName = field.getName();
            for (Field fieldC : fieldsCom) {
                String fieldNameC = field.getName();
                //当crawl对象和本地对象的属性和类型都相同时，值做映射
                if (fieldName.equals(fieldNameC)
                        && field.getType().toString()
                        .equals(fieldC.getType().toString())) {
                    try {
                        Field fieldData = local.getClass().getDeclaredField(
                            fieldName);
                        fieldData.setAccessible(true);
                        Object value = field.get(crawl);
                        fieldData.set(local, value);
                    } catch (NoSuchFieldException | SecurityException
                            | IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return local;
    }

    private void sendMessage(CompanyInfo resp, String systemId,
            String requestId, AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider.packageResponse(resp,
                systemId, DataChannelSubType.PENGYUAN_COMPANY, requestId,
                asyncCode);
            this.provider.sendTextMessage(messageBody, systemId,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
