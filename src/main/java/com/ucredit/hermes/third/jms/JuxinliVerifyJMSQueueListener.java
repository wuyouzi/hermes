package com.ucredit.hermes.third.jms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ucredit.hermes.common.RESTTemplate;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.juxinli.ReportDataDAO;
import com.ucredit.hermes.dao.juxinli.VerifyContactListDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.juxinli.JuxinliDataInfo;
import com.ucredit.hermes.model.juxinli.ReportData;
import com.ucredit.hermes.model.juxinli.VerifyBackInfo;
import com.ucredit.hermes.model.juxinli.VerifyContactList;
import com.ucredit.hermes.service.juxinli.GrantAuthorizationService;

/**
 * 验证信息监听
 *
 * @author zhouwuyuan
 */
@Component("juxinliVerifyJMSQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class JuxinliVerifyJMSQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
            .getLogger(JuxinliVerifyJMSQueueListener.class);
    private static final String JUXINLI_VERIFY_TABLE_NAME = "verify_contact_list";
    @Autowired
    private Variables variables;
    @Autowired
    private GrantAuthorizationService grantAuthorizationService;
    @Autowired
    private VerifyContactListDAO verifyContactListDAO;
    @Autowired
    private LogInfoDAO logInfoDao;
    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;
    @Autowired
    private ReportDataDAO reportDataDAO;
    @Autowired
    private RESTTemplate restTemplate;

    @Override
    public void onMessage(Message message) {
        Object[] vars = null;
        try {
            vars = (Object[]) ((ObjectMessage) message).getObject();
        } catch (JMSException e1) {
            JuxinliVerifyJMSQueueListener.logger.error("JMSException :"
                + ObjectUtils.toString(e1));
            e1.printStackTrace();
        }
        if (vars == null || vars.length == 0) {
            JuxinliVerifyJMSQueueListener.logger.error("vars == null,message:"
                + ObjectUtils.toString(message));
            return;
        }
        // 1.
        ThirdQueueTaskResult result = (ThirdQueueTaskResult) vars[0];
        String name = result.getName();
        String username = result.getUserName();
        String idcard = result.getIdcard();
        String phone = result.getPhone();
        String ip = result.getIp();
        String keyid = result.getKeyid();
        int fkId = result.getFkId();
        // 2.
        ReportData reportData1 = this.reportDataDAO.getReportData(fkId, 1);
        String home_tel = result.getHome_tel();
        String home_addr = result.getHome_addr();
        String work_tel = result.getWork_tel();
        String couple_phone_num = result.getCouple_phone_num();
        String contact_list = result.getContact_list();
        String url = this.variables.getJuxinliVerifyUrl();
        String token = this.variables.getJuxinliAccessToken();
        String secret = this.variables.getJuxinliClientSecret();

        //3.
        VerifyContactList verifyContactList = new VerifyContactList();
        verifyContactList.setName(name);
        verifyContactList.setIdcard(idcard);
        verifyContactList.setPhone(phone);
        verifyContactList.setHome_tel(home_tel);
        verifyContactList.setHome_addr(home_addr);
        verifyContactList.setWork_tel(work_tel);
        verifyContactList.setCouple_phone_num(couple_phone_num);
        verifyContactList.setContact_list(contact_list);
        verifyContactList.setGrant_authorization_id(fkId);
        Map<String, String> urlVariables = new HashMap<>();
        this.verifyContactListDAO.addEntity(verifyContactList);

        //4.
        LogInfo logInfo = new LogInfo(username, null, null, new Date(),
            JuxinliVerifyJMSQueueListener.JUXINLI_VERIFY_TABLE_NAME);
        String conStr = "";
        try {
            conStr = URLEncoder.encode(contact_list, "utf-8");
        } catch (UnsupportedEncodingException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        StringBuilder params = new StringBuilder("?name=" + name + "&idcard="
            + idcard + "&phone=" + phone + "&client_secret=" + secret
            + "&access_token=" + token + "&home_addr=" + home_addr
            + "&contact_list=" + conStr);
        urlVariables.put("name", name);
        urlVariables.put("idcard", idcard);
        urlVariables.put("phone", phone);
        urlVariables.put("client_secret", secret);
        urlVariables.put("access_token", token);
        urlVariables.put("home_addr", home_addr);
        urlVariables.put("contact_list", conStr);
        if (StringUtils.isNotBlank(home_tel)) {
            params.append("&home_tel=");
            params.append(home_tel);
        }
        if (StringUtils.isNotBlank(work_tel)) {
            params.append("&work_tel=");
            params.append(work_tel);
        }
        if (StringUtils.isNotBlank(couple_phone_num)) {
            params.append("&couple_phone_num=");
            params.append(couple_phone_num);
        }
        String queryString = url + params.toString();
        logInfo.setIp(ip);
        logInfo.setQueryString(queryString);
        logInfo.setEndTime(new Date());
        logInfo.setOperationName(username);
        logInfo.setDataChannel(DataChannel.JUXINLI);
        logInfo.setRecordId(fkId);
        this.logInfoDao.addEntity(logInfo);
        String errorMessage = "";
        verifyContactList.setQueryTime(new Date());
        verifyContactList.setBackTime(new Date());
        queryString = queryString.replace("\"", "%22").replace("{", "%7b")
                .replace("}", "%7d");
        String data = "";
        try {
            data = this.restTemplate.getEntity(queryString);
            JuxinliVerifyJMSQueueListener.logger
                .info("entityStringBuilder:--------------->:" + data);
        } catch (Exception e1) {
            errorMessage = e1.getMessage();
            JuxinliVerifyJMSQueueListener.logger.error("访问第三方异常："
                + ObjectUtils.toString(e1));
            e1.printStackTrace();
        }
        if (StringUtils.isBlank(data)) {
            logInfo.setResultType(ResultType.FAILURE);
            logInfo.setErrorMessage(errorMessage);
            verifyContactList.setEnabled(false);
            verifyContactList
                .setErrorCode(AsyncCode.FAILURE_CONNECTION_REFUSED);
            verifyContactList.setBackTime(new Date());
            verifyContactList.setErrorMessage(errorMessage);
            JuxinliDataInfo juxinliReportData = new JuxinliDataInfo();
            juxinliReportData.setReportData1(reportData1);
            juxinliReportData.setErrrormsg("hermes出错或无法连接");
            this.sendMessage(juxinliReportData, username, reportData1.getId(),
                AsyncCode.FAILURE_CONNECTION_REFUSED);
            JuxinliVerifyJMSQueueListener.logger
                .error("hermes出错或无法连接： verifyContactList  "
                    + ObjectUtils.toString(verifyContactList));
        } else {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            VerifyBackInfo verifyBackInfo = gson.fromJson(data,
                VerifyBackInfo.class);
            if (verifyBackInfo.isSuccess()) {//接口调用成功,报告正在生成
                try {
                    Thread.sleep(60000 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //去查询报告2
                verifyContactList.setEnabled(true);
                this.grantAuthorizationService.sendToQueue(name, idcard, phone,
                    username, ip, 2, fkId, keyid, "", "", "", "", "",
                    JMSType.JU_XIN_LI);
            } else {
                verifyContactList.setEnabled(false);
                verifyContactList.setErrorCode(AsyncCode.RESPONSE_NO_DETAILS);
                verifyContactList.setErrorMessage(verifyBackInfo.getNote());
                JuxinliDataInfo juxinliReportData = new JuxinliDataInfo();
                juxinliReportData.setReportData1(reportData1);
                juxinliReportData.setErrrormsg(verifyBackInfo.getNote());
                this.sendMessage(juxinliReportData, username,
                    reportData1.getId(), AsyncCode.RESPONSE_NO_DETAILS);
            }
        }
    }

    private void sendMessage(JuxinliDataInfo resp, String systemId,
            int requestId, AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider
                    .packageResponse(resp, systemId,
                        DataChannelSubType.PENGYUAN_EDU, requestId, asyncCode);
            this.provider.sendTextMessage(messageBody, systemId,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
