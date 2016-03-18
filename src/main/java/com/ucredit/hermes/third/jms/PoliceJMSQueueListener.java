/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.third.jms;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.dao.HermesCountDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.PoliceResidenceInfoDAO;
import com.ucredit.hermes.dao.SubReportInfoDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.ReportType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.enums.TableType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.pengyuan.BaseReportType;
import com.ucredit.hermes.model.pengyuan.HermesCount;
import com.ucredit.hermes.model.pengyuan.PoliceResidenceInfo;
import com.ucredit.hermes.service.CompanyInfoBatchService;
import com.ucredit.hermes.utils.DecodeZipUtils;
import com.ucredit.hermes.utils.JmsUtils;
import com.ucredit.hermes.utils.ParseXmlUtils;

/**
 * 专线个人户籍信息
 *
 * @author caoming
 */
@Component("policeJMSQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class PoliceJMSQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
        .getLogger(PoliceJMSQueueListener.class);
    @Autowired
    private PoliceResidenceInfoDAO dao;
    @Autowired
    private LogInfoDAO logInfoDAO;
    @Autowired
    private JmsFactory factory;
    @Autowired
    private SubReportInfoDAO subReportInfoDAO;
    @Autowired
    private CompanyInfoBatchService companyInfoBatchService;
    @Autowired
    private HermesCountDAO hermesCountDAO;

    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;

    @Override
    public void onMessage(Message message) {
        PoliceJMSQueueListener.logger.debug("JMS执行相应代码...");
        try {
            String systemId = message
                .getStringProperty(JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY);
            String messageId = message.getJMSMessageID();
            PoliceJMSQueueListener.logger.debug("接收到消息Id: " + messageId
                + ", 消息StringProperty为: " + systemId);
            message.getStringProperty(JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY);
            Object[] vars = (Object[]) ((ObjectMessage) message).getObject();
            ThirdQueueTaskResult result = (ThirdQueueTaskResult) vars[0];
            DataChannel type = (DataChannel) vars[1];
            LogInfo logInfo = (LogInfo) vars[2];
            logInfo.setDataChannel(type);
            ThirdGenerator thirdGenerator = this.factory.createFactory(type);
            String queryString = thirdGenerator.buildConditionsXML(result);
            PoliceJMSQueueListener.logger
                .debug("鹏元专线查询个人户籍的xml：" + queryString);
            logInfo.setQueryString(queryString);
            PoliceResidenceInfo policeInfo = this.dao.getPoliceResidenceInfo(
                result.getName(), result.getNumber());
            BaseReportType baseReportType = policeInfo.getBaseReportType();
            baseReportType.setDataChannel(DataChannel.PENGYUAN);
            String data = "";
            try {
                data = thirdGenerator.sendXML(queryString);
                PoliceJMSQueueListener.logger.debug("返回的数据为：" + data);
                if (StringUtils.isBlank(data)) {
                    // BaseReportType baseReportType = policeInfo
                    // .getBaseReportType();
                    baseReportType.setErrorMessage("系统返回错误，请重试！");
                    throw new Exception("三方返回数据为空，请重试......");
                    // ray - no details message would be sent in catch below
                }
                this.setPoliceResidenceInfo(data, policeInfo, logInfo, systemId);
            } catch (Exception ex) {
                logInfo.setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR);
                logInfo.setResultType(ResultType.FAILURE);
                logInfo.setEndTime(new Date());
                logInfo.setRecordId(policeInfo.getId());
                this.logInfoDAO.addEntity(logInfo);
                // BaseReportType baseReportType =
                // policeInfo.getBaseReportType();
                baseReportType.setResultType(ResultType.FAILURE);
                baseReportType.setCreateTime(new Date());
                baseReportType
                    .setTreatErrorCode(HermesConsts.HERMES_RESPONSE_ERROR);
                baseReportType
                    .setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR + " "
                        + ex.getMessage());
                this.dao.updateEntity(policeInfo);

                // ray - send no value message
                policeInfo.setBaseReportType(baseReportType);
                this.sendMessage(policeInfo, systemId, policeInfo.getId(),
                    AsyncCode.FAILURE);

                PoliceJMSQueueListener.logger.error(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception e) {
            PoliceJMSQueueListener.logger.error("JMS监听器启动失败 - "
                + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 解析xml
     *
     * @param data
     * @param policeInfo
     * @param logInfo
     * @throws DocumentException
     * @throws ParseException
     */
    private void setPoliceResidenceInfo(String data,
            PoliceResidenceInfo policeInfo, LogInfo logInfo, String systemId)
            throws DocumentException, ParseException {
        Document document = DocumentHelper.parseText(data);
        Element rootElement = document.getRootElement();
        String status = rootElement.elementText("status");
        BaseReportType baseReportType = policeInfo.getBaseReportType();

        // 解密解压缩
        String returnVaule = DecodeZipUtils.decodeZip(rootElement
            .elementText("returnValue"));

        PoliceJMSQueueListener.logger.debug("返回的xml为：" + returnVaule);

        Document returnDocument = DocumentHelper.parseText(returnVaule);
        Element root = returnDocument.getRootElement();

        if ("1".equals(status)) {// 返回成功

            // 主表id
            int id = policeInfo.getId();

            // 第一级节点
            List<Element> elements = root.elements("cisReport");
            for (Element e : elements) {
                Element policeElement = e.element("policeInfo");
                boolean isSuccess = policeElement.attributeValue("treatResult")
                    .contains("1");
                // 解析头信息
                ParseXmlUtils.parseXmlHead(policeElement, baseReportType);

                // TODO {计费}检查， 目前看起来像是只有成功才记录，实际为查询则记录
                if (isSuccess) {// 返回有内容

                    logInfo.setResultType(ResultType.SUCCESS);

                    baseReportType.setResultType(ResultType.SUCCESS);
                    baseReportType.setDataChannel(DataChannel.PENGYUAN);

                    // 子报告
                    baseReportType.setSubReportType(this.subReportInfoDAO
                        .getSubReportByCode(
                            policeElement.attributeValue("subReportType"),
                            ReportType.SPECIAL_REPORT));

                    PoliceJMSQueueListener.parseXMLSetPoliceResidenceInfo(
                        policeElement.element("item"), policeInfo);

                    // md5值的设置
                    String newMd5 = DigestUtils.md5Hex(policeInfo.toString());
                    String oldMd5 = baseReportType.getMd5();

                    if (StringUtils.isBlank(oldMd5) || newMd5.equals(oldMd5)) {// md5值相等或为空

                        if (StringUtils.isBlank(oldMd5)
                            || com.ucredit.hermes.utils.DateUtils.isDateBefore(
                                baseReportType.getCreateTime(), 1)) {
                            // 解析鹏元记录收费子报告次数，添加到数据库
                            this.companyInfoBatchService.addPengYuanCount(
                                root.element("costCount"), id);

                            // hermes自己记录收费次数
                            HermesCount hermesCount = new HermesCount(id, 1,
                                TableType.POLICE_RESIDENCE_INFOS,
                                DataChannel.PENGYUAN);
                            this.hermesCountDAO.addEntity(hermesCount);
                        }

                        policeInfo.setId(id);
                        baseReportType.setMd5(newMd5);
                        baseReportType.setEnabled(true);
                        baseReportType.setCreateTime(new Date());
                        policeInfo.setBaseReportType(baseReportType);
                        id = this.dao.updateEntity(policeInfo).getId();

                        // ray - send valid message
                        this.sendMessage(policeInfo, systemId,
                            policeInfo.getId(), AsyncCode.SUCCESS);

                    } else if (!newMd5.equals(oldMd5)) {// md5值不相等
                        PoliceResidenceInfo newPoliceInfo = new PoliceResidenceInfo();
                        BeanUtils.copyProperties(policeInfo, newPoliceInfo);

                        newPoliceInfo.setId(null);
                        BaseReportType newBaseReportType = new BaseReportType();
                        BeanUtils.copyProperties(baseReportType,
                            newBaseReportType);
                        newBaseReportType.setMd5(newMd5);
                        newBaseReportType.setEnabled(true);
                        newBaseReportType.setCreateTime(new Date());
                        newPoliceInfo.setBaseReportType(newBaseReportType);
                        policeInfo.setId(id);

                        id = this.dao.addEntity(newPoliceInfo);

                        // 解析鹏元记录收费子报告次数，添加到数据库
                        this.companyInfoBatchService.addPengYuanCount(
                            root.element("costCount"), id);

                        // hermes自己记录收费次数
                        HermesCount hermesCount = new HermesCount(id, 1,
                            TableType.POLICE_RESIDENCE_INFOS,
                            DataChannel.PENGYUAN);
                        this.hermesCountDAO.addEntity(hermesCount);

                        PoliceJMSQueueListener.logger
                            .debug("PoliceResidenceInfo <" + id + "> add");

                        // 之前的数据置为无效
                        baseReportType.setEnabled(false);
                        baseReportType.setMd5(oldMd5);
                        policeInfo.setBaseReportType(baseReportType);
                        this.dao.updateEntity(policeInfo);

                        // ray send new valid info
                        /*
                         * 虽然为新的entity，但是客户端方并未知晓新的id号，所
                         * 以如果用REST返回的entityID作为查询记录id的话，需要返回旧的id号以便对照
                         */
                        this.sendMessage(newPoliceInfo, systemId,
                            policeInfo.getId(), AsyncCode.SUCCESS);
                    }
                } else {// 返回无内容

                    logInfo.setResultType(ResultType.FAILURE);
                    baseReportType.setResultType(ResultType.FAILURE);
                    baseReportType.setCreateTime(new Date());
                    baseReportType.setErrorMessage("未查到此企业的内容，请确认后再次查询！");
                    id = this.dao.updateEntity(policeInfo).getId();

                    // ray - send no value message
                    policeInfo.setBaseReportType(baseReportType);
                    this.sendMessage(policeInfo, systemId, policeInfo.getId(),
                        AsyncCode.FAILURE_EMPTYDATA);

                }
            }
            logInfo.setRecordId(id);
            ParseXmlUtils.parseXmlSetLogInfo(root, logInfo);
        } else {// 返回失败

            // TODO 不确定{计费} by ray
            logInfo.setErrorCode(baseReportType.getTreatErrorCode());
            logInfo.setErrorMessage(baseReportType.getErrorMessage());
            logInfo.setEndTime(new Date());
            logInfo.setResultType(ResultType.FAILURE);

            baseReportType.setResultType(ResultType.FAILURE);
            baseReportType.setCreateTime(new Date());
            baseReportType.setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR);
            policeInfo.setBaseReportType(baseReportType);

            // ray - send failure message
            this.sendMessage(policeInfo, systemId, policeInfo.getId(),
                AsyncCode.FAILURE_HERMES_ERROR);
        }

        logInfo.setData(returnVaule);
        this.logInfoDAO.addEntity(logInfo);
        PoliceJMSQueueListener.logger.debug("LogInfo <" + logInfo.getId()
            + "> add");
    }

    /**
     * 设置个人户籍信息
     *
     * @param element
     * @param policeInfo
     * @throws ParseException
     */
    public static void parseXMLSetPoliceResidenceInfo(Element element,
            PoliceResidenceInfo policeInfo) throws ParseException {
        policeInfo.setName(element.elementText("name"));
        policeInfo.setDocumentNo(element.elementText("documentNo"));
        policeInfo.setGender(ParseXmlUtils.parseXMLToGender(element
            .elementText("gender")));
        Date birthday = DateUtils.parseDate(element.elementText("birthday"),
            "yyyyMMdd");
        policeInfo.setBirthday(birthday);
        policeInfo.setHistoryName(element.elementText("historyName"));
        policeInfo.setRegisterAddress(element.elementText("registerAddress"));
        policeInfo.setPoliceStation(element.elementText("policeStation"));
        policeInfo.setEducationLevel(element.elementText("educationLevel"));
        policeInfo.setResidentAddress(element.elementText("residentAddress"));
        policeInfo.setNation(element.elementText("nation"));
        if (StringUtils.isNotBlank(element.elementText("marriageStatus"))) {
            policeInfo.setMarryStatus(ParseXmlUtils
                .parseXMLToMarryStatus(element.elementText("marriageStatus")));
        }
        policeInfo.setPhoto(element.elementText("photo"));
        policeInfo.setInfoUtil(Integer.valueOf(StringUtils.isBlank(element
            .elementText("infoUnit")) ? "0" : element.elementText("infoUnit")));
    }

    private void sendMessage(PoliceResidenceInfo resp, String systemId,
            int requestId, AsyncCode asyncCode) {
        this.sendMessage(resp, systemId, this.hermesPublishQueue, requestId,
            asyncCode);
    }

    private void sendMessage(PoliceResidenceInfo resp, String systemId,
            Destination destination, int requestId, AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider.packageResponse(resp,
                systemId, DataChannelSubType.PENGYUAN_POLICE_RES, requestId,
                asyncCode);
            this.provider.sendTextMessage(messageBody, systemId, destination);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
