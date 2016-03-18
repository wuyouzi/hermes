/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.third.jms;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.dao.DegreeCollegeInfoDAO;
import com.ucredit.hermes.dao.EducationInfoDAO;
import com.ucredit.hermes.dao.HermesCountDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.PengYuanReportRecordDAO;
import com.ucredit.hermes.dao.SubReportInfoDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.EducationType;
import com.ucredit.hermes.enums.QueryType;
import com.ucredit.hermes.enums.ReportType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.enums.TableType;
import com.ucredit.hermes.enums.TreatResult;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.pengyuan.BaseReportType;
import com.ucredit.hermes.model.pengyuan.DegreeCollegeInfo;
import com.ucredit.hermes.model.pengyuan.EducationInfo;
import com.ucredit.hermes.model.pengyuan.HermesCount;
import com.ucredit.hermes.model.pengyuan.PengYuanReportRecord;
import com.ucredit.hermes.service.CompanyInfoBatchService;
import com.ucredit.hermes.utils.DecodeZipUtils;
import com.ucredit.hermes.utils.JmsUtils;
import com.ucredit.hermes.utils.ParseXmlUtils;

/**
 * 专线学历信息
 *
 * @author caoming
 */
@Component("educationJMSQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class EducationJMSQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
        .getLogger(EducationJMSQueueListener.class);

    @Autowired
    private EducationInfoDAO dao;
    @Autowired
    private LogInfoDAO logInfoDAO;
    @Autowired
    private JmsFactory factory;
    @Autowired
    private SubReportInfoDAO subReportInfoDAO;
    /*
     * @Autowired private InformationSourceUnitTypeDAO
     * informationSourceUnitTypeDAO;
     */
    @Autowired
    private CompanyInfoBatchService companyInfoBatchService;
    @Autowired
    private HermesCountDAO hermesCountDAO;

    @Autowired
    private DegreeCollegeInfoDAO dcdao;
    /**
     * add by xubaoyong
     */
    @Autowired
    private PengYuanReportRecordDAO pengYuanReportRecordDAO;

    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;

    @Override
    public void onMessage(Message message) {
        EducationJMSQueueListener.logger.debug("JMS执行相应代码...");
        try {
            String systemId = message
                .getStringProperty(JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY);
            String messageId = message.getJMSMessageID();
            EducationJMSQueueListener.logger.debug("系统: " + systemId
                + " 查询鹏元个人学历, 消息ID： " + messageId);
            Object[] vars = (Object[]) ((ObjectMessage) message).getObject();
            ThirdQueueTaskResult result = (ThirdQueueTaskResult) vars[0];
            DataChannel type = (DataChannel) vars[1];
            LogInfo logInfo = (LogInfo) vars[2];
            logInfo.setDataChannel(type);
            ThirdGenerator thirdGenerator = this.factory.createFactory(type);
            String queryString = thirdGenerator.buildConditionsXML(result);
            EducationJMSQueueListener.logger.debug("鹏元专线查询个人学历的xml："
                + queryString);
            logInfo.setQueryString(queryString);
            List<EducationInfo> educationInfos = this.dao.getEducationInfo(
                result.getName(), result.getNumber(), result.getUnitName(),
                result.getQueryUserID(), result.getNewReceiveTime(),
                result.getLevelNo(), result.getGraduateYear(),
                result.getCollege());
            String data = "";
            /*
             * data = thirdGenerator.sendXML(queryString); FileWriter fw = new
             * FileWriter("D:/Test.txt"); fw.write(data,0,data.length());
             * fw.flush();
             */

            /*
             * String encoding = "GBK";
             * File file = new File("D:\\Testyln.txt");
             * if (file.isFile() && file.exists()) { //判断文件是否存在
             * InputStreamReader
             * InputStreamReader read = new InputStreamReader(
             * new FileInputStream(file), encoding);//考虑到编码格式 BufferedReader
             * BufferedReader bufferedReader = new BufferedReader(read);
             * String lineTxt = null;
             * while ((lineTxt = bufferedReader.readLine()) != null) {
             * data = data + lineTxt;
             * }
             * read.close();
             * } else {
             * System.out.println("找不到指定的文件");
             * }
             */

            try {
                for (EducationInfo e : educationInfos) {
                    e.setQueryTime(new Date());
                }
                data = thirdGenerator.sendXML(queryString);
                logInfo.setData(data);
                EducationJMSQueueListener.logger.debug("返回的数据为：" + data);
                if (StringUtils.isBlank(data)) {
                    BaseReportType baseReportType = educationInfos.get(0)
                        .getBaseReportType();
                    baseReportType.setErrorMessage("系统返回错误，请重试！");
                    for (EducationInfo educationInfo : educationInfos) {
                        educationInfo.setBaseReportType(baseReportType);
                        this.dao.updateEntity(educationInfo);
                    }
                    // TODO 抛出何种异常会再次查询
                    throw new Exception("三方返回数据为空，请重试......");
                }
                this.setEducationInfo(data, educationInfos, logInfo, result,
                    systemId);
            } catch (Exception ex) {
                logInfo.setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR
                    + ex.getMessage());
                logInfo.setResultType(ResultType.FAILURE);
                logInfo.setEndTime(new Date());
                logInfo.setRecordId(educationInfos.get(0).getId());
                this.logInfoDAO.addEntity(logInfo);
                BaseReportType baseReportType = educationInfos.get(0)
                    .getBaseReportType();
                baseReportType.setEnabled(false);
                baseReportType.setResultType(ResultType.FAILURE);
                baseReportType.setCreateTime(new Date());
                baseReportType.setDataChannel(type);
                baseReportType
                .setErrorMessage(AsyncCode.FAILURE_CONNECTION_REFUSED + "_"
                        + HermesConsts.HERMES_RESPONSE_ERROR + ex.getMessage());
                for (EducationInfo educationInfo : educationInfos) {
                    educationInfo.setBaseReportType(baseReportType);
                    this.dao.updateEntity(educationInfo);

                    // ray - send error message
                    this.sendMessage(educationInfo, systemId,
                        educationInfo.getKeyid(),
                        AsyncCode.FAILURE_CONNECTION_REFUSED);
                }
                EducationJMSQueueListener.logger.error(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception e) {
            EducationJMSQueueListener.logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 解析xml
     *
     * @param data
     * @param educationInfo
     * @param logInfo
     * @throws ParseException
     * @throws DocumentException
     * @throws InterruptedException
     */
    private void setEducationInfo(String data,
            List<EducationInfo> educationInfos, LogInfo logInfo,
            ThirdQueueTaskResult result, String systemId)
            throws ParseException, DocumentException, InterruptedException {
        Document document = DocumentHelper.parseText(data);
        Element rootElement = document.getRootElement();
        String status = rootElement.elementText("status");
        BaseReportType baseReportType = educationInfos.get(0)
            .getBaseReportType();
        Date now = new Date();

        if ("1".equals(status)) {// 返回成功

            // 解密解压缩
            String returnVaule = DecodeZipUtils.decodeZip(rootElement
                .elementText("returnValue"));

            /*
             * FileWriter fw; try { fw = new FileWriter("D:\\aa.txt");
             * fw.write(returnVaule, 0, returnVaule.length()); fw.flush(); }
             * catch ( IOException e) { // TODO Auto-generated catch block
             * e.printStackTrace(); }
             */
            EducationJMSQueueListener.logger.debug("返回的xml为：" + returnVaule);

            Document returnDocument = DocumentHelper.parseText(returnVaule);
            Element root = returnDocument.getRootElement();

            // 第一级节点
            List<Element> cisReports = root.elements("cisReport");
            String batNo = root.attributeValue("batNo");
            String unitName = root.attributeValue("unitName");
            String subOrgan = root.attributeValue("subOrgan");
            String queryUserID = root.attributeValue("queryUserID");
            String receiveTime = root.attributeValue("receiveTime");

            // 主表id
            int id = educationInfos.get(0).getId();
            //主表进件号
            String lendRequestId = educationInfos.get(0).getLendRequestId();

            for (Element cisReport : cisReports) {
                // 记录我方流水号
                String refId = result.getRefId();
                String pengYuanReturnRefId = cisReport.attributeValue("refID");
                String pengYuanRePortId = cisReport.attributeValue("reportID");
                baseReportType.setRefId(refId);

                Element educationElement = cisReport
                    .element("lastEducationInfo");
                boolean isSuccess = educationElement.attributeValue(
                    "treatResult").contains("1");

                // 解析头信息
                ParseXmlUtils.parseXmlHead(educationElement, baseReportType);
                baseReportType.setDataChannel(DataChannel.PENGYUAN);

                if (isSuccess) {// 返回有内容

                    logInfo.setResultType(ResultType.SUCCESS);

                    baseReportType.setResultType(ResultType.SUCCESS);

                    // 子报告
                    baseReportType.setSubReportType(this.subReportInfoDAO
                        .getSubReportByCode(
                            educationElement.attributeValue("subReportType"),
                            ReportType.SPECIAL_REPORT));

                    // 解析最新的xml，获取EducationInfo数据
                    EducationInfo newEducationInfo = EducationJMSQueueListener
                        .parseXMLSetEducationInfo(educationElement,
                            baseReportType);

                    String newMd5 = DigestUtils.md5Hex(educationInfos
                        .toString());

                    if (newEducationInfo != null) {
                        // 创建新的
                        EducationInfo educationInfo = newEducationInfo;

                        String oldMd5 = educationInfos.get(0)
                            .getBaseReportType().getMd5();
                        if (StringUtils.isBlank(oldMd5)
                            || newMd5.equals(oldMd5)) {// md5值相等或为空
                            educationInfo.setId(id);
                            educationInfo.setKeyid(result.getKeyid());
                            baseReportType.setMd5(newMd5);
                            baseReportType.setEnabled(true);
                            baseReportType.setCreateTime(now);
                            // 更新第一条记录
                            educationInfo.setBaseReportType(baseReportType);
                            educationInfo.setBatNo(batNo);
                            educationInfo.setUnitName(unitName);
                            educationInfo.setSubOrgan(subOrgan);
                            educationInfo.setQueryUserID(queryUserID);
                            educationInfo.setReceiveTime(DateUtils.parseDate(
                                receiveTime, "yyyyMMdd hh:mm:ss"));
                            educationInfo.setReportID(pengYuanRePortId);
                            educationInfo.setLendRequestId(lendRequestId);
                            educationInfo
                            .setEducationType(EducationType.EDUCATION);
                            educationInfo.setQueryTime(educationInfos.get(0)
                                .getQueryTime());
                            educationInfo.setLastUpdateTime(new Date());
                            id = this.dao.updateEntity(educationInfo).getId();
                        } else if (!newMd5.equals(oldMd5)) {// md5值不相等
                            // 将之前的都设置为enabled= false;
                            for (EducationInfo educationInfoTemp : educationInfos) {
                                BaseReportType tempBaseReportType = educationInfoTemp
                                    .getBaseReportType();
                                tempBaseReportType.setEnabled(false);
                                educationInfoTemp
                                    .setBaseReportType(tempBaseReportType);
                                this.dao.updateEntity(educationInfoTemp);

                            }
                            // 其它记录添加
                            educationInfo.setBatNo(batNo);
                            educationInfo.setUnitName(unitName);
                            educationInfo.setSubOrgan(subOrgan);
                            educationInfo.setQueryUserID(queryUserID);
                            educationInfo.setReceiveTime(DateUtils.parseDate(
                                receiveTime, "yyyyMMdd hh:mm:ss"));
                            BaseReportType newBaseReportType = baseReportType;
                            newBaseReportType.setEnabled(true);
                            educationInfo.setBaseReportType(newBaseReportType);
                            educationInfo.setReportID(pengYuanRePortId);
                            educationInfo.setLendRequestId(lendRequestId);
                            educationInfo
                                .setEducationType(EducationType.EDUCATION);
                            educationInfo.setQueryTime(educationInfos.get(0)
                                .getQueryTime());
                            educationInfo.setLastUpdateTime(new Date());
                            educationInfo.setKeyid(result.getKeyid());
                            this.dao.addEntity(educationInfo);
                        }

                        Set<DegreeCollegeInfo> degreeCollegeInfos = newEducationInfo
                            .getDegreeCollegeInfos();
                        if (educationInfos.get(0).getDegreeCollegeInfos() == null
                            || educationInfos.get(0).getDegreeCollegeInfos()
                                .size() < 1) {
                            for (DegreeCollegeInfo degreeCollegeInfo : degreeCollegeInfos) {
                                degreeCollegeInfo
                                    .setEducationInfos_id(educationInfo.getId());
                                this.dcdao.addEntity(degreeCollegeInfo);
                            }
                        }
                        EducationJMSQueueListener.logger
                            .info("---------------------开始往队列里发鹏元返回的信息---学历id："
                                + educationInfo.getId());
                        Thread.sleep(2000);
                        this.sendMessage(educationInfo, systemId,
                            educationInfo.getKeyid(), AsyncCode.SUCCESS);
                        EducationJMSQueueListener.logger
                            .info("---------------------结束往队列里发鹏元返回的信息---学历");

                    } else {
                        logInfo.setResultType(ResultType.FAILURE);
                        baseReportType.setResultType(ResultType.FAILURE);
                        baseReportType.setTreatResult(TreatResult.NORICHARD);
                        baseReportType.setCreateTime(now);
                        baseReportType.setErrorMessage("未查到此用户的学历信息！");
                        for (EducationInfo educationInfo : educationInfos) {
                            educationInfo.setLastUpdateTime(new Date());
                            educationInfo.setBaseReportType(baseReportType);
                            this.dao.updateEntity(educationInfo);
                            Thread.sleep(2000);
                            this.sendMessage(educationInfo, systemId,
                                educationInfo.getKeyid(),
                                AsyncCode.FAILURE_EMPTYDATA);

                        }

                    }

                } else {//未查到此用户的学历信息
                    logInfo.setResultType(ResultType.FAILURE);
                    baseReportType.setResultType(ResultType.FAILURE);
                    baseReportType.setTreatResult(TreatResult.NORICHARD);
                    baseReportType.setCreateTime(now);
                    baseReportType.setErrorMessage("未查到此用户的学历信息");
                    for (EducationInfo educationInfo : educationInfos) {
                        educationInfo.setBaseReportType(baseReportType);
                        this.dao.updateEntity(educationInfo);
                        educationInfo.setLastUpdateTime(new Date());
                        Thread.sleep(2000);
                        this.sendMessage(educationInfo, systemId,
                            educationInfo.getKeyid(),
                            AsyncCode.FAILURE_EMPTYDATA);
                    }
                }
                // 解析鹏元记录收费子报告次数，添加到数据库
                this.companyInfoBatchService.addPengYuanCount(
                    root.element("costCount"), id);

                Element costCountElement = root.element("costCount");
                List<Element> itemElements = costCountElement.elements("item");
                int counts = 0;
                for (Element e : itemElements) {
                    counts = counts + Integer.parseInt(e.elementText("count"));
                }

                // 记录收费日志
                this.addPengYuanReportRecord(pengYuanRePortId, counts,
                    pengYuanReturnRefId, refId,
                    educationElement.attributeValue("subReportType"), now, id
                        + "");
                // hermes自己记录收费次数
                HermesCount hermesCount = new HermesCount(id, counts,
                    TableType.EDUCATION_INFOS, DataChannel.PENGYUAN);
                this.hermesCountDAO.addEntity(hermesCount);
            }

            logInfo.setData(returnVaule);
            logInfo.setRecordId(id);
            ParseXmlUtils.parseXmlSetLogInfo(root, logInfo);

            // end for get response from 3rd
        } else {// 返回失败
            logInfo.setErrorCode(rootElement.elementText("errorCode"));
            logInfo.setErrorMessage(rootElement.elementText("errorMessage"));
            logInfo.setEndTime(now);
            logInfo.setResultType(ResultType.FAILURE);
            baseReportType.setResultType(ResultType.FAILURE);
            baseReportType.setEnabled(false);
            baseReportType.setCreateTime(now);
            baseReportType.setErrorMessage(rootElement
                .elementText("errorMessage"));
            for (EducationInfo educationInfo : educationInfos) {
                educationInfo.setBaseReportType(baseReportType);
                educationInfo.setLastUpdateTime(new Date());
                // ray - ger response is empty and send bad queried message back
                Thread.sleep(2000);
                this.sendMessage(educationInfo, systemId,
                    educationInfo.getKeyid(),
                    AsyncCode.FAILURE_3RD_RETURN_ERROR);
            }
            logInfo.setData(data);
            //返回失败hermes记录日志
            HermesCount hermesCount = new HermesCount(educationInfos.get(0)
                .getId(), 1, false, TableType.EDUCATION_INFOS,
                DataChannel.PENGYUAN);
            this.hermesCountDAO.addEntity(hermesCount);
        }

        this.logInfoDAO.addEntity(logInfo);
        EducationJMSQueueListener.logger.debug("LogInfo <" + logInfo.getId()
            + "> add");
    }

    /**
     * 获取学历信息
     *
     * @param e
     * @param baseReportType
     * @return
     * @throws ParseException
     * @author zhouwuyuan
     */
    private static EducationInfo parseXMLSetEducationInfo(Element e,
            BaseReportType baseReportType) throws ParseException {
        Element personBaseInfo = e.element("personBaseInfo");
        EducationInfo educationalInfo = new EducationInfo();
        educationalInfo.setName(personBaseInfo.elementText("name"));
        educationalInfo.setDocumentNo(personBaseInfo.elementText("documentNo"));
        educationalInfo.setDegree(personBaseInfo.elementText("degree"));
        educationalInfo.setSpecialty(personBaseInfo.elementText("specialty"));
        educationalInfo.setCollege(personBaseInfo.elementText("college"));
        educationalInfo.setGraduateTime(Integer.parseInt(personBaseInfo
            .elementText("graduateTime")));
        educationalInfo.setGraduateYears(Integer.parseInt(personBaseInfo
            .elementText("graduateYears")));
        String birthdayString = personBaseInfo.elementText("birthday");
        if (StringUtils.isNotBlank(birthdayString)) {
            educationalInfo.setBirthday(DateUtils.parseDate(birthdayString,
                    "yyyyMMdd"));
        }
        educationalInfo.setGender(ParseXmlUtils.parseXMLToGender(personBaseInfo
            .elementText("gender")));
        educationalInfo.setAge(Integer.parseInt(personBaseInfo
            .elementText("age")));
        educationalInfo.setOriginalAddress(personBaseInfo
            .elementText("originalAddress"));
        educationalInfo.setVerifyResult(personBaseInfo
            .elementText("verifyResult"));
        educationalInfo
        .setRiskAndAdviceInfo(e.elementText("riskAndAdviceInfo"));
        Element element = e.element("educationInfo");
        //当查询的是所有学历信息时，启用注解部分
        //Element educationalEle = e.element("educationInfo");
        // List<Element> elements = educationalEle.elements("item");
        // 学历详细信息Set集合
        Set<DegreeCollegeInfo> degreeCollegeInfos = new HashSet<>();
        // for (Element element : elements) {
        Element degreeMent = element.element("degreeInfo");
        Element collegeMent = element.element("collegeInfo");
        DegreeCollegeInfo degreeCollegeInfo = new DegreeCollegeInfo();
        degreeCollegeInfo.setCollege(degreeMent.elementText("college"));
        degreeCollegeInfo.setLevelNo(degreeMent.elementText("levelNo"));
        String startTimeString = degreeMent.elementText("startTime");
        if (StringUtils.isNotBlank(startTimeString)) {
            if (startTimeString.length() == 8) {
                degreeCollegeInfo.setStartTime(DateUtils.parseDate(
                    startTimeString, "yyyyMMdd"));
            } else if (startTimeString.length() == 6) {
                degreeCollegeInfo.setStartTime(DateUtils.parseDate(
                    startTimeString, "yyyyMM"));

            }
        }
        degreeCollegeInfo.setGraduateTime(degreeMent
            .elementText("graduateTime"));
        degreeCollegeInfo.setStudyStyle(degreeMent.elementText("studyStyle"));
        degreeCollegeInfo.setStudyType(degreeMent.elementText("studyType"));
        degreeCollegeInfo.setSpecialty(degreeMent.elementText("specialty"));
        degreeCollegeInfo.setIsKeySubject(degreeMent
            .elementText("isKeySubject"));
        degreeCollegeInfo.setDegree(degreeMent.elementText("degree"));
        degreeCollegeInfo.setStudyResult(degreeMent.elementText("studyResult"));
        degreeCollegeInfo.setPhoto(degreeMent.elementText("photo"));
        degreeCollegeInfo.setPhotoStyle(degreeMent.elementText("photoStyle"));

        degreeCollegeInfo.setCollegeOldName(collegeMent
            .elementText("collegeOldName"));
        degreeCollegeInfo.setAddress(collegeMent.elementText("address"));
        degreeCollegeInfo.setCreateDate(collegeMent.elementText("createDate"));
        if (!collegeMent.elementText("createYears").isEmpty()) {
            degreeCollegeInfo.setCreateYears(Integer.parseInt(collegeMent
                .elementText("createYears")));
        }
        degreeCollegeInfo.setColgCharacter(collegeMent
            .elementText("colgCharacter"));
        degreeCollegeInfo.setColgLevel(collegeMent.elementText("colgLevel"));
        degreeCollegeInfo.setCharacter(collegeMent.elementText("character"));
        degreeCollegeInfo.setColgType(collegeMent.elementText("colgType"));
        degreeCollegeInfo.setScienceBatch(collegeMent
            .elementText("scienceBatch"));
        degreeCollegeInfo.setArtBatch(collegeMent.elementText("artBatch"));
        if (!collegeMent.elementText("postDoctorNum").isEmpty()) {
            degreeCollegeInfo.setPostDoctorNum(Integer.parseInt(collegeMent
                .elementText("postDoctorNum")));
        }
        if (!collegeMent.elementText("doctorDegreeNum").isEmpty()) {
            degreeCollegeInfo.setDoctorDegreeNum(Integer.parseInt(collegeMent
                .elementText("doctorDegreeNum")));
        }
        if (!collegeMent.elementText("masterDegreeNum").isEmpty()) {
            degreeCollegeInfo.setMasterDegreeNum(Integer.parseInt(collegeMent
                .elementText("masterDegreeNum")));
        }
        if (!collegeMent.elementText("academicianNum").isEmpty()) {
            degreeCollegeInfo.setAcademicianNum(Integer.parseInt(collegeMent
                .elementText("academicianNum")));
        }

        degreeCollegeInfo.setIs211(collegeMent.elementText("is211"));
        degreeCollegeInfo.setManageDept(collegeMent.elementText("manageDept"));
        if (!collegeMent.elementText("keySubjectNum").isEmpty()) {
            degreeCollegeInfo.setKeySubjectNum(Integer.parseInt(collegeMent
                .elementText("keySubjectNum")));
        }

        degreeCollegeInfos.add(degreeCollegeInfo);

        //}
        educationalInfo.setDegreeCollegeInfos(degreeCollegeInfos);
        educationalInfo.setBaseReportType(baseReportType);
        return educationalInfo;
    }

    public void addPengYuanReportRecord(String pengYuanRePortId, int count,
            String pengYuanReturnRefId, String refId, String subReportCode,
            Date resultTime, String tableId) {
        // 增加记录
        PengYuanReportRecord logRecord = new PengYuanReportRecord();
        logRecord.setCount(count);
        logRecord.setPengYuanRePortId(pengYuanRePortId);
        logRecord.setPengYuanReturnRefId(pengYuanReturnRefId);
        logRecord.setDataChannel(DataChannel.PENGYUAN);
        logRecord.setMainId(refId);
        logRecord.setQueryType(QueryType.PERSON_EDUCATION);
        logRecord.setSubReportCode(subReportCode);
        logRecord.setResultTime(resultTime);
        logRecord.setTableId(tableId);
        this.pengYuanReportRecordDAO.addEntity(logRecord);
    }

    private void sendMessage(EducationInfo resp, String systemId,
            String requestId, AsyncCode asyncCode) {
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
