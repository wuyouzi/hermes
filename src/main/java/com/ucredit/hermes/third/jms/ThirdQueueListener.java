/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.third.jms;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.dao.CompanyInfoDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.QueryType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.Conditions;
import com.ucredit.hermes.model.Conditions.Condition;
import com.ucredit.hermes.model.Conditions.Item;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.UserGroup;
import com.ucredit.hermes.model.pengyuan.CompanyInfo;
import com.ucredit.hermes.service.CompanyInfoBatchService;
import com.ucredit.hermes.service.ThirdQueueService;
import com.ucredit.hermes.utils.DecodeZipUtils;
import com.ucredit.hermes.utils.JmsUtils;

/**
 * @author caoming edit by xubaoyong 2014-11-28
 */
@Component("thirdQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class ThirdQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
            .getLogger(ThirdQueueListener.class);
    @Autowired
    private CompanyInfoDAO companyInfoDao;
    @Autowired
    private LogInfoDAO logInfoDao;
    @Autowired
    private CompanyInfoBatchService companyInfoBatchService;
    @Autowired
    private ThirdPengYuanGenerator thirdPengYuanGenerator;

    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;
    @Autowired
    private ThirdQueueService thirdQueueService;

    /**
     * 组织查询编码
     *
     * @param companyName
     *        被查询企业名称
     * @param orgCode
     *        被查询企业组织机构代码
     * @param registerNo
     *        被查询企业工商注册号
     * @param refId
     *        引用ID,用于唯一的识别本批次查询中的该查询流水号，鹏元将该refID原样输出到xml结果中
     * @return String
     * @throws Exception
     */
    public static String buildConditionsXML(String companyName, String orgCode,
            String registerNo, String refId, String reportType)
            throws Exception {
        if (StringUtils.isEmpty(refId)) {
            throw new Exception("refId不能为空");
        }
        Conditions conditions = new Conditions();

        Condition condition = new Condition();
        condition.setQueryType(ThirdPengYuanGenerator.COMPANY_QUERY_TYPE);

        List<Item> items = new LinkedList<>();
        if (StringUtils.isNotEmpty(companyName)) {
            Item companyNameitem = new Item();
            companyNameitem.setName("corpName");
            companyNameitem.setValue(companyName);
            items.add(companyNameitem);
        }

        if (StringUtils.isNotEmpty(registerNo)) {
            Item registerNoitem = new Item();
            registerNoitem.setName("registerNo");
            registerNoitem.setValue(registerNo);
            items.add(registerNoitem);
        }
        if (StringUtils.isNotEmpty(orgCode)) {
            Item orgCodeitem = new Item();
            orgCodeitem.setName("orgCode");
            orgCodeitem.setValue(orgCode);
            items.add(orgCodeitem);
        }

        // 鹏元查询类型
        Item subreportIDsitem = new Item();
        subreportIDsitem.setName("subreportIDs");
        if ("90035".equals(reportType)) {
            subreportIDsitem.setValue("90035");
        } else {
            subreportIDsitem.setValue("95001,90035");
        }
        items.add(subreportIDsitem);

        // 查询原因
        Item queryReasonItem = new Item();
        queryReasonItem.setName(ThirdPengYuanGenerator.QUERY_REASON_ID);
        queryReasonItem.setValue(HermesConsts.QUERY_REASON_TYPE);
        items.add(queryReasonItem);

        // 引用ＩＤ
        Item refIDitem = new Item();
        refIDitem.setName(ThirdPengYuanGenerator.REF_IDS);
        refIDitem.setValue(refId);
        items.add(refIDitem);

        condition.setItem(items);
        conditions.setCondition(condition);
        return conditions.toSearchString();
    }

    /**
     * 约定 1：所有的查询都要通过refID进行关联
     * 2：查询前所有的通过companyName、orgCode、registerNo能查到的企业都要设置enable=false
     * 3:refID生成规则按照原来的逻辑即：ThirdGenerator.getBatchNumber()方法
     */
    @Override
    public void onMessage(Message message) {

        try {
            String systemId = message
                    .getStringProperty(JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY);
            String messageId = message.getJMSMessageID();
            Object[] vars = (Object[]) ((ObjectMessage) message).getObject();
            int companyid = (int) vars[0];
            ThirdQueueListener.logger.debug("系统: " + systemId
                + " 发送请求， 消息ID为: " + messageId);
            String operationName = (String) vars[1];
            UserGroup userGroup = (UserGroup) vars[2];
            String ip = (String) vars[3];
            String lendRequestId = (String) vars[4];
            String reportType = "";
            int num = 0;
            if (vars.length == 6) {
                if ("report90035".equals(vars[5])) {
                    reportType = "report90035";
                } else {
                    num = (int) vars[5];
                }
            }
            CompanyInfo dbcompanyInfo = this.companyInfoDao
                    .getEntityByDbId(companyid);
            if (dbcompanyInfo == null) {
                throw new Exception(String.format("根据id=%d没有查询到公司", companyid));
            }

            String refId = dbcompanyInfo.getRefId();
            if (StringUtils.isEmpty(refId) && !"report90035".equals(vars[5])) {
                throw new Exception("refId 不能为空");
            } else {
                refId = ThirdGenerator.getBatchNumber(QueryType.CHECK_COMPANY,
                    DataChannel.PENGYUAN);
                dbcompanyInfo.setRefId(refId);
            }
            String companyName = dbcompanyInfo.getCompanyName();
            String orgCode = vars.length == 6 ? null : dbcompanyInfo
                .getOrgCode();
            String registerNo = dbcompanyInfo.getRegisterNo();

            Date now = new Date();
            //只查询90035屏蔽 String queryStringV2 = ThirdQueueListener.buildConditionsXML(
            //   companyName, orgCode, registerNo, refId, "all");
            String queryStringV2 = ThirdQueueListener.buildConditionsXML(
                companyName, orgCode, registerNo, refId, "90035");
            //爬取平台能够爬取到其他信息，只发起90035的查询
            if ("report90035".equals(reportType)) {
                queryStringV2 = ThirdQueueListener.buildConditionsXML(
                    companyName, orgCode, registerNo, refId, "90035");
            }
            LogInfo logInfo = new LogInfo(operationName, userGroup, ip, now,
                CompanyInfoBatchService.COMPANYINFO);
            logInfo.setTableName(CompanyInfoBatchService.COMPANYINFO);
            logInfo.setDataChannel(DataChannel.PENGYUAN);
            logInfo.setQueryString(queryStringV2);
            logInfo.setCreateTime(now);
            logInfo.setLendRequestId(lendRequestId);
            String data = null;
            String errorMsg = "";
            try {
                // 设置查询时间
                dbcompanyInfo.setQueryTime(now);
                data = this.thirdPengYuanGenerator.sendXML(queryStringV2);

            } catch (Exception e) {
                errorMsg += e.getMessage();
            }

            if (StringUtils.isBlank(data)) {
                errorMsg = AsyncCode.FAILURE_CONNECTION_REFUSED + "_"
                        + HermesConsts.HERMES_RESPONSE_ERROR + errorMsg;
                dbcompanyInfo.setErrorMessage(errorMsg);
                dbcompanyInfo.setResultType(ResultType.FAILURE);
                dbcompanyInfo.setLastUpdateTime(new Date());
                dbcompanyInfo.setEnabled(false);
                this.companyInfoDao.updateEntity(dbcompanyInfo);

                // 写log日志
                logInfo.setResultType(ResultType.FAILURE);
                logInfo.setRecordId(dbcompanyInfo.getId());
                logInfo.setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR);
                logInfo.setEndTime(now);
                logInfo.setLastUpdateTime(new Date());
                this.logInfoDao.addEntity(logInfo);

                // ray - send blank data message
                this.sendMessage(dbcompanyInfo, systemId,
                    dbcompanyInfo.getKeyid(), AsyncCode.FAILURE_EMPTYDATA);

            } else {
                // 解析获取数据到companyInfo，并入库
                dbcompanyInfo.setData(data);
                try {
                    if ("report90035".equals(reportType)) {
                        this.setCompanyInfo(data, dbcompanyInfo, logInfo, now,
                            systemId, num, "report90035");
                    } else {
// 只查询90035屏蔽                       this.setCompanyInfo(data, dbcompanyInfo, logInfo, now,
//                            systemId, num, "all");
                        this.setCompanyInfo(data, dbcompanyInfo, logInfo, now,
                            systemId, num, "report90035");
                    }
                } catch (Exception e) {
                    dbcompanyInfo.setData(data);
                    dbcompanyInfo.setErrorMessage(errorMsg);
                    dbcompanyInfo.setResultType(ResultType.FAILURE);
                    dbcompanyInfo.setLastUpdateTime(new Date());
                    e.printStackTrace();

                    // ray - send error message
                    this.sendMessage(dbcompanyInfo, systemId,
                        dbcompanyInfo.getKeyid(), AsyncCode.FAILURE_UNEXCEPTED);
                }

            }
        } catch (Exception ex) {
            ThirdQueueListener.logger.error(ex.getMessage());

            ex.printStackTrace();
        }

    }

    private void setCompanyInfo(String pengyuanReturnString,
            CompanyInfo dbCompany, LogInfo logInfo, Date now, String systemId,
            int num, String reportType) throws Exception {

        Element rootElement = DocumentHelper.parseText(pengyuanReturnString)
                .getRootElement();

        String status = rootElement.elementText("status");

        if ("1".equals(status)) {// 返回成功
            // 解密解压缩
            String returnVaule = DecodeZipUtils.decodeZip(rootElement
                .elementText("returnValue"));

            /**
             * 记录返回内容
             */
            dbCompany.setData(returnVaule);

            ThirdQueueListener.logger.debug("返回的xml为：" + returnVaule);

            Document document = DocumentHelper.parseText(returnVaule);
            Element root = document.getRootElement();

            // 主表id
            int id = dbCompany.getId();
            // 企业报告
            List<Element> cisReportList = root.elements("cisReport");
            // 约定每次只返回一个企业信息
            Element cisReport = cisReportList.get(0);
            //获取返回是否发生异常
            String hasSystemError = cisReport.attributeValue("hasSystemError");
            dbCompany.setHasSystemError(hasSystemError);
            //检测出发生异常，并且查询次数小于3次重新发起查询
            if ("true".equals(hasSystemError) && num < 2) {
                ThirdQueueListener.logger
                .info("鹏元企业hasSystemError为true重新发起查询，companyId=" + id);
                logInfo.setResultType(ResultType.FAILURE);
                logInfo.setData(returnVaule);
                logInfo.setErrorCode("06");
                logInfo.setEndTime(new Date());
                logInfo.setRecordId(id);
                logInfo.setLastUpdateTime(new Date());
                logInfo.setErrorMessage(HermesConsts.COMPANY_SYSTEM_ERROR);
                logInfo = this.companyInfoBatchService.logEntity(document,
                    logInfo, now);
                this.thirdQueueService.sendMessageV2(id,
                    logInfo.getOperationName(), logInfo.getGroup(),
                    logInfo.getIp(), dbCompany.getLendRequestId(), num + 1,
                    systemId);

            } else {
                String treatResultString = cisReport
                        .attributeValue("treatResult");
                boolean treatResultBoolean = treatResultString.contains("1");// 是否有返回内容
                if (treatResultBoolean) {// 有返回内容
                    logInfo.setResultType(ResultType.SUCCESS);

                    dbCompany.setResultType(ResultType.SUCCESS);
                    List<String> allList = new LinkedList<>();
                    Set<String> feeReportSet = new HashSet<>();
                    Set<String> subReportSet = new HashSet<>();
                    // 设置主表信息
                    this.companyInfoBatchService.setCompanyInfo(dbCompany,
                        cisReport, allList, feeReportSet, subReportSet);
                    // 记录log日志 PengYuanReportRecord
                    /*
                     * 只查询90035屏蔽
                     * this.companyInfoBatchService.addPengYuanReportRecord(
                     * cisReport, "corpBaseNationalInfo",
                     * dbCompany.getRefId(), now);
                     */

                    // 设置子表信息，并添加到数据库
                    if ("report90035".equals(reportType)) {//只解析90035报告
                        allList = this.companyInfoBatchService
                                .addCompanyInfoSubList1(dbCompany, allList,
                                    cisReport, true, now, feeReportSet,
                                    subReportSet);
                    } else {//解析所有报告
// 只查询90035屏蔽                       allList = this.companyInfoBatchService
//                                .addCompanyInfoSubList(dbCompany, allList,
//                                cisReport, true, now, feeReportSet,
//                                subReportSet);
                        allList = this.companyInfoBatchService
                            .addCompanyInfoSubList1(dbCompany, allList,
                                cisReport, true, now, feeReportSet,
                                subReportSet);
                    }
                    StringBuilder feeReport = new StringBuilder();
                    StringBuilder subReport = new StringBuilder();
                    for (String str : feeReportSet) {
                        feeReport.append(str);
                        feeReport.append(',');
                    }
                    for (String str : subReportSet) {
                        subReport.append(str);
                        subReport.append(',');
                    }
                    dbCompany.setLastUpdateTime(new Date());
                    dbCompany.setEnabled(true);
                    dbCompany.setResultType(ResultType.SUCCESS);
                    if (feeReport.length() > 0) {
                        dbCompany.setFeeReport(feeReport.toString().substring(
                            0, feeReport.length() - 1));
                    }
                    if (subReport.length() > 0) {
                        dbCompany.setSubReport(subReport.toString().substring(
                            0, subReport.length() - 1));
                    }

                    this.companyInfoDao.updateEntity(dbCompany);
                    //TODO
                    // ray send valid message
                    ThirdQueueListener.logger
                    .info("-------------------开始往队列里发鹏元返回的信息---工商id："
                            + dbCompany.getId() + "systemId:" + systemId);
                    Thread.sleep(2000);
                    this.sendMessage(dbCompany, systemId, dbCompany.getKeyid(),
                        AsyncCode.SUCCESS);
                    ThirdQueueListener.logger
                    .info("-------------------结束往队列里发鹏元返回的信息---工商");
                } else {
                    // 无返回内容
                    // log
                    logInfo.setResultType(ResultType.FAILURE);
                    // dbCompany
                    dbCompany.setLastUpdateTime(new Date());
                    dbCompany.setResultType(ResultType.FAILURE);
                    ThirdQueueListener.logger
                    .info("-------------------开始往队列里发鹏元返回的信息(未查到)---学历id："
                            + dbCompany.getId());
                    dbCompany.setErrorMessage("未查到此企业的内容，请确认后再次查询！");
                    ThirdQueueListener.logger
                    .info("-------------------结束往队列里发鹏元返回的信息(未查到)---学历id："
                            + dbCompany.getId());
                    this.companyInfoDao.updateEntity(dbCompany);
                    // ray send valid message
                    this.sendMessage(dbCompany, systemId, dbCompany.getKeyid(),
                        AsyncCode.FAILURE_EMPTYDATA);

                }

                // 将返回结果及企业ID记录到log中
                logInfo.setRecordId(id);
                logInfo.setData(returnVaule);
                logInfo = this.companyInfoBatchService.logEntity(document,
                    logInfo, now);
            }
        } else {// 返回失败

            String errorCode = rootElement.elementText("errorCode");
            String errorMessage = rootElement.elementText("errorMessage");
            logInfo.setErrorCode(errorCode);
            logInfo.setErrorMessage(errorMessage);
            logInfo.setEndTime(now);
            logInfo.setLastUpdateTime(new Date());
            logInfo.setResultType(ResultType.FAILURE);

            dbCompany.setErrorMessage(errorMessage);
            dbCompany.setResultType(ResultType.FAILURE);
            dbCompany.setLastUpdateTime(new Date());
            this.companyInfoDao.updateEntity(dbCompany);

            // ray send failed message
            this.sendMessage(dbCompany, systemId, dbCompany.getKeyid(),
                AsyncCode.FAILURE_3RD_RETURN_ERROR);
        }
        this.logInfoDao.addEntity(logInfo);
        ThirdQueueListener.logger
        .debug("LogInfo <" + logInfo.getId() + "> add");
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
