package com.ucredit.hermes.third.jms.qianhai;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
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
import com.ucredit.hermes.dao.qianhai.QianhaiDAO;
import com.ucredit.hermes.dao.qianhai.QianhaiRecordsDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.qianhai.QianhaiBusiData;
import com.ucredit.hermes.model.qianhai.QianhaiHeader;
import com.ucredit.hermes.model.qianhai.QianhaiRecords;
import com.ucredit.hermes.model.qianhai.QianhaiResponseInfo;
import com.ucredit.hermes.model.qianhai.QianhaiSearchRecord;
import com.ucredit.hermes.utils.JmsUtils;
import com.ucredit.hermes.utils.qianhai.DataSecurityUtil;
import com.ucredit.hermes.utils.qianhai.MessageUtil;

@Component("qianhaiQueueJMSQueueListener")
@Transactional(rollbackFor = Exception.class)
public class QianhaiBlackJMSQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
            .getLogger(QianhaiBlackJMSQueueListener.class);
    @Autowired
    private Variables variables;
    @Autowired
    private RESTTemplate restTemplate;
    @Autowired
    private QianhaiDAO qianhaiDAO;
    @Autowired
    private LogInfoDAO logInfoDao;
    @Autowired
    private QianhaiRecordsDAO qianhaiRecordsDAO;
    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;

    @Override
    public void onMessage(Message message) {
        QianhaiSearchRecord qianhaiSearchRecord = new QianhaiSearchRecord();
        String systemId = "";
        String apply_id = "";
        try {
            message.getStringProperty(JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY);
            message.getJMSMessageID();
            Object[] vars = (Object[]) ((ObjectMessage) message).getObject();
            ThirdQueueTaskResult result = (ThirdQueueTaskResult) vars[0];
            //获取配置参数
            String sfUrl = this.variables.getQianhaiUrl();
            String orgCode = this.variables.getQianhaiOrgCode();
            String chnlId = this.variables.getQianhaiChnlId();
            String authCode = this.variables.getQianhaiAuthCode();
            String userPassword = this.variables.getQianhaiUserPassword();
            String transNo = "ucredit" + System.currentTimeMillis() + "";
            String batchNo = transNo;
            String seqNo = batchNo;
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date nowTime = new Date();
            String transDate = time.format(nowTime);
            String authDate = this.variables.getQianhaiAuthDate();
            String checkCode = this.variables.getQianhaiCheckCode();
            String cerPath = this.variables.getQianhaiPathCer();
            String jksPath = this.variables.getQianhaiPathJks();
            String userName = this.variables.getQianhaiUserName();
            String storeAlias = this.variables.getQianhaiJksAlias();
            String storePassword = this.variables.getQianhaiJksPassword();
            apply_id = result.getLendRequestId();
            String messageQianhai = "";
            int id = result.getFkId();
            String idNo = result.getIdcard();
            String idType = result.getSearchType();
            String name = result.getName();
            String reasonCode = result.getCode();
            systemId = result.getUserName();
            qianhaiSearchRecord = this.qianhaiDAO.getEntityByID(id);
            LogInfo logInfo = new LogInfo(systemId, null, result.getIdcard(),
                new Date(), "qianhai_search_info");
            logInfo.setTableName("qianhai_search_info");
            logInfo.setDataChannel(DataChannel.QIANHAI);
            logInfo.setCreateTime(new Date());
            logInfo.setLendRequestId(apply_id);
            logInfo.setEndTime(new Date());
            this.logInfoDao.addEntity(logInfo);
            try {//组装查询条件
                byte[] oriByte = MessageUtil.getBusiData_MSC8004(batchNo,
                    reasonCode, idNo, idType, name, seqNo).getBytes();
                String encBusiData = "";
                String header = "\"header\":"
                        + MessageUtil.getMHeader_DMZ(orgCode, chnlId, transNo,
                            transDate, authCode, authDate);
                encBusiData = DataSecurityUtil.encrypt(oriByte, checkCode);
                String busiData = "\"busiData\":\"" + encBusiData + "\"";
                String sigValue = DataSecurityUtil.signData(encBusiData,
                    jksPath, storeAlias, storePassword);
                String pwd = DataSecurityUtil.digest(userPassword.getBytes());
                String securityInfo = "\"securityInfo\":"
                        + MessageUtil.getSecurityInfo(sigValue, pwd, userName);
                messageQianhai = "{" + header + "," + busiData + ","
                        + securityInfo + "}";
                System.out.println("请求：" + messageQianhai);
            } catch (Exception e) {
                logInfo.setErrorMessage("组装查询条件失败");
                qianhaiSearchRecord.setErrorMessage("组装查询条件失败");
                qianhaiSearchRecord.setResultType(ResultType.FAILURE);
                qianhaiSearchRecord
                    .setErrorCode(AsyncCode.FAILURE_HERMES_ERROR_PARSE);
                this.sendMessage(qianhaiSearchRecord, systemId, apply_id,
                    AsyncCode.FAILURE_HERMES_ERROR_PARSE);
                QianhaiBlackJMSQueueListener.logger
                .info("---------------组装查询条件失败---------userName："
                        + userName);
                e.printStackTrace();
                this.qianhaiDAO.updateEntity(qianhaiSearchRecord);
                return;

            }
            JSONObject msgJSON = null;
            String res = "";
            try {
                if (!"".equals(message)) {//发起查询
                    res = this.restTemplate.sendJsonWithHttps(sfUrl,
                        messageQianhai);
                    qianhaiSearchRecord.setBackTime(new Date());
                    msgJSON = JSONObject.fromObject(res);
                }
            } catch (Exception e) {
                logInfo.setResultType(ResultType.FAILURE);
                logInfo.setErrorMessage("发起前海查询失败");
                qianhaiSearchRecord.setErrorMessage("发起前海查询失败");
                qianhaiSearchRecord.setResultType(ResultType.FAILURE);
                qianhaiSearchRecord
                    .setErrorCode(AsyncCode.FAILURE_CONNECTION_REFUSED);
                this.sendMessage(qianhaiSearchRecord, systemId, apply_id,
                    AsyncCode.FAILURE_CONNECTION_REFUSED);
                QianhaiBlackJMSQueueListener.logger
                .info("---------------发起前海查询失败---------message：" + message);
                e.printStackTrace();
                this.qianhaiDAO.updateEntity(qianhaiSearchRecord);
                return;
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            if (!StringUtils.isEmpty(res)) {
                logInfo.setData(res);
                logInfo.setEndTime(new Date());
                QianhaiResponseInfo reportInfo = gson.fromJson(res,
                    QianhaiResponseInfo.class);
                QianhaiHeader header = reportInfo.getHeader();
                qianhaiSearchRecord.setRtCode(header.getRtCode());
                qianhaiSearchRecord.setRtMsg(header.getRtMsg());
                qianhaiSearchRecord.setTransDate(header.getTransDate());
                qianhaiSearchRecord.setOriMessage(reportInfo.getOriMessage());
                qianhaiSearchRecord.setSignatureValue(reportInfo
                    .getSecurityInfo().getSignatureValue());
            }
            String busiData = "";
            try {
                // 验证签名的合法性
                if (msgJSON != null) {
                    DataSecurityUtil.verifyData(
                        msgJSON.getString("busiData"),
                        msgJSON.getJSONObject("securityInfo").getString(
                                "signatureValue"), cerPath);
                    qianhaiSearchRecord.setResultType(ResultType.SUCCESS);
                    logInfo.setResultType(ResultType.SUCCESS);
                    busiData = DataSecurityUtil.decrypt(
                        msgJSON.getString("busiData"), checkCode);
                    System.out.println("响应BusiData明文：" + busiData);
                }

            } catch (Exception e) {
                logInfo.setResultType(ResultType.FAILURE);
                logInfo.setErrorMessage("验证签名发生异常");
                qianhaiSearchRecord.setResultType(ResultType.FAILURE);
                qianhaiSearchRecord.setErrorMessage("验证签名发生异常");
                qianhaiSearchRecord.setErrorCode(AsyncCode.SING_ERROR);
                this.sendMessage(qianhaiSearchRecord, systemId, apply_id,
                    AsyncCode.SING_ERROR);
                QianhaiBlackJMSQueueListener.logger
                .info("---------------验证签名发生异常---------msgJSON：" + msgJSON);
                e.printStackTrace();
                this.qianhaiDAO.updateEntity(qianhaiSearchRecord);
                return;
            }

            QianhaiBusiData qianhaiBusiData = gson.fromJson(busiData,
                QianhaiBusiData.class);
            QianhaiBlackJMSQueueListener.logger
            .info("---------------qianhaiBusiData："
                    + qianhaiBusiData.getBatchNo());
            qianhaiSearchRecord.setBatchNo(qianhaiBusiData.getBatchNo());

            List<QianhaiRecords> records = qianhaiBusiData.getRecords();
            for (QianhaiRecords r : records) {
                r.setSearch_record_id(id);
                this.qianhaiRecordsDAO.addEntity(r);
            }
            QianhaiBlackJMSQueueListener.logger.info("---------------records："
                    + records.size());
            qianhaiSearchRecord.setRecords(records);
            QianhaiBlackJMSQueueListener.logger
                .info("----------------前海查询发送mq");
            this.sendMessage(qianhaiSearchRecord, systemId, apply_id,
                AsyncCode.SUCCESS);
            QianhaiBlackJMSQueueListener.logger
            .info("----------------前海查询成功发送mq：");
        } catch (Exception e) {
            qianhaiSearchRecord.setResultType(ResultType.FAILURE);
            qianhaiSearchRecord.setErrorMessage("hermes程序错误");
            qianhaiSearchRecord.setEnabled(false);
            qianhaiSearchRecord
            .setErrorCode(AsyncCode.FAILURE_HERMES_ERROR_PARSE);
            this.sendMessage(qianhaiSearchRecord, systemId, apply_id,
                AsyncCode.FAILURE_HERMES_ERROR_PARSE);
            e.printStackTrace();
            this.qianhaiDAO.updateEntity(qianhaiSearchRecord);
            return;
        }

    }

    private void sendMessage(QianhaiSearchRecord resp, String systemId,
            String requestId, AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider
                .packageResponse(resp, systemId,
                    DataChannelSubType.QIANHAIBLACK, requestId, asyncCode);
            QianhaiBlackJMSQueueListener.logger.info("发送的信息：" + messageBody);
            this.provider.sendTextMessage(messageBody, systemId,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
