/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.third.jms;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
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
import com.ucredit.hermes.dao.AreaCodeDAO;
import com.ucredit.hermes.dao.EconomicTypeInfoDAO;
import com.ucredit.hermes.dao.GdSiCorpInfoDAO;
import com.ucredit.hermes.dao.GdSiPersonInfoDAO;
import com.ucredit.hermes.dao.HermesCountDAO;
import com.ucredit.hermes.dao.HistoryQueryInfoDAO;
import com.ucredit.hermes.dao.HistorySiPersonInfoDAO;
import com.ucredit.hermes.dao.IdentityVerifyInfoDAO;
import com.ucredit.hermes.dao.InformationSourceUnitTypeDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.PoliceResidenceInfoDAO;
import com.ucredit.hermes.dao.QueryReasonDAO;
import com.ucredit.hermes.dao.SpecialInfoDAO;
import com.ucredit.hermes.dao.StopReasonDAO;
import com.ucredit.hermes.dao.SubReportInfoDAO;
import com.ucredit.hermes.dao.TradeInfoDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.ReportType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.enums.TableType;
import com.ucredit.hermes.enums.TreatResult;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.pengyuan.BaseHeadReport;
import com.ucredit.hermes.model.pengyuan.BaseReportType;
import com.ucredit.hermes.model.pengyuan.GdSiCorpInfo;
import com.ucredit.hermes.model.pengyuan.GdSiPersonInfo;
import com.ucredit.hermes.model.pengyuan.HermesCount;
import com.ucredit.hermes.model.pengyuan.HistoryQueryInfo;
import com.ucredit.hermes.model.pengyuan.HistorySiPersonInfo;
import com.ucredit.hermes.model.pengyuan.IdentityVerifyInfo;
import com.ucredit.hermes.model.pengyuan.PoliceResidenceInfo;
import com.ucredit.hermes.model.pengyuan.SpecialInfo;
import com.ucredit.hermes.service.CompanyInfoBatchService;
import com.ucredit.hermes.utils.DecodeZipUtils;
import com.ucredit.hermes.utils.JmsUtils;
import com.ucredit.hermes.utils.ParseXmlUtils;

/**
 * 专线广东社保信息
 *
 * @author caoming
 */
