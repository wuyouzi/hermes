/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service;

import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

import javax.jms.Destination;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.dao.IdentifySearchRecordDAO;
import com.ucredit.hermes.dao.IdentityInfoDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.UserDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.Gender;
import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.User;
import com.ucredit.hermes.model.guozhengtong.IdentifySearchRecord;
import com.ucredit.hermes.model.guozhengtong.IdentityInfo;
import com.ucredit.hermes.third.jms.JmsFactory;
import com.ucredit.hermes.third.jms.ThirdGenerator;

/**
 * 身份信息: service
 *
 * @author liuqianqian
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class IdentityInfoService {
    private static Logger logger = LoggerFactory
            .getLogger(IdentityInfoService.class);
    @Autowired
    private IdentityInfoDAO identityInfoDAO;
    @Autowired
    private LogInfoDAO logInfoDao;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private IdentifySearchRecordDAO identifySearchRecordDAO;
    @Autowired
    private ThirdQueueService thirdQueueService;
    @Autowired
    private JmsFactory factory;
    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;
    private final static String IDENTITYINFO = "identity_infos";
    private final static String QUERY_FAILED_2 = "身份证和姓名不匹配";
    private final static String QUERY_FAILED_1 = "库存中无此数据";
    private final static String IDCARD_ERROR = "身份证格式错误";
    private final static String NAME_ERROR = "姓名格式错误";

    /**
     * 根据身份证号获取记录 同步和异步
     *
     * @param name支持
     *        姓名
     * @param identitycard
     *        身份证号
     * @param operationName
     * @param constraint
     *        强制刷新
     * @param isSynch
     *        true:同步 false：异步
     * @param activeDays
     *        有效天数
     * @param activeDate
     *        有效日期
     * @param principal
     * @return IdentityInfo
     */
    public IdentityInfo getIdentityInfos(String operationName, String name,
            String identitycard, String keyid, boolean isSynch,
            boolean constraint, int activeDays, Date activeDate,
            Principal principal) {
        Pattern idNumPattern = Pattern
            .compile("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");

        if (!idNumPattern.matcher(identitycard).matches()) {
            IdentityInfo identityInfoError = new IdentityInfo();
            identityInfoError.setName(name);
            identityInfoError.setResultType(ResultType.FAILURE);
            identityInfoError.setIdentitycard(identitycard);
            identityInfoError.setErrorCode(HermesConsts.HERMES_IDCARD_ERROR);
            identityInfoError.setErrorMessage(IdentityInfoService.IDCARD_ERROR);
            return identityInfoError;
        }
        Pattern namePattern = Pattern
                .compile("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*");
        if (!namePattern.matcher(name).matches()) {
            IdentityInfo identityInfoError = new IdentityInfo();
            identityInfoError.setName(name);
            identityInfoError.setResultType(ResultType.FAILURE);
            identityInfoError.setIdentitycard(identitycard);
            identityInfoError.setErrorCode(HermesConsts.HERMES_NAME_ERROR);
            identityInfoError.setErrorMessage(IdentityInfoService.NAME_ERROR);
            return identityInfoError;
        }

        String username = ((Authentication) principal).getName();
        User user = this.userDao.getUserByUserName(username);

        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
                .getDetails();
        // 记录日志
        LogInfo logInfo = new LogInfo(operationName, user.getGroup(),
            details.getRemoteAddress(), new Date(), null);
        ThirdQueueTaskResult result = new ThirdQueueTaskResult(name,
            identitycard, null, JMSType.GUO_ZHENG_TONG_JMS_QUEUE);

        IdentityInfo identityInfo = this.identityInfoDAO
                .getIdentityInfosByName(name, identitycard);
        String queryString = result.getName() + "," + result.getNumber();
        Date date = new Date();

        //查询记录
        IdentifySearchRecord searchRecord = new IdentifySearchRecord();
        this.identifySearchRecordDAO.addEntity(searchRecord);
        if (identityInfo == null) {
            // 插入数据库一条记录，状态为查询中
            identityInfo = new IdentityInfo();
            identityInfo.setName(name);
            identityInfo.setIdentitycard(identitycard);
            identityInfo.setCreateTime(date);
            identityInfo.setKeyid(keyid);
            identityInfo.setDataChannel(DataChannel.GUOZHENGTONG);
            identityInfo.setResultType(ResultType.QUERY);
            this.identityInfoDAO.addEntity(identityInfo);
            searchRecord.setIdentify_id(identityInfo.getId());
            //hermes先按照身份证查询本地库，如存在匹配成功的则表示发起查询的姓名有误，就可以不发起国政通的查询
            IdentityInfo identityInfoByCard = this.identityInfoDAO
                .getIdentityInfosByCard(identitycard);
            if (identityInfoByCard != null) {
                identityInfo.setDataChannel(DataChannel.HERMES);
                identityInfo.setResultType(ResultType.FAILURE);
                identityInfo.setErrorCode(HermesConsts.HERMES_GZT_NOTMATCH);
                identityInfo
                    .setErrorMessage(IdentityInfoService.QUERY_FAILED_2);
                return identityInfo;
            }
            // 异步请求，访问JMS代码
            if (!isSynch) {
                this.thirdQueueService.sendMessage(result,
                    DataChannel.GUOZHENGTONG, logInfo);
            } else {//同步请求，直接发起查询
                try {
                    ThirdGenerator thirdGenerator = this.factory
                            .createFactory(DataChannel.GUOZHENGTONG);
                    logInfo.setQueryString(queryString);
                    logInfo.setDataChannel(DataChannel.GUOZHENGTONG);
                    logInfo.setTableName(IdentityInfoService.IDENTITYINFO);
                    identityInfo.setQueryTime(new Date());
                    String resultXml = thirdGenerator.sendXML(queryString);
                    identityInfo.setBackTime(new Date());
                    logInfo.setData(resultXml);
                    IdentityInfoService.logger.debug("国政通返回的xml为：" + resultXml);

                    if (StringUtils.isBlank(resultXml)) {
                        identityInfo.setErrorMessage("系统返回错误，请重试！");
                        throw new Exception("三方返回数据为空，请重试......");
                    }
                    searchRecord.setDataChannel(DataChannel.GUOZHENGTONG);
                    this.setIdentityInfo(resultXml, identityInfo, logInfo,
                        username, isSynch);
                    searchRecord.setErrorCode(identityInfo.getErrorCode());
                    searchRecord
                    .setErrorMessage(identityInfo.getErrorMessage());
                } catch (Exception e) {
                    identityInfo.setBackTime(new Date());
                    e.printStackTrace();
                    logInfo.setResultType(ResultType.FAILURE);
                    logInfo.setEndTime(new Date());
                    logInfo.setRecordId(identityInfo.getId());
                    logInfo
                        .setErrorMessage(HermesConsts.HERMES_GZT_RESPONSE_ERROR
                            + "：" + e.getMessage());
                    identityInfo
                    .setErrorCode(HermesConsts.HERMES_CONNECT_ERROR);
                    identityInfo
                    .setErrorMessage(HermesConsts.HERMES_GZT_RESPONSE_ERROR
                            + "：" + e.getMessage());
                    identityInfo.setEnabled(false);
                    identityInfo.setResultType(ResultType.FAILURE);
                    identityInfo.setCreateTime(new Date());
                    searchRecord.setDataChannel(DataChannel.GUOZHENGTONG);
                    searchRecord.setErrorCode(identityInfo.getErrorCode());
                    searchRecord
                    .setErrorMessage(identityInfo.getErrorMessage());
                    this.identityInfoDAO.updateEntity(identityInfo);
                    this.logInfoDao.addEntity(logInfo);
                }
            }
        } else {
            Date createTime = identityInfo.getCreateTime();
            Date acctiveTime = DateUtils.addDays(createTime, activeDays);

            // 有效期时间 是否有效判断，以有效日期为基准。当查询时间（new Date()）在查询时间之前，该数据为有效，反之亦然。
            // 当传过来的为数字，数据时间加上此天数在当前时间之后为有效，反之亦然。
            if (constraint || date.after(activeDate) || date.after(acctiveTime)) {
                identityInfo.setResultType(ResultType.QUERY);
                this.identityInfoDAO.updateEntity(identityInfo);
                //异步请求， 访问JMS代码
                if (!isSynch) {
                    this.thirdQueueService.sendMessage(result,
                        DataChannel.GUOZHENGTONG, logInfo);
                } else {//同步请求，直接发起查询
                    try {
                        ThirdGenerator thirdGenerator = this.factory
                            .createFactory(DataChannel.GUOZHENGTONG);
                        logInfo.setQueryString(queryString);
                        logInfo.setDataChannel(DataChannel.GUOZHENGTONG);
                        logInfo.setTableName(IdentityInfoService.IDENTITYINFO);
                        identityInfo.setQueryTime(new Date());
                        String resultXml = thirdGenerator.sendXML(queryString);
                        identityInfo.setBackTime(new Date());
                        logInfo.setData(resultXml);
                        IdentityInfoService.logger.debug("国政通返回的xml为："
                                + resultXml);

                        if (StringUtils.isBlank(resultXml)) {
                            identityInfo.setErrorMessage("系统返回错误，请重试！");
                            throw new Exception("三方返回数据为空，请重试......");
                        }
                        searchRecord.setIdentify_id(identityInfo.getId());
                        searchRecord.setDataChannel(DataChannel.GUOZHENGTONG);
                        this.setIdentityInfo(resultXml, identityInfo, logInfo,
                            username, isSynch);
                        searchRecord.setErrorCode(identityInfo.getErrorCode());
                        searchRecord.setErrorMessage(identityInfo
                            .getErrorMessage());
                    } catch (Exception e) {
                        identityInfo.setBackTime(new Date());
                        e.printStackTrace();
                        logInfo.setResultType(ResultType.FAILURE);
                        logInfo.setEndTime(new Date());
                        logInfo.setRecordId(identityInfo.getId());
                        logInfo
                            .setErrorMessage(HermesConsts.HERMES_GZT_RESPONSE_ERROR
                                + "：" + e.getMessage());
                        identityInfo
                            .setErrorCode(HermesConsts.HERMES_CONNECT_ERROR);
                        identityInfo
                        .setErrorMessage(HermesConsts.HERMES_GZT_RESPONSE_ERROR
                                + "：" + e.getMessage());
                        identityInfo.setEnabled(false);
                        identityInfo.setResultType(ResultType.FAILURE);
                        identityInfo.setCreateTime(new Date());
                        searchRecord.setDataChannel(DataChannel.GUOZHENGTONG);
                        searchRecord.setErrorCode(identityInfo.getErrorCode());
                        searchRecord.setErrorMessage(identityInfo
                            .getErrorMessage());
                        this.identityInfoDAO.updateEntity(identityInfo);
                        this.logInfoDao.addEntity(logInfo);
                    }
                }
            } else {
                // 插入一条日志记录，状态为主表记录的状态

                // ray - published valid info which queried from db
                // others are in the listener - thirdQueueListener
                if (identityInfo.getResultType().equals(ResultType.SUCCESS)) {
                    logInfo.setResultType(ResultType.SUCCESS);
                    if (!isSynch) {//异步请求
                        this.sendMessage(identityInfo, username,
                            AsyncCode.SUCCESS);
                    }
                } else if (identityInfo.getResultType().equals(
                    ResultType.FAILURE)) {
                    logInfo.setResultType(ResultType.FAILURE);
                    if (!isSynch) {//异步请求
                        this.sendMessage(identityInfo, username,
                            AsyncCode.FAILURE);
                    }
                } else {
                    logInfo.setResultType(ResultType.QUERY);
                }
                logInfo.setRecordId(identityInfo.getId());
                logInfo.setTableName(IdentityInfoService.IDENTITYINFO);
                logInfo.setDataChannel(DataChannel.HERMES);
                logInfo.setEndTime(new Date());
                searchRecord.setIdentify_id(identityInfo.getId());
                searchRecord.setDataChannel(DataChannel.HERMES);
                searchRecord.setErrorCode(identityInfo.getErrorCode());
                searchRecord.setErrorMessage(identityInfo.getErrorMessage());
                this.logInfoDao.addEntity(logInfo);

            }
        }
        return identityInfo;
    }

    /**
     * 设置相应的xml
     *
     * @param resultXml
     * @param identityInfo
     * @param logInfo
     * @param systemId
     * @param isSynch
     *        true:同步 false：异步
     * @throws DocumentException
     * @throws ParseException
     */
    public void setIdentityInfo(String resultXml, IdentityInfo identityInfo,
            LogInfo logInfo, String systemId, boolean isSynch)
            throws DocumentException, ParseException {
        Document parseText = DocumentHelper.parseText(resultXml);
        Element rootElement = parseText.getRootElement();
        Element messageElement = rootElement.element("message");
        String isSuccess = messageElement.elementText("status");
        String isSuccessInfo = messageElement.elementText("value");
        identityInfo.setMessageStatus1(isSuccess);
        identityInfo.setMessageInfo1(isSuccessInfo);
        int id = identityInfo.getId();
        if (isSuccess.contains("0")) {// 成功返回
            Element policeCheckInfoElement = rootElement.element(
                "policeCheckInfos").element("policeCheckInfo");
            Element policeCheckInfoMessageElement = policeCheckInfoElement
                .element("message");
            String isResultSuccess = policeCheckInfoMessageElement
                .elementText("status");
            String isResultSuccessInfo = policeCheckInfoMessageElement
                .elementText("value");
            identityInfo.setMessageStatus2(isResultSuccess);
            identityInfo.setMessageInfo2(isResultSuccessInfo);
            String isStatus = policeCheckInfoElement.elementText("compStatus");
            identityInfo.setCompStatus(isStatus);
            switch (isResultSuccess) {// 是否查询到数据
            // 查询成功
                case "0":
                    switch (isStatus) {// 查到数据的类型
                    // 库存无此号
                        case "1":
                            // 日志设置
                            logInfo.setResultType(ResultType.FAILURE);
                            logInfo.setEndTime(new Date());
                            logInfo
                                .setErrorCode(HermesConsts.HERMES_GZT_DATABASENOTFOUND);
                            logInfo
                                .setErrorMessage(IdentityInfoService.QUERY_FAILED_1);
                            identityInfo
                            .setErrorCode(HermesConsts.HERMES_GZT_DATABASENOTFOUND);
                            identityInfo
                                .setErrorMessage(IdentityInfoService.QUERY_FAILED_1);
                            identityInfo.setCreateTime(new Date());
                            identityInfo.setResultType(ResultType.FAILURE);
                            // ray - send valid queried message
                            this.identityInfoDAO.updateEntity(identityInfo);
                            if (!isSynch) {
                                this.sendMessage(identityInfo, systemId,
                                    identityInfo.getKeyid(),
                                    AsyncCode.RESPONSE_NO_DETAILS);
                            }
                            break;
                        // 姓名和身份证不一致
                        case "2":
                            // 日志设置
                            logInfo.setResultType(ResultType.FAILURE);
                            logInfo.setEndTime(new Date());
                            logInfo
                                .setErrorCode(HermesConsts.HERMES_GZT_NOTMATCH);
                            logInfo
                                .setErrorMessage(IdentityInfoService.QUERY_FAILED_2);
                            identityInfo
                                .setErrorMessage(IdentityInfoService.QUERY_FAILED_2);
                            identityInfo.setCreateTime(new Date());
                            identityInfo.setResultType(ResultType.FAILURE);
                            identityInfo
                                .setErrorCode(HermesConsts.HERMES_GZT_NOTMATCH);
                            this.identityInfoDAO.updateEntity(identityInfo);

                            // ray - send valid queried message
                            if (!isSynch) {
                                this.sendMessage(identityInfo, systemId,
                                    identityInfo.getKeyid(),
                                    AsyncCode.RESPONSE_NOT_MATHCING);
                            }
                            break;
                        // 成功返回
                        case "3":
                            // 日志设置
                            logInfo.setResultType(ResultType.SUCCESS);
                            logInfo.setEndTime(new Date());
                            // identityInfo 设置
                            IdentityInfo newIdentityInfo = new IdentityInfo();
                            newIdentityInfo
                                .setDataChannel(DataChannel.GUOZHENGTONG);
                            newIdentityInfo.setName(policeCheckInfoElement
                                .elementText("name"));
                            newIdentityInfo
                                .setIdentitycard(policeCheckInfoElement
                                    .elementText("identitycard"));
                            newIdentityInfo.setAddress(policeCheckInfoElement
                                .elementText("policeadd"));
                            newIdentityInfo.setPhoto(policeCheckInfoElement
                                .elementText("checkPhoto"));
                            String sex = policeCheckInfoElement
                                .elementText("sex2");
                            newIdentityInfo
                                .setSex("男".equals(sex) ? Gender.MALE
                                    : Gender.FEMALE);
                            newIdentityInfo
                                .setBirthday(DateUtils.parseDate(
                                    policeCheckInfoElement
                                        .elementText("birthday2"), "yyyyMMdd"));
                            newIdentityInfo.setMessageStatus1(identityInfo
                                .getMessageStatus1());
                            newIdentityInfo.setMessageStatus2(identityInfo
                                .getMessageStatus2());
                            newIdentityInfo.setMessageInfo1(identityInfo
                                .getMessageInfo1());
                            newIdentityInfo.setMessageInfo2(identityInfo
                                .getMessageInfo2());
                            newIdentityInfo.setCompStatus(identityInfo
                                .getCompStatus());
                            // 新md5值
                            String newMd5 = DigestUtils.md5Hex(newIdentityInfo
                                .toString());
                            // 旧md5值
                            String oldMd5 = identityInfo.getMd5();
                            if (StringUtils.isEmpty(oldMd5)
                                || newMd5.equals(oldMd5)) {// 之前md5值为空
                                // 或
                                // md5值一致
                                // newIdentityInfo 复制到 identityInfo
                                BeanUtils.copyProperties(newIdentityInfo,
                                    identityInfo);
                                identityInfo.setId(id);
                                identityInfo.setMd5(newMd5);
                                identityInfo.setCreateTime(new Date());
                                identityInfo.setResultType(ResultType.SUCCESS);
                                identityInfo.setEnabled(true);
                                this.identityInfoDAO.updateEntity(identityInfo);

                                // ray - send valid queried message
                                if (!isSynch) {
                                    this.sendMessage(identityInfo, systemId,
                                        identityInfo.getKeyid(),
                                        AsyncCode.SUCCESS);
                                }
                            } else {// 两条数据md5不一致
                                BeanUtils.copyProperties(identityInfo,
                                    newIdentityInfo);
                                newIdentityInfo.setId(null);
                                newIdentityInfo.setCreateTime(new Date());
                                newIdentityInfo
                                    .setResultType(ResultType.SUCCESS);
                                newIdentityInfo.setEnabled(true);
                                newIdentityInfo.setMd5(newMd5);
                                id = this.identityInfoDAO
                                    .addEntity(newIdentityInfo);
                                identityInfo.setEnabled(false);
                                identityInfo.setResultType(ResultType.SUCCESS);
                                this.identityInfoDAO.updateEntity(identityInfo);

                                // ray - send new identifiInfo, the old one has been
                                // setEndabled(false)
                                /*
                                 * 虽然为新的entity，但是客户端方并未知晓新的id号，所
                                 * 以如果用REST返回的entityID作为查询记录id的话，需要返回旧的id号以便对照
                                 */
                                if (!isSynch) {
                                    this.sendMessage(newIdentityInfo, systemId,
                                        identityInfo.getKeyid(),
                                        AsyncCode.SUCCESS);
                                }

                            }
                            break;
                        default:
                            break;
                    }
                    break;
                // 未查到数据
                case "1":
                    identityInfo.setResultType(ResultType.FAILURE);
                    identityInfo.setCreateTime(new Date());
                    identityInfo.setErrorCode(HermesConsts.HERMES_GZG_NOTFOUND);
                    identityInfo.setErrorMessage(policeCheckInfoMessageElement
                        .elementText("value"));
                    this.identityInfoDAO.updateEntity(identityInfo);
                    // ray - send no detail value message to amq
                    if (!isSynch) {
                        this.sendMessage(identityInfo, systemId,
                            identityInfo.getKeyid(),
                            AsyncCode.FAILURE_EMPTYDATA);
                    }
                    logInfo.setEndTime(new Date());
                    logInfo.setResultType(identityInfo.getResultType());
                    logInfo.setErrorMessage(identityInfo.getErrorMessage());
                    logInfo.setRecordId(id);
                    break;
                // 查询失败
                case "2":
                    identityInfo.setResultType(ResultType.FAILURE);
                    identityInfo.setCreateTime(new Date());
                    identityInfo
                        .setErrorCode(HermesConsts.HERMES_GZT_SEARCHFAILD);
                    identityInfo.setErrorMessage(policeCheckInfoMessageElement
                        .elementText("value"));
                    this.identityInfoDAO.updateEntity(identityInfo);

                    // ray - send queried failed message to amq
                    if (!isSynch) {
                        this.sendMessage(identityInfo, systemId,
                            identityInfo.getKeyid(),
                            AsyncCode.FAILURE_3RD_RETURN_ERROR);
                    }
                    logInfo.setEndTime(new Date());
                    logInfo.setResultType(identityInfo.getResultType());
                    logInfo.setErrorMessage(identityInfo.getErrorMessage());
                    logInfo.setRecordId(id);
                    break;
                default:
                    break;
            }
        } else {
            identityInfo.setResultType(ResultType.FAILURE);
            identityInfo.setErrorCode(HermesConsts.HERMES_GZT_ERROR);
            identityInfo.setCreateTime(new Date());
            identityInfo.setErrorMessage(messageElement.elementText("value"));
            this.identityInfoDAO.updateEntity(identityInfo);
            if (!isSynch) {
                this.sendMessage(identityInfo, systemId,
                    identityInfo.getKeyid(), AsyncCode.FAILURE_3RD_RETURN_ERROR);
            }
            logInfo.setEndTime(new Date());
            logInfo.setResultType(ResultType.FAILURE);
            logInfo.setErrorMessage(messageElement.elementText("value"));
            logInfo.setErrorCode(isSuccess);
        }
        logInfo.setRecordId(id);
        this.logInfoDao.addEntity(logInfo);
        IdentityInfoService.logger.debug("add LogInfo <" + logInfo.getId()
            + ">");
    }

    public void sendMessage(IdentityInfo identityInfo, String username,
            AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider.packageResponse(
                identityInfo, username,
                DataChannelSubType.GUOZHENGTONG_IDENTIFY, identityInfo.getId(),
                asyncCode);
            this.provider.sendTextMessage(messageBody, username,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param resp
     * @param systemId
     * @author ray
     */
    private void sendMessage(IdentityInfo resp, String systemId,
            String requestId, AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider.packageResponse(resp,
                systemId, DataChannelSubType.GUOZHENGTONG_IDENTIFY, requestId,
                asyncCode);
            this.provider.sendTextMessage(messageBody, systemId,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
