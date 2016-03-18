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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.dao.DegreeCollegeInfoDAO;
import com.ucredit.hermes.dao.EducationInSchoolInfoDAO;
import com.ucredit.hermes.dao.HermesCountDAO;
import com.ucredit.hermes.dao.InformationSourceUnitTypeDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
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
import com.ucredit.hermes.model.pengyuan.DegreeCollegeInfo;
import com.ucredit.hermes.model.pengyuan.EducationInfo;
import com.ucredit.hermes.model.pengyuan.HermesCount;
import com.ucredit.hermes.service.CompanyInfoBatchService;
import com.ucredit.hermes.utils.DecodeZipUtils;
import com.ucredit.hermes.utils.JmsUtils;
import com.ucredit.hermes.utils.ParseXmlUtils;

/**
 * 专线学籍信息
 *
 * @author caoming
 */
@Component("inSchoolEducationJMSQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class InSchoolEducationJMSQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
        .getLogger(InSchoolEducationJMSQueueListener.class);
    @Autowired
    private EducationInSchoolInfoDAO dao;
    @Autowired
    private LogInfoDAO logInfoDAO;
    @Autowired
    private DegreeCollegeInfoDAO dcdao;
    @Autowired
    private JmsFactory factory;
    @Autowired
    private SubReportInfoDAO subReportInfoDAO;
    @Autowired
    private InformationSourceUnitTypeDAO informationSourceUnitTypeDAO;
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
        InSchoolEducationJMSQueueListener.logger.debug("JMS执行相应代码...");
        try {
            String systemId = message
                .getStringProperty(JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY);
            String messageId = message.getJMSMessageID();
            InSchoolEducationJMSQueueListener.logger.debug("系统: " + systemId
                + " 查询鹏元个人学籍, 消息ID： " + messageId);
            Object[] vars = (Object[]) ((ObjectMessage) message).getObject();
            ThirdQueueTaskResult result = (ThirdQueueTaskResult) vars[0];
            DataChannel type = (DataChannel) vars[1];
            LogInfo logInfo = (LogInfo) vars[2];
            logInfo.setDataChannel(type);
            ThirdGenerator thirdGenerator = this.factory.createFactory(type);
            String queryString = thirdGenerator.buildConditionsXML(result);
            InSchoolEducationJMSQueueListener.logger.debug("鹏元专线查询个人学籍的xml："
                + queryString);
            logInfo.setQueryString(queryString);
            EducationInfo info = this.dao.getEducationInSchoolInfo(
                result.getName(), result.getNumber());
            String data = "";
            try {
                data = thirdGenerator.sendXML(queryString);

                /*
                 * String encoding = "GBK";
                 * File file = new File("D:\\eduIn.txt");
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

                InSchoolEducationJMSQueueListener.logger
                    .debug("返回的数据为：" + data);
                if (StringUtils.isBlank(data)) {
                    BaseReportType baseReportType = info.getBaseReportType();
                    baseReportType.setErrorMessage("系统返回错误，请重试！");
                    throw new Exception("三方返回数据为空，请重试......");
                }
                this.setEducationInSchoolInfo(data, info, logInfo, systemId);
            } catch (Exception ex) {
                logInfo.setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR);
                logInfo.setResultType(ResultType.FAILURE);
                logInfo.setEndTime(new Date());
                logInfo.setRecordId(info.getId());
                this.logInfoDAO.addEntity(logInfo);
                BaseReportType baseReportType = info.getBaseReportType();
                baseReportType.setResultType(ResultType.FAILURE);
                baseReportType.setCreateTime(new Date());
                baseReportType
                    .setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR + " "
                        + ex.getMessage());

                this.dao.updateEntity(info);

                // ray - send exception message
                this.sendMessage(info, systemId, info.getId(),
                    AsyncCode.FAILURE_UNEXCEPTED);
                InSchoolEducationJMSQueueListener.logger.error(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception e) {
            InSchoolEducationJMSQueueListener.logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setEducationInSchoolInfo(String data, EducationInfo info,
            LogInfo logInfo, String systemId) throws ParseException,
            DocumentException {

        Document document = DocumentHelper.parseText(data);
        Element rootElement = document.getRootElement();
        String status = rootElement.elementText("status");
        BaseReportType baseReportType = info.getBaseReportType();
        // 解密解压缩
        String returnVaule = DecodeZipUtils.decodeZip(rootElement
            .elementText("returnValue"));

        InSchoolEducationJMSQueueListener.logger
            .debug("返回的xml为：" + returnVaule);

        if ("1".equals(status)) {// 返回成功

            Document returnDocument = DocumentHelper.parseText(returnVaule);
            Element root = returnDocument.getRootElement();

            // 第一级节点
            List<Element> elements = root.elements("cisReport");
            // 主表id
            int id = info.getId();
            String batNo = root.attributeValue("batNo");
            String unitName = root.attributeValue("unitName");
            String subOrgan = root.attributeValue("subOrgan");
            String queryUserID = root.attributeValue("queryUserID");
            String receiveTime = root.attributeValue("receiveTime");
            for (Element e : elements) {
                Element inSchoolInfoElement = e.element("educationInSchool2");
                boolean isSuccess = inSchoolInfoElement.attributeValue(
                    "treatResult").contains("1");
                // 解析头信息
                ParseXmlUtils.parseXmlHead(inSchoolInfoElement, baseReportType);
                if (isSuccess) {// 返回有内容

                    logInfo.setResultType(ResultType.SUCCESS);

                    baseReportType.setResultType(ResultType.SUCCESS);
                    baseReportType.setDataChannel(DataChannel.PENGYUAN);
                    // 子报告
                    baseReportType
                        .setSubReportType(this.subReportInfoDAO
                            .getSubReportByCode(inSchoolInfoElement
                                .attributeValue("subReportType"),
                                ReportType.SPECIAL_REPORT));
                    boolean ifFirst = info.getDegreeCollegeInfos() == null
                            || info.getDegreeCollegeInfos().size() < 1 ? true
                                : false;
                    this.parseXMLSetEducationInSchoolInfo(inSchoolInfoElement,
                        info);
                    Set<DegreeCollegeInfo> degreeCollegeInfos = info
                            .getDegreeCollegeInfos();
                    if (ifFirst) {
                        for (DegreeCollegeInfo degreeCollegeInfo : degreeCollegeInfos) {
                            degreeCollegeInfo
                            .setEducationInfos_id(info.getId());
                            this.dcdao.addEntity(degreeCollegeInfo);
                        }
                    }

                    // md5值的设置
                    String newMd5 = DigestUtils.md5Hex(info.toString());
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
                                TableType.EDUCATION_IN_SCHOOL_INFOS,
                                DataChannel.PENGYUAN);
                            this.hermesCountDAO.addEntity(hermesCount);
                        }

                        info.setId(id);
                        info.setBatNo(batNo);
                        info.setUnitName(unitName);
                        info.setSubOrgan(subOrgan);
                        info.setQueryUserID(queryUserID);
                        info.setReceiveTime(DateUtils.parseDate(receiveTime,
                                "yyyyMMdd hh:mm:ss"));
                        baseReportType.setMd5(newMd5);
                        baseReportType.setEnabled(true);
                        baseReportType.setCreateTime(new Date());
                        info.setBaseReportType(baseReportType);
                        id = this.dao.updateEntity(info).getId();

                        // ray - send message
                        this.sendMessage(info, systemId, info.getId(),
                            AsyncCode.SUCCESS);

                    } else if (!newMd5.equals(oldMd5)) {// md5值不相等

                        // 新数据插入数据库
                        EducationInfo newInfo = new EducationInfo();
                        BeanUtils.copyProperties(info, newInfo);
                        newInfo.setVersion(0);

                        BaseReportType newBaseReportType = new BaseReportType();
                        BeanUtils.copyProperties(baseReportType,
                            newBaseReportType);
                        newInfo.setId(null);
                        newBaseReportType.setMd5(newMd5);
                        newBaseReportType.setEnabled(true);
                        newBaseReportType.setCreateTime(new Date());
                        newInfo.setBaseReportType(newBaseReportType);
                        info.setId(id);
                        id = this.dao.addEntity(newInfo);

                        InSchoolEducationJMSQueueListener.logger
                            .debug("EducationInSchoolInfo <" + id + "> add");

                        // 之前的数据置为无效
                        baseReportType.setEnabled(false);
                        info.setBaseReportType(baseReportType);
                        this.dao.updateEntity(info);

                        // 解析鹏元记录收费子报告次数，添加到数据库
                        this.companyInfoBatchService.addPengYuanCount(
                            root.element("costCount"), id);

                        // hermes自己记录收费次数
                        HermesCount hermesCount = new HermesCount(id, 1,
                            TableType.EDUCATION_IN_SCHOOL_INFOS,
                            DataChannel.PENGYUAN);
                        this.hermesCountDAO.addEntity(hermesCount);

                        // ray - send new info
                        /*
                         * 虽然为新的entity，但是客户端方并未知晓新的id号，所
                         * 以如果用REST返回的entityID作为查询记录id的话，需要返回旧的id号以便对照
                         */
                        this.sendMessage(newInfo, systemId, info.getId(),
                            AsyncCode.SUCCESS);
                    }
                } else {// 返回无内容
                    logInfo.setResultType(ResultType.FAILURE);
                    baseReportType.setResultType(ResultType.FAILURE);
                    baseReportType.setCreateTime(new Date());
                    baseReportType.setErrorMessage("未查到此企业的内容，请确认后再次查询！");
                    id = this.dao.updateEntity(info).getId();

                    // ray - send message for empty
                    this.sendMessage(info, systemId, info.getId(),
                        AsyncCode.FAILURE_EMPTYDATA);
                }
            }

            logInfo.setRecordId(id);
            ParseXmlUtils.parseXmlSetLogInfo(root, logInfo);
        } else {// 返回失败
            logInfo.setErrorCode(baseReportType.getTreatErrorCode());
            logInfo.setErrorMessage(baseReportType.getErrorMessage());
            logInfo.setEndTime(new Date());
            logInfo.setResultType(ResultType.FAILURE);

            baseReportType.setResultType(ResultType.FAILURE);
            baseReportType.setCreateTime(new Date());
            baseReportType.setErrorMessage(HermesConsts.HERMES_RESPONSE_ERROR);
            info.setBaseReportType(baseReportType);
        }

        logInfo.setData(returnVaule);
        this.logInfoDAO.addEntity(logInfo);
        InSchoolEducationJMSQueueListener.logger.debug("LogInfo <"
            + logInfo.getId() + "> add");
    }

    /**
     * 解析xml
     *
     * @param element
     * @param info
     * @throws ParseException
     */
    private void parseXMLSetEducationInSchoolInfo(Element element,
            EducationInfo info) throws ParseException {
        Element personBaseInfo = element.element("personBaseInfo");

        info.setName(personBaseInfo.elementText("name"));
        info.setDocumentNo(personBaseInfo.elementText("documentNo"));
        info.setOriginalAddress(personBaseInfo.elementText("originalAddress"));
        info.setVerifyResult(personBaseInfo.elementText("verifyResult"));
        Date birthday = DateUtils.parseDate(
            personBaseInfo.elementText("birthday"), "yyyyMMdd");
        info.setBirthday(birthday);
        info.setGender(ParseXmlUtils.parseXMLToGender(personBaseInfo
            .elementText("gender")));
        info.setAge(Integer.parseInt(personBaseInfo.elementText("age")));

        info.setInfoUnit(Integer.valueOf(element.elementText("infoUnit")));
        info.setInformation(this.informationSourceUnitTypeDAO
            .getInformationSourceUnitTypeByCode(element
                .elementText("infoUnitMemberID")));
        Element educationInfos = element.element("educationInfo");
        List<Element> elements = educationInfos.elements("item");
        // 学籍详细信息Set集合
        Set<DegreeCollegeInfo> degreeCollegeInfos = new HashSet<>();
        for (Element ele : elements) {

            Element degreeMent = ele.element("degreeInfo");
            Element collegeMent = ele.element("collegeInfo");
            DegreeCollegeInfo degreeCollegeInfo = new DegreeCollegeInfo();
            degreeCollegeInfo.setCollege(collegeMent.elementText("college"));
            degreeCollegeInfo.setLevelNo(degreeMent.elementText("levelNo"));
            String startTimeString = degreeMent.elementText("startTime");
            if (StringUtils.isNotBlank(startTimeString)) {
                degreeCollegeInfo.setStartTime(DateUtils.parseDate(
                    startTimeString, "yyyy"));
            }
            degreeCollegeInfo.setGraduateTime(degreeMent
                .elementText("graduateTime"));
            degreeCollegeInfo.setStudyStyle(degreeMent
                .elementText("studyStyle"));
            degreeCollegeInfo.setStudyType(degreeMent.elementText("studyType"));
            degreeCollegeInfo.setSpecialty(degreeMent.elementText("specialty"));
            degreeCollegeInfo.setIsKeySubject(degreeMent
                .elementText("isKeySubject"));
            degreeCollegeInfo.setDegree(degreeMent.elementText("degree"));
            degreeCollegeInfo.setStudyResult(degreeMent
                .elementText("studyResult"));
            degreeCollegeInfo.setPhoto(degreeMent.elementText("photo"));
            degreeCollegeInfo.setPhotoStyle(degreeMent
                .elementText("photoStyle"));

            degreeCollegeInfo.setCollegeOldName(collegeMent
                .elementText("collegeOldName"));
            degreeCollegeInfo.setAddress(collegeMent.elementText("address"));
            degreeCollegeInfo.setCreateDate(collegeMent
                .elementText("createDate"));
            degreeCollegeInfo.setCreateYears(Integer.parseInt(collegeMent
                .elementText("createYears")));
            degreeCollegeInfo.setColgCharacter(collegeMent
                .elementText("colgCharacter"));
            degreeCollegeInfo
                .setColgLevel(collegeMent.elementText("colgLevel"));
            degreeCollegeInfo
                .setCharacter(collegeMent.elementText("character"));
            degreeCollegeInfo.setColgType(collegeMent.elementText("colgType"));
            degreeCollegeInfo.setScienceBatch(collegeMent
                .elementText("scienceBatch"));
            degreeCollegeInfo.setArtBatch(collegeMent.elementText("artBatch"));
            degreeCollegeInfo.setPostDoctorNum(Integer.parseInt(collegeMent
                .elementText("postDoctorNum")));
            degreeCollegeInfo.setDoctorDegreeNum(Integer.parseInt(collegeMent
                .elementText("doctorDegreeNum")));
            degreeCollegeInfo.setMasterDegreeNum(Integer.parseInt(collegeMent
                .elementText("masterDegreeNum")));
            degreeCollegeInfo.setAcademicianNum(Integer.parseInt(collegeMent
                .elementText("academicianNum")));
            degreeCollegeInfo.setIs211(collegeMent.elementText("is211"));
            degreeCollegeInfo.setManageDept(collegeMent
                .elementText("manageDept"));
            degreeCollegeInfo.setKeySubjectNum(Integer.parseInt(collegeMent
                .elementText("keySubjectNum")));

            degreeCollegeInfos.add(degreeCollegeInfo);
        }
        info.setDegreeCollegeInfos(degreeCollegeInfos);
    }

    private void sendMessage(EducationInfo resp, String systemId,
            int requestId, AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider.packageResponse(resp,
                systemId, DataChannelSubType.PENGYUAN_EDU_REG, requestId,
                asyncCode);
            this.provider.sendTextMessage(messageBody, systemId,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