@Component("gdsiJMSQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class GdSiJMSQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
            .getLogger(GdSiJMSQueueListener.class);
    @Autowired
    private GdSiPersonInfoDAO dao;
    @Autowired
    private LogInfoDAO logInfoDAO;
    @Autowired
    private JmsFactory factory;
    @Autowired
    private SubReportInfoDAO subReportInfoDAO;
    @Autowired
    private AreaCodeDAO areaCodeDAO;
    @Autowired
    private StopReasonDAO stopReasonDAO;
    @Autowired
    private QueryReasonDAO queryReasonDAO;
    @Autowired
    private EconomicTypeInfoDAO economicTypeInfoDAO;
    @Autowired
    private TradeInfoDAO tradeInfoDAO;
    @Autowired
    private InformationSourceUnitTypeDAO informationSourceUnitTypeDAO;
    @Autowired
    private GdSiCorpInfoDAO gdSiCorpInfoDAO;
    @Autowired
    private IdentityVerifyInfoDAO identityVerifyInfoDAO;
    @Autowired
    private HistoryQueryInfoDAO historyQueryInfoDAO;
    @Autowired
    private SpecialInfoDAO specialInfoDAO;
    @Autowired
    private HistorySiPersonInfoDAO historySiPersonInfoDAO;
    @Autowired
    private PoliceResidenceInfoDAO policeResidenceInfoDAO;
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
        GdSiJMSQueueListener.logger.debug("JMS执行相应代码...");
        try {

            String systemId = message
                    .getStringProperty(JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY);
            String messageId = message.getJMSMessageID();
            GdSiJMSQueueListener.logger.debug("系统: " + systemId
                + " 查询鹏元个人户籍, 消息ID： " + messageId);
            // 获取传过来的参数
            Object[] vars = (Object[]) ((ObjectMessage) message).getObject();
            ThirdQueueTaskResult result = (ThirdQueueTaskResult) vars[0];
            DataChannel type = (DataChannel) vars[1];
            LogInfo logInfo = (LogInfo) vars[2];
            logInfo.setDataChannel(type);

            // 拼xml参数
            ThirdGenerator thirdGenerator = this.factory.createFactory(type);
            String queryString = thirdGenerator.buildConditionsXML(result);
            GdSiJMSQueueListener.logger.debug("鹏元专线查询个人户籍的xml：" + queryString);
            logInfo.setQueryString(queryString);
            List<GdSiPersonInfo> gdSiPersonInfos = this.dao.getGdSiPersonInfo(
                result.getName(), result.getNumber());
            String data = "";

            // 广东省社保信息
            GdSiPersonInfo gdSiPersonInfo = gdSiPersonInfos.get(0);

            // 去第三方查询
            try {
                data = thirdGenerator.sendXML(queryString);
                GdSiJMSQueueListener.logger.debug("返回的数据为：" + data);

                if (StringUtils.isBlank(data)) {
                    BaseReportType baseReportType = gdSiPersonInfo
                            .getBaseReportType();
                    baseReportType.setErrorMessage("系统返回错误，请重试！");

                    for (GdSiPersonInfo personInfo : gdSiPersonInfos) {
                        personInfo.setBaseReportType(baseReportType);
                    }
                    throw new Exception("三方返回数据为空，请重试......");
                }

                // 解析返回的xml
                this.setGdSiPersonInfo(data, gdSiPersonInfos, logInfo, systemId);
            } catch (Exception ex) {
                logInfo.setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR);
                logInfo.setResultType(ResultType.FAILURE);
                logInfo.setEndTime(new Date());
                logInfo.setRecordId(gdSiPersonInfo.getId());
                this.logInfoDAO.addEntity(logInfo);

                BaseReportType baseReportType = gdSiPersonInfo
                        .getBaseReportType();
                baseReportType.setResultType(ResultType.FAILURE);
                baseReportType.setCreateTime(new Date());
                baseReportType
                .setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR);

                for (GdSiPersonInfo personInfo : gdSiPersonInfos) {
                    personInfo.setBaseReportType(baseReportType);
                    this.dao.updateEntity(personInfo);
                }

                // ray - send error message
                this.sendMessage(gdSiPersonInfo, systemId,
                    gdSiPersonInfo.getId(), AsyncCode.FAILURE_UNEXCEPTED);

                GdSiJMSQueueListener.logger.error(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception e) {
            GdSiJMSQueueListener.logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 解析xml，返回广东个人社保信息
     *
     * @param data
     * @param gdSiPersonInfos
     * @param logInfo
     * @throws ParseException
     * @throws DocumentException
     */
    private void setGdSiPersonInfo(String data,
            List<GdSiPersonInfo> gdSiPersonInfos, LogInfo logInfo,
            String systemId) throws ParseException, DocumentException {

        Document document = DocumentHelper.parseText(data);
        Element rootElement = document.getRootElement();
        String status = rootElement.elementText("status");

        GdSiPersonInfo gdSiPersonInfo = gdSiPersonInfos.get(0);

        BaseReportType baseReportType = gdSiPersonInfo.getBaseReportType();

        // 解密解压缩
        String returnVaule = DecodeZipUtils.decodeZip(rootElement
            .elementText("returnValue"));

        GdSiJMSQueueListener.logger.debug("返回的xml为：" + returnVaule);

        Document returnDocument = DocumentHelper.parseText(returnVaule);
        Element root = returnDocument.getRootElement();

        if ("1".equals(status)) {// 返回成功

            // 第一级节点
            List<Element> elements = root.elements("cisReport");
            // 主表id
            int id = gdSiPersonInfo.getId();

            for (Element e : elements) {

                Element gdSiPersonInfoElement = e.element("gdSiPersonInfo");
                // 解析头信息
                ParseXmlUtils.parseXmlHead(gdSiPersonInfoElement,
                    baseReportType);

                if (baseReportType.getTreatResult() == TreatResult.RICHARD) {// 返回有内容

                    logInfo.setResultType(ResultType.SUCCESS);

                    baseReportType.setTreatResult(TreatResult.RICHARD);
                    baseReportType.setResultType(ResultType.SUCCESS);
                    baseReportType.setDataChannel(DataChannel.PENGYUAN);
                    // 子报告
                    baseReportType.setSubReportType(this.subReportInfoDAO
                        .getSubReportByCode(gdSiPersonInfoElement
                            .attributeValue("subReportType"),
                            ReportType.SPECIAL_REPORT));

                    // 个人社保基本信息
                    List<GdSiPersonInfo> newGdSiPersonInfos = this
                            .parseXMLSetGdSiPersonInfo(gdSiPersonInfoElement);

                    // md5的String
                    String md5String = "";

                    // 个人社保信息md5String
                    for (GdSiPersonInfo personInfo : newGdSiPersonInfos) {
                        md5String = md5String + personInfo.toString();
                        List<HistorySiPersonInfo> historySiPersonInfos = personInfo
                                .getHistorySiPersonInfos();
                        for (HistorySiPersonInfo historySiPersonInfo : historySiPersonInfos) {
                            md5String = md5String
                                    + historySiPersonInfo.toString();
                        }
                    }

                    /**
                     * 广东省个人所在企业社保 信息
                     */
                    List<GdSiCorpInfo> gdSiCorpInfos = new LinkedList<>();
                    // 解析头信息
                    BaseHeadReport gdSiCorpInfoBaseHeadReport = this
                            .parseXMLBaseHeadReport(e, "gdSiCorpInfo");
                    if (gdSiCorpInfoBaseHeadReport.getTreatResult() == TreatResult.RICHARD) {
                        gdSiCorpInfos = this.parseXMLSetGdSiCorpInfo(e,
                            gdSiPersonInfo);
                        for (GdSiCorpInfo corpInfo : gdSiCorpInfos) {
                            corpInfo.setDocumentNo(gdSiPersonInfo
                                .getDocumentNo());
                            md5String = md5String + corpInfo.toString();
                        }
                    }
                    /**
                     * 身份证校验信息
                     */
                    // 解析头信息
                    BaseHeadReport identityVerifyInfoBaseHeadReport = this
                            .parseXMLBaseHeadReport(e, "identityVerifyInfo");
                    IdentityVerifyInfo identityVerifyInfo = null;
                    if (identityVerifyInfoBaseHeadReport.getTreatResult() == TreatResult.RICHARD) {
                        // 解析item
                        identityVerifyInfo = GdSiJMSQueueListener
                                .parseXMLSetIdentityVerifyInfo(e,
                                    identityVerifyInfoBaseHeadReport);
                        md5String = md5String + identityVerifyInfo.toString();
                    }
                    /**
                     * 近两年查询历史明细
                     */
                    // 解析头信息
                    BaseHeadReport historyQueryInfoBaseHeadReport = this
                            .parseXMLBaseHeadReport(e, "historyQueryInfo");
                    List<HistoryQueryInfo> queryInfos = new LinkedList<>();
                    if (historyQueryInfoBaseHeadReport.getTreatResult() == TreatResult.RICHARD) {
                        // 解析item
                        queryInfos = this.parseXMLHistoryQueryInfo(e,
                            historyQueryInfoBaseHeadReport);
                        for (HistoryQueryInfo queryInfo : queryInfos) {
                            queryInfo.setDocumentNo(gdSiPersonInfo
                                .getDocumentNo());
                            md5String = md5String + queryInfo.toString();
                        }
                    }

                    /**
                     * 本人声明
                     */
                    // 解析头信息
                    BaseHeadReport specialInfoBaseHeadReport = this
                            .parseXMLBaseHeadReport(e, "specialInfo");
                    List<SpecialInfo> specialInfos = new LinkedList<>();
                    if (specialInfoBaseHeadReport.getTreatResult() == TreatResult.RICHARD) {
                        specialInfos = this.parseXMLSpecialInfo(e,
                            specialInfoBaseHeadReport);
                        for (SpecialInfo info : specialInfos) {
                            info.setDocumentNo(gdSiPersonInfo.getDocumentNo());
                            md5String = md5String + info.toString();
                        }
                    }

                    /**
                     * 个人户籍信息
                     */

                    // 解析头信息
                    BaseReportType policeBaseReportType = new BaseReportType();
                    Element policeElement = e.element("policeInfo");
                    ParseXmlUtils.parseXmlHead(policeElement,
                        policeBaseReportType);
                    policeBaseReportType.setSubReportType(this.subReportInfoDAO
                        .getSubReportByCode(
                            policeElement.attributeValue("subReportType"),
                            ReportType.SPECIAL_REPORT));

                    // 解析item
                    PoliceResidenceInfo policeInfo = new PoliceResidenceInfo();
                    if (policeBaseReportType.getTreatResult() == TreatResult.RICHARD) {
                        PoliceJMSQueueListener.parseXMLSetPoliceResidenceInfo(
                            policeElement.element("item"), policeInfo);
                        md5String = md5String + policeInfo.toString();
                    }

                    String newMd5 = DigestUtils.md5Hex(md5String);
                    String oldMd5 = baseReportType.getMd5();

                    if (StringUtils.isBlank(oldMd5)) {// md5值相等或为空
                        GdSiPersonInfo newGdSiPersonInfo = newGdSiPersonInfos
                                .get(0);

                        newGdSiPersonInfo.setId(id);
                        baseReportType.setMd5(newMd5);
                        baseReportType.setEnabled(true);
                        baseReportType.setResultType(ResultType.SUCCESS);
                        baseReportType.setCreateTime(new Date());

                        // 更新第一条记录
                        newGdSiPersonInfo.setBaseReportType(baseReportType);
                        id = this.dao.updateEntity(newGdSiPersonInfo).getId();

                        // 更新第一条记录下的广东个人近36个月养老参保记录
                        for (HistorySiPersonInfo historySiPersonInfo : newGdSiPersonInfo
                                .getHistorySiPersonInfos()) {
                            historySiPersonInfo
                            .setGdSiPersonInfo(newGdSiPersonInfo);
                            this.historySiPersonInfoDAO
                            .addEntity(historySiPersonInfo);
                        }

                        // 添加其它记录
                        for (int i = 1; i < newGdSiPersonInfos.size(); i++) {
                            newGdSiPersonInfos.get(i).setBaseReportType(
                                baseReportType);
                            this.dao.addEntity(newGdSiPersonInfos.get(i));
                        }

                        // 添加子表信息
                        if (StringUtils.isBlank(oldMd5)) {
                            this.setSubTable(newGdSiPersonInfo, gdSiCorpInfos,
                                identityVerifyInfo, queryInfos, specialInfos,
                                policeInfo);
                        }

                        // 解析鹏元记录收费子报告次数，添加到数据库
                        this.companyInfoBatchService.addPengYuanCount(
                            root.element("costCount"), id);

                        // hermes自己记录收费次数
                        HermesCount hermesCount = new HermesCount(id, 1,
                            TableType.GD_SI_PERSON_INFOS, DataChannel.PENGYUAN);

                        this.hermesCountDAO.addEntity(hermesCount);

                        // ray - send valid message
                        this.sendMessage(newGdSiPersonInfo, systemId,
                            gdSiPersonInfos.get(0).getId(), AsyncCode.SUCCESS);
                    } else if (newMd5.equals(oldMd5)) {// 查询相等

                        if (com.ucredit.hermes.utils.DateUtils.isDateBefore(
                            baseReportType.getCreateTime(), 1)) {
                            // 解析鹏元记录收费子报告次数，添加到数据库
                            this.companyInfoBatchService.addPengYuanCount(
                                root.element("costCount"), id);

                            // hermes自己记录收费次数
                            HermesCount hermesCount = new HermesCount(id, 1,
                                TableType.GD_SI_PERSON_INFOS,
                                DataChannel.PENGYUAN);
                            this.hermesCountDAO.addEntity(hermesCount);
                        }

                        baseReportType.setMd5(newMd5);
                        baseReportType.setEnabled(true);
                        baseReportType.setCreateTime(new Date());
                        for (GdSiPersonInfo gdsiInfo : gdSiPersonInfos) {
                            gdsiInfo.setBaseReportType(baseReportType);
                            this.dao.updateEntity(gdsiInfo);
                        }

                    } else if (!newMd5.equals(oldMd5)) {// md5值不相等

                        // 之前的数据更新为无效
                        BaseReportType newBaseReportType = new BaseReportType();
                        BeanUtils.copyProperties(baseReportType,
                            newBaseReportType);
                        newBaseReportType.setEnabled(false);
                        newBaseReportType.setMd5(oldMd5);
                        for (GdSiPersonInfo gdSiInfo : gdSiPersonInfos) {
                            gdSiInfo.setBaseReportType(newBaseReportType);
                            this.dao.updateEntity(gdSiInfo);
                        }

                        // 插入新数据
                        baseReportType.setEnabled(true);
                        baseReportType.setCreateTime(new Date());
                        baseReportType.setMd5(newMd5);
                        baseReportType.setResultType(ResultType.SUCCESS);
                        for (GdSiPersonInfo gdSiInfo : newGdSiPersonInfos) {
                            gdSiInfo.setBaseReportType(baseReportType);
                            this.dao.addEntity(gdSiInfo);
                        }

                        // 添加子表记录
                        this.setSubTable(newGdSiPersonInfos.get(0),
                            gdSiCorpInfos, identityVerifyInfo, queryInfos,
                            specialInfos, policeInfo);

                        id = newGdSiPersonInfos.get(0).getId();

                        // 解析鹏元记录收费子报告次数，添加到数据库
                        this.companyInfoBatchService.addPengYuanCount(
                            root.element("costCount"), id);

                        // hermes自己记录收费次数
                        HermesCount hermesCount = new HermesCount(id, 1,
                            TableType.GD_SI_PERSON_INFOS, DataChannel.PENGYUAN);

                        this.hermesCountDAO.addEntity(hermesCount);

                        // ray - send only the newGdSiPersonInfos.get(0) message
                        this.sendMessage(newGdSiPersonInfos.get(0), systemId,
                            newGdSiPersonInfos.get(0).getId(),
                            AsyncCode.SUCCESS);

                    }
                } else {// 返回无内容

                    logInfo.setResultType(ResultType.FAILURE);

                    baseReportType.setTreatResult(TreatResult.NORICHARD);
                    baseReportType.setResultType(ResultType.FAILURE);
                    baseReportType.setCreateTime(new Date());
                    baseReportType.setDataChannel(DataChannel.PENGYUAN);
                    baseReportType.setErrorMessage("未查到此企业的内容，请确认后再次查询！");
                    gdSiPersonInfo.setBaseReportType(baseReportType);
                    id = this.dao.updateEntity(gdSiPersonInfo).getId();

                    // ray send empty message
                    this.sendMessage(gdSiPersonInfo, systemId,
                        gdSiPersonInfo.getId(), AsyncCode.FAILURE_EMPTYDATA);
                }
            }

            logInfo.setRecordId(id);
            ParseXmlUtils.parseXmlSetLogInfo(root, logInfo);
        } else {// 返回失败

            logInfo.setErrorCode(rootElement.elementText("errorCode"));
            logInfo.setErrorMessage(rootElement.elementText("errorMessage"));
            logInfo.setEndTime(new Date());
            logInfo.setResultType(ResultType.FAILURE);

            baseReportType.setResultType(ResultType.FAILURE);
            baseReportType.setCreateTime(new Date());
            baseReportType.setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR);
            for (GdSiPersonInfo info : gdSiPersonInfos) {
                info.setBaseReportType(baseReportType);
                this.dao.updateEntity(info);

                // ray send each message
                this.sendMessage(info, systemId, info.getId(),
                    AsyncCode.FAILURE_3RD_RETURN_ERROR);
            }
        }

        logInfo.setData(returnVaule);
        this.logInfoDAO.addEntity(logInfo);
        GdSiJMSQueueListener.logger.debug("LogInfo <" + logInfo.getId()
            + "> add");
    }

    /**
     * 子表添加到数据
     *
     * @param gdSiPersonInfo
     * @param gdSiCorpInfos
     * @param identityVerifyInfo
     * @param queryInfos
     * @param specialInfos
     * @param policeInfo
     */
    private void setSubTable(GdSiPersonInfo gdSiPersonInfo,
            List<GdSiCorpInfo> gdSiCorpInfos,
            IdentityVerifyInfo identityVerifyInfo,
            List<HistoryQueryInfo> queryInfos, List<SpecialInfo> specialInfos,
            PoliceResidenceInfo policeInfo) {

        // 个人所在单位参保信息
        if (!gdSiCorpInfos.isEmpty()) {
            for (GdSiCorpInfo corpInfo : gdSiCorpInfos) {
                corpInfo.setPersonInfo(gdSiPersonInfo);
            }
            this.gdSiCorpInfoDAO.batchAddEntities(gdSiCorpInfos,
                gdSiCorpInfos.size());
        }

        // 个人身份验证信息
        if (identityVerifyInfo != null
            && StringUtils.isNotBlank(identityVerifyInfo.getDocumentNo())) {
            identityVerifyInfo.setPersonInfo(gdSiPersonInfo);
            this.identityVerifyInfoDAO.addEntity(identityVerifyInfo);
        }

        // 近两年历史查询明细
        if (!queryInfos.isEmpty()) {
            for (HistoryQueryInfo queryInfo : queryInfos) {
                queryInfo.setPersonInfo(gdSiPersonInfo);
            }
            this.historyQueryInfoDAO.batchAddEntities(queryInfos,
                queryInfos.size());
        }

        // 个人申明信息
        if (!specialInfos.isEmpty()) {
            for (SpecialInfo info : specialInfos) {
                info.setPersonInfo(gdSiPersonInfo);
            }
            this.specialInfoDAO.batchAddEntities(specialInfos,
                specialInfos.size());
        }

        // 个人户籍信息
        if (policeInfo != null
            && StringUtils.isNotBlank(policeInfo.getDocumentNo())) {
            policeInfo.setPersonInfo(gdSiPersonInfo);
            policeInfo.setBaseReportType(gdSiPersonInfo.getBaseReportType());
            this.policeResidenceInfoDAO.addEntity(policeInfo);
        }
    }

    /**
     * 解析本人声明
     *
     * @param e
     * @param specialInfoBaseHeadReport
     * @return
     * @throws ParseException
     */
    private List<SpecialInfo> parseXMLSpecialInfo(Element e,
            BaseHeadReport specialInfoBaseHeadReport) throws ParseException {
        List<Element> elements = e.element("specialInfo").elements("item");
        List<SpecialInfo> list = new LinkedList<>();
        for (Element element : elements) {
            SpecialInfo specialInfo = new SpecialInfo();
            specialInfo.setBaseHeadReport(specialInfoBaseHeadReport);
            specialInfo.setInfoType(ParseXmlUtils.parseXmlInfoType(element
                .attributeValue("infoType")));
            specialInfo.setInfoUnit(element.elementText("infoUnit"));
            specialInfo.setInfomation(this.informationSourceUnitTypeDAO
                .getInformationSourceUnitTypeByCode(element
                    .elementText("infoUnitMemberID")));
            specialInfo
                .setInfoDepartment(element.elementText("infoDepartment"));
            Date occurDate = DateUtils.parseDate(
                element.elementText("occurDate"), "yyyyMMdd");
            specialInfo.setOccurDate(occurDate);
            specialInfo.setContent(element.elementText("content"));
            Date infoDate = DateUtils.parseDate(
                element.elementText("infoDate"), "yyyyMMdd");
            specialInfo.setInfoDate(infoDate);
            list.add(specialInfo);
        }
        Collections.sort(list, new Comparator<SpecialInfo>() {

            @Override
            public int compare(SpecialInfo o1, SpecialInfo o2) {
                if (o1.getOccurDate().before(o2.getOccurDate())) {
                    return 1;
                } else if (o1.getOccurDate().after(o2.getOccurDate())) {
                    return -1;
                } else {
                    return 0;
                }

            }
        });
        return list;
    }

    /**
     * 解析近两年的历史记录
     *
     * @param e
     * @param historyQueryInfoBaseHeadReport
     * @throws ParseException
     * @return
     */
    private List<HistoryQueryInfo> parseXMLHistoryQueryInfo(Element e,
            BaseHeadReport historyQueryInfoBaseHeadReport)
            throws ParseException {
        List<Element> elements = e.element("historyQueryInfo").elements("item");
        List<HistoryQueryInfo> queryInfos = new LinkedList<>();
        for (Element element : elements) {
            HistoryQueryInfo queryInfo = new HistoryQueryInfo();
            queryInfo.setBaseHeadReport(historyQueryInfoBaseHeadReport);
            queryInfo.setUnit(Integer.valueOf(element.elementText("unit")));
            queryInfo.setInformation(this.informationSourceUnitTypeDAO
                .getInformationSourceUnitTypeByCode(element
                    .elementText("unitMemberID")));
            queryInfo.setQueryReason(this.queryReasonDAO
                .getQueryReasonByCode(element.elementText("queryReason")));
            Date queryDate = DateUtils.parseDate(
                element.elementText("queryDate"), "yyyyMMdd");
            queryInfo.setQueryDate(queryDate);
            queryInfos.add(queryInfo);
        }
        Collections.sort(queryInfos, new Comparator<HistoryQueryInfo>() {

            @Override
            public int compare(HistoryQueryInfo o1, HistoryQueryInfo o2) {
                if (o1.getQueryDate().before(o2.getQueryDate())) {
                    return 1;
                } else if (o1.getQueryDate().after(o2.getQueryDate())) {
                    return -1;
                }
                return 0;
            }

        });
        return queryInfos;
    }

    /**
     * 身份证号码验证
     *
     * @param e
     * @param identityVerifyInfoBaseHeadReport
     * @return
     * @throws ParseException
     */
    private static IdentityVerifyInfo parseXMLSetIdentityVerifyInfo(Element e,
            BaseHeadReport identityVerifyInfoBaseHeadReport)
            throws ParseException {
        Element element = e.element("identityVerifyInfo").element("item");
        IdentityVerifyInfo verifyInfo = new IdentityVerifyInfo();
        verifyInfo.setBaseHeadReport(identityVerifyInfoBaseHeadReport);
        verifyInfo.setDocumentNo(element.elementText("documentNo"));
        Date birthday = DateUtils.parseDate(element.elementText("birthday"),
            "yyyyMMdd");
        verifyInfo.setBirthday(birthday);
        verifyInfo.setGender(ParseXmlUtils.parseXMLToGender(element
            .elementText("gender")));
        verifyInfo.setOriginalAddress(element.elementText("originalAddress"));
        verifyInfo.setVerifyOfParity(element.elementText("verifyOfParity")
            .equals("1") ? true : false);
        verifyInfo.setVerifyOfArea(element.elementText("verifyOfArea").equals(
            "1") ? true : false);
        verifyInfo.setVerifyOfBirthday(element.elementText("verifyOfBirthday")
            .equals("1") ? true : false);
        verifyInfo.setIs18Identify(element.elementText("is18Identify").equals(
            "1") ? true : false);
        verifyInfo.setVerifyResult(element.elementText("verifyResult").equals(
            "1") ? true : false);
        return verifyInfo;
    }

    /**
     * 解析个人所在单位的信息
     *
     * @param e
     * @param gdSiPersonInfo
     * @return
     * @throws ParseException
     */
    private List<GdSiCorpInfo> parseXMLSetGdSiCorpInfo(Element e,
            GdSiPersonInfo gdSiPersonInfo) throws ParseException {
        List<GdSiCorpInfo> list = new LinkedList<>();
        List<Element> elements = e.element("gdSiCorpInfo").elements("item");
        for (Element ele : elements) {
            GdSiCorpInfo info = new GdSiCorpInfo();
            // 基本信息
            Element baseInfoElement = ele.element("baseInfo");
            info.setCorpName(baseInfoElement.elementText("corpName"));
            info.setOldName(baseInfoElement.elementText("oldName"));
            info.setAreaCode(this.areaCodeDAO.getAreaCodeByCode(baseInfoElement
                .elementText("area")));
            info.setUnitType(ParseXmlUtils.parseXMLUnitType(baseInfoElement
                .elementText("unitType")));
            info.setEconomicTypeInfo(this.economicTypeInfoDAO
                .getEconomicTypeByCode(baseInfoElement
                    .elementText("economyType")));
            info.setRelation(ParseXmlUtils.parseXmlRelation(baseInfoElement
                .elementText("relation")));
            info.setTradeInfo(this.tradeInfoDAO.getTradeByCode(baseInfoElement
                .elementText("trade")));
            info.setCurrentStatus(ParseXmlUtils
                .parseXMLToInsuranceCurrentStatus(baseInfoElement
                    .elementText("currentStatus")));
            Date startDate = DateUtils.parseDate(
                baseInfoElement.elementText("startDate"), "yyyyMMdd");
            info.setStartDate(startDate);
            info.setOweTotalMoney(new BigDecimal(StringUtils
                .isBlank(baseInfoElement.elementText("oweTotalMoney")) ? "0"
                : baseInfoElement.elementText("oweTotalMoney")));
            info.setOweTotalMoneyUnit(new BigDecimal(
                StringUtils.isBlank(baseInfoElement
                    .elementText("oweTotalMoneyUnit")) ? "0" : baseInfoElement
                    .elementText("oweTotalMoneyUnit")));
            Date baseInfoDate = DateUtils.parseDate(
                baseInfoElement.elementText("infoDate"), "yyyyMM");
            info.setBaseInfoDate(baseInfoDate);

            // 最近参保信息
            Element lastNormalInfoElement = ele.element("lastNormalInfo");
            Date lastMonth = DateUtils.parseDate(
                lastNormalInfoElement.elementText("lastMonth"), "yyyyMM");
            info.setLastMonth(lastMonth);
            info.setShouldMoneyMonth(new BigDecimal(
                StringUtils.isBlank(lastNormalInfoElement
                    .elementText("shouldMoneyMonth")) ? "0"
                    : lastNormalInfoElement.elementText("shouldMoneyMonth")));
            info.setActualMoneyMonth(new BigDecimal(
                StringUtils.isBlank(lastNormalInfoElement
                    .elementText("actualMoneyMonth")) ? "0"
                    : lastNormalInfoElement.elementText("actualMoneyMonth")));
            info.setPatchMoneyMonth(new BigDecimal(
                StringUtils.isBlank(lastNormalInfoElement
                    .elementText("patchMoneyMonth")) ? "0"
                    : lastNormalInfoElement.elementText("patchMoneyMonth")));
            info.setInsurePay(new BigDecimal(StringUtils
                .isBlank(lastNormalInfoElement.elementText("insurePay")) ? "0"
                : lastNormalInfoElement.elementText("insurePay")));
            info.setUnitInsureNumber(Integer.valueOf(StringUtils
                .isBlank(lastNormalInfoElement.elementText("unitInsureNumber")) ? "0"
                : lastNormalInfoElement.elementText("unitInsureNumber")));
            Date normalInfoDate = DateUtils.parseDate(
                lastNormalInfoElement.elementText("infoDate"), "yyyyMM");
            info.setNormalInfoDate(normalInfoDate);
            info.setPersonInfo(gdSiPersonInfo);
            list.add(info);
        }
        Collections.sort(list, new Comparator<GdSiCorpInfo>() {
            @Override
            public int compare(GdSiCorpInfo o1, GdSiCorpInfo o2) {
                if (o1.getUnitInsureNumber() > o2.getUnitInsureNumber()) {
                    return 1;
                } else if (o1.getUnitInsureNumber() < o2.getUnitInsureNumber()) {
                    return -1;
                } else {
                    if (o1.getStartDate().before(o2.getStartDate())) {
                        return 1;
                    } else if (o1.getStartDate().after(o2.getStartDate())) {
                        return -1;
                    }
                    return 0;
                }
            }
        });
        return list;
    }

    /**
     * 解析报告的头信息
     *
     * @param e
     * @param sign
     * @return
     */
    private BaseHeadReport parseXMLBaseHeadReport(Element e, String sign) {

        Element element = e.element(sign);
        BaseHeadReport baseHeadReport = new BaseHeadReport();
        ParseXmlUtils.parseXMLBaseHeadReport(element, baseHeadReport);
        baseHeadReport.setSubReportType(this.subReportInfoDAO
            .getSubReportByCode(element.attributeValue("subReportType"),
                ReportType.SPECIAL_REPORT));

        return baseHeadReport;
    }

    /**
     * 最近36个月养老参保记录
     *
     * @param gdSiPersonInfoElement
     * @param gdSiPersonInfo
     * @return
     * @throws ParseException
     */
    private List<HistorySiPersonInfo> parseXMLSetHistorySiPersonInfo(
            Element gdSiPersonInfoElement, GdSiPersonInfo gdSiPersonInfo)
            throws ParseException {
        Element historyElement = gdSiPersonInfoElement.element("historyInfo");
        List<Element> elements = historyElement.elements("item");
        List<HistorySiPersonInfo> historySiPersonInfos = new LinkedList<>();
        for (Element e : elements) {
            HistorySiPersonInfo historySiPersonInfo = new HistorySiPersonInfo();
            historySiPersonInfo.setUnitName(e.elementText("unitName"));
            historySiPersonInfo.setOrgCode(e.elementText("orgCode"));
            historySiPersonInfo.setAreaCode(this.areaCodeDAO
                .getAreaCodeByCode(e.elementText("area")));
            historySiPersonInfo.setUnitType(ParseXmlUtils.parseXMLUnitType(e
                .elementText("unitType")));
            Date startDate = DateUtils.parseDate(e.elementText("startDate"),
                "yyyyMM");
            historySiPersonInfo.setStartDate(startDate);
            Date endDate = DateUtils.parseDate(e.elementText("startDate"),
                "yyyyMM");
            historySiPersonInfo.setEndDate(endDate);
            historySiPersonInfo.setInsuranceState(e
                .elementText("insuranceState"));
            historySiPersonInfo.setDocumentNo(gdSiPersonInfo.getDocumentNo());
            historySiPersonInfos.add(historySiPersonInfo);
        }
        Collections.sort(historySiPersonInfos,
            new Comparator<HistorySiPersonInfo>() {

                @Override
                public int compare(HistorySiPersonInfo o1,
                        HistorySiPersonInfo o2) {
                    if (o1.getStartDate().after(o2.getStartDate())) {
                        return 1;
                    } else if (o1.getStartDate().before(o2.getStartDate())) {
                        return -1;
                    }
                    return 0;
                }
            });
        return historySiPersonInfos;
    }

    /**
     * 解析广东个人信用报告
     *
     * @param gdSiPersonInfoElement
     * @throws ParseException
     */
    private List<GdSiPersonInfo> parseXMLSetGdSiPersonInfo(
            Element gdSiPersonInfoElement) throws ParseException {
        List<Element> itemElements = gdSiPersonInfoElement.elements("item");
        List<GdSiPersonInfo> list = new LinkedList<>();

        int size = itemElements.size();
        GdSiPersonInfo gdSiPersonInfo = new GdSiPersonInfo();
        for (int i = 1; i <= size; i++) {
            Element itemElement = itemElements.get(i - 1);

            gdSiPersonInfo.setNumber(Integer.valueOf(StringUtils
                .isBlank(itemElement.attributeValue("number")) ? "0"
                : itemElement.attributeValue("number")));
            // 基本信息
            Element baseInfoElement = itemElement.element("baseInfo");
            gdSiPersonInfo.setName(baseInfoElement.elementText("name"));
            gdSiPersonInfo.setDocumentNo(baseInfoElement
                .elementText("documentNo"));
            gdSiPersonInfo.setAreaCode(this.areaCodeDAO
                .getAreaCodeByCode(baseInfoElement.elementText("area")));
            gdSiPersonInfo.setCurrentStatus(ParseXmlUtils
                .parseXMLToInsuranceCurrentStatus(baseInfoElement
                    .elementText("currentStatus")));
            gdSiPersonInfo
                .setStopReason(this.stopReasonDAO
                    .getStopReasonByCode(baseInfoElement
                        .elementText("stopReason")));
            if (StringUtils
                .isNotBlank(baseInfoElement.elementText("startDate"))) {
                Date startDate = DateUtils.parseDateStrictly(
                    baseInfoElement.elementText("startDate"), "yyyyMM");
                gdSiPersonInfo.setStartDate(startDate);
            }
            gdSiPersonInfo.setEndowmentAmount(new BigDecimal(StringUtils
                .isBlank(baseInfoElement.elementText("endowmentAmount")) ? "0"
                : baseInfoElement.elementText("endowmentAmount")));
            gdSiPersonInfo.setEndowmentAmountPerson(new BigDecimal(
                baseInfoElement.elementText("endowmentAmountPerson")));
            gdSiPersonInfo.setEndowmentAmountCorp(new BigDecimal(
                baseInfoElement.elementText("endowmentAmountCorp")));
            gdSiPersonInfo.setTotalMonths(Integer.valueOf(baseInfoElement
                .elementText("totalMonths")));
            gdSiPersonInfo.setSpecialJobMonths(Integer.valueOf(baseInfoElement
                .elementText("specialJobMonths")));
            Date baseInfoDate = DateUtils.parseDate(
                baseInfoElement.elementText("infoDate"), "yyyyMM");
            gdSiPersonInfo.setBaseInfoDate(baseInfoDate);

            // 最后参保信息
            Element lastElement = itemElement.element("lastNormalInfo");
            Date lastMonth = DateUtils.parseDate(
                lastElement.elementText("lastMonth"), "yyyyMM");
            gdSiPersonInfo.setLastMonth(lastMonth);
            gdSiPersonInfo.setInsurePay(new BigDecimal(lastElement
                .elementText("insurePay")));

            gdSiPersonInfo.setEndowmentMoneyMonthPerson(new BigDecimal(
                StringUtils.isBlank(lastElement
                    .elementText("endowmentMoneyMonthPerson")) ? "0"
                    : lastElement.elementText("endowmentMoneyMonthPerson")));
            gdSiPersonInfo.setEndowmentMoneyMonthCorp(new BigDecimal(
                StringUtils.isBlank(lastElement
                    .elementText("endowmentMoneyMonthCorp")) ? "0"
                    : lastElement.elementText("endowmentMoneyMonthCorp")));
            gdSiPersonInfo.setLastUnitName(lastElement
                .elementText("lastUnitName"));
            gdSiPersonInfo.setUnitType(ParseXmlUtils
                .parseXMLUnitType(lastElement.elementText("unitType")));
            gdSiPersonInfo.setUnitInsureNumber(Integer.valueOf(StringUtils
                .isBlank(lastElement.elementText("unitInsureNumber")) ? "0"
                : lastElement.elementText("unitInsureNumber")));
            Date normalInfoDate = DateUtils.parseDate(
                lastElement.elementText("infoDate"), "yyyyMM");
            gdSiPersonInfo.setNormalInfoDate(normalInfoDate);

            // 近36个月内参保信息汇总
            Element summaryElement = itemElement.element("summaryInfo");
            if (summaryElement != null) {
                gdSiPersonInfo.setTimesOfLast12Months(Integer
                    .valueOf(StringUtils.isBlank(summaryElement
                        .elementText("timesOfLast12Months")) ? "0"
                        : summaryElement.elementText("timesOfLast12Months")));

                gdSiPersonInfo.setTimesOfLast24Months(Integer
                    .valueOf(StringUtils.isBlank(summaryElement
                        .elementText("timesOfLast24Months")) ? "0"
                        : summaryElement.elementText("timesOfLast24Months")));

                gdSiPersonInfo.setSeriesTimesRecently(Integer
                    .valueOf(StringUtils.isBlank(summaryElement
                        .elementText("seriesTimesRecently")) ? "0"
                        : summaryElement.elementText("seriesTimesRecently")));
                gdSiPersonInfo.setUnitCount(Integer.valueOf(StringUtils
                    .isBlank(summaryElement.elementText("unitCount")) ? "0"
                    : summaryElement.elementText("unitCount")));
            }

            /**
             * <!-- 最近36个月养老参保记录 -->
             */
            List<HistorySiPersonInfo> historySiPersonInfos = this
                .parseXMLSetHistorySiPersonInfo(itemElement, gdSiPersonInfo);
            gdSiPersonInfo.setHistorySiPersonInfos(historySiPersonInfos);

            list.add(gdSiPersonInfo);
        }
        Collections.sort(list, new Comparator<GdSiPersonInfo>() {

            @Override
            public int compare(GdSiPersonInfo o1, GdSiPersonInfo o2) {
                if (o1.getBaseInfoDate().after(o2.getBaseInfoDate())) {
                    return 1;
                } else if (o1.getBaseInfoDate().before(o2.getBaseInfoDate())) {
                    return -1;
                }
                return 0;
            }
        });
        return list;
    }

    private void sendMessage(GdSiPersonInfo resp, String systemId,
            int requestId, AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider.packageResponse(resp,
                systemId, DataChannelSubType.PENGYUAN_GDSOCIAL, requestId,
                asyncCode);
            this.provider.sendTextMessage(messageBody, systemId,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
