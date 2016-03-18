package com.ucredit.hermes.third.jms;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ucredit.hermes.common.RESTTemplate;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.juxinli.GrantAuthorizationDAO;
import com.ucredit.hermes.dao.juxinli.ReportDataDAO;
import com.ucredit.hermes.dao.juxinli.SaveEntityDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.juxinli.JsonInfoTwo;
import com.ucredit.hermes.model.juxinli.JuxinliDataInfo;
import com.ucredit.hermes.model.juxinli.ReportBackInfo;
import com.ucredit.hermes.model.juxinli.ReportData;
import com.ucredit.hermes.model.juxinli.ReportTwo;
import com.ucredit.hermes.service.juxinli.GrantAuthorizationService;

/**
 * 生成报告监听
 *
 * @author zhouwuyuan
 */
@Scope("prototype")
@Component("juxinliReport1JMSQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class JuxinliReport1JMSQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
        .getLogger(JuxinliReport1JMSQueueListener.class);
    @Autowired
    private Variables variables;
    @Autowired
    private GrantAuthorizationService grantAuthorizationService;
    @Autowired
    private LogInfoDAO logInfoDao;
    @Autowired
    private SaveEntityDAO saveEntityDAO;
    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;
    @Autowired
    private ReportDataDAO reportDataDAO;
    @Autowired
    private GrantAuthorizationDAO grantAuthorizationDAO;

    @Autowired
    private RESTTemplate restTemplate;
    private static final String JUXINLI_TABLE_NAME = "report_data";

    @Override
    public void onMessage(Message message) {

        String queryString = "";
        String username = "";
        LogInfo logInfo = new LogInfo(username, null, null, new Date(),
            JuxinliReport1JMSQueueListener.JUXINLI_TABLE_NAME);
        ReportData reportData = new ReportData();
        ReportData reportData1 = new ReportData();
        try {
            //间隔一段时间查询报告
            //Thread.sleep(60000 * 10);
            Thread.sleep(600 * 10);
            Object[] vars = (Object[]) ((ObjectMessage) message).getObject();
            ThirdQueueTaskResult result = (ThirdQueueTaskResult) vars[0];
            String name = result.getName();
            username = result.getUserName();
            String idcard = result.getIdcard();
            String phone = result.getPhone();
            String ip = result.getIp();
            int fkId = result.getFkId();
            String home_tel = result.getHome_tel();
            String home_addr = result.getHome_addr();
            String work_tel = result.getWork_tel();
            String keyid = result.getKeyid();
            String couple_phone_num = result.getCouple_phone_num();
            String contact_list = result.getContact_list();
            reportData1 = this.reportDataDAO.getReportData(fkId, 1);
            int reportNum = result.getReportNum();
            JuxinliReport1JMSQueueListener.logger
            .info("--------report---------" + reportNum + "  fkId:" + fkId);
            String url = this.variables.getJuxinliUrl();
            String token = this.variables.getJuxinliAccessToken();
            String secret = this.variables.getJuxinliClientSecret();
            String params = "?name=" + name + "&idcard=" + idcard + "&phone="
                    + phone + "&client_secret=" + secret + "&access_token=" + token;
            queryString = url + params;
            JuxinliReport1JMSQueueListener.logger.debug("queryString---->:"
                + queryString);
            logInfo.setIp(ip);
            logInfo.setQueryString(queryString);
            logInfo.setEndTime(new Date());
            logInfo.setOperationName(username);
            logInfo.setDataChannel(DataChannel.JUXINLI);
            logInfo.setRecordId(fkId);
            reportData.setGrant_authorization_id(fkId);
            this.logInfoDao.addEntity(logInfo);
            reportData.setReportNum(reportNum);
            //保存报告初始信息
            this.saveEntityDAO.addEntity(reportData);
            //报告查询时间
            reportData.setQueryTime(new Date());
            String data = "";
            String errorMessage = "";
            try {
                data = this.restTemplate.getEntity(queryString, String.class);
            } catch (IOException e1) {
                errorMessage = e1.getMessage();
                JuxinliReport1JMSQueueListener.logger.error("访问第三方异常："
                        + ObjectUtils.toString(e1));
                e1.printStackTrace();
            }
            //报告返回时间
            reportData.setBackTime(new Date());
            if (data.isEmpty()) {
                logInfo.setErrorMessage(errorMessage);
                logInfo.setEndTime(new Date());
                logInfo.setResultType(ResultType.FAILURE);
                reportData.setEnabled(false);
                reportData.setErrorCode(AsyncCode.FAILURE_EMPTYDATA);
                reportData.setErrorMessage("链接第三方出错" + errorMessage);
                JuxinliReport1JMSQueueListener.logger.error("链接第三方出错，"
                        + errorMessage);
                JuxinliReport1JMSQueueListener.logger
                    .info("-----------------begin聚信立listen插入队列-----------------失败");
                this.sendMessage(null, username, keyid,
                    AsyncCode.FAILURE_EMPTYDATA);
                JuxinliReport1JMSQueueListener.logger
                    .info("-----------------end聚信立listen插入队列-----------------失败");
                this.grantAuthorizationDAO.updateBeforeGrant(name, idcard,
                    phone);
                return;
            } else {
                logInfo.setEndTime(new Date());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                ReportBackInfo reportInfo = gson.fromJson(data,
                    ReportBackInfo.class);
                JsonInfoTwo gsonInfo = reportInfo.getReport_data();
                //report 报告基本信息
                if (gsonInfo != null) {
                    ReportTwo report = gsonInfo.getReport();
                    reportData.setBack_id(report.getId());
                    reportData.setToken(report.getToken());
                    reportData.setUuid(report.getUid());
                    reportData.setVersion(report.getVersion());
                    reportData.set$Date(report.getUpdt());
                    reportData.setSuccess(reportInfo.getSuccess());
                    reportData.setNote(reportInfo.getNote());
                    //解析所有报告
                    this.grantAuthorizationService.saveReport(gsonInfo,
                        reportData, reportNum);
                    logInfo.setResultType(ResultType.SUCCESS);
                    logInfo.setOperationName(username);
                    //报告一查询成功
                    if (reportNum == 1) {
                        JuxinliReport1JMSQueueListener.logger
                        .info("-----------------聚信立报告一查询-----------------");
                        this.grantAuthorizationService.sendToQueue(name,
                            idcard, phone, username, ip, 2, fkId, keyid,
                            home_tel, home_addr, work_tel, couple_phone_num,
                            contact_list, JMSType.JU_XIN_LI_VERIFY);
                    }
                    if (reportNum == 2) {
                        JuxinliReport1JMSQueueListener.logger
                        .info("-----------------进入聚信立队列-----------------");
                        JuxinliDataInfo juxinliReportData = new JuxinliDataInfo();
                        ReportData reportData2 = this.reportDataDAO
                                .getReportData(fkId, 2);
                        juxinliReportData.setReportData1(reportData1);
                        juxinliReportData.setReportData2(reportData2);
                        JuxinliReport1JMSQueueListener.logger
                        .info("-----------------begin聚信立listen插入队列-----------------"
                                + username);
                        this.sendMessage(juxinliReportData, username, keyid,
                            AsyncCode.SUCCESS);
                        JuxinliReport1JMSQueueListener.logger
                        .info("-----------------end聚信立listen插入队列-----------------"
                                + username);
                    }
                } else {
                    reportData.setNote(reportInfo.getNote());
                    reportData.setErrorMessage(reportInfo.getNote());
                    reportData.setSuccess(reportInfo.getSuccess());
                    JuxinliReport1JMSQueueListener.logger
                    .info("-----------------begin聚信立listen插入队列-----------------失败");
                    this.sendMessage(null, username, keyid,
                        AsyncCode.FAILURE_EMPTYDATA);
                    JuxinliReport1JMSQueueListener.logger
                        .info("-----------------end聚信立listen插入队列-----------------失败");
                }

            }
        } catch (Exception e) {
            logInfo.setErrorMessage(e.getMessage());
            logInfo.setEndTime(new Date());
            logInfo.setResultType(ResultType.FAILURE);
            reportData.setEnabled(false);
            reportData.setErrorCode(AsyncCode.FAILURE_HERMES_ERROR);
            reportData.setErrorMessage("Hermes出错" + e.getMessage());
            JuxinliReport1JMSQueueListener.logger.error("保存报告失败，"
                    + e.getMessage());
            e.printStackTrace();
        }

    }

    private void sendMessage(JuxinliDataInfo resp, String systemId,
            String requestId, AsyncCode asyncCode) {
        //聚信立接口特殊封装
        Map<String, Object> result = new HashMap<>();
        result.put("msg", resp);
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider.packageResponse(result,
                systemId, DataChannelSubType.JUXINLI, requestId, asyncCode);
            this.provider.sendTextMessage(messageBody, systemId,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
