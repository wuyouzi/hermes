/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.Destination;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.dao.EducationInfoDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.UserDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.EducationType;
import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.User;
import com.ucredit.hermes.model.pengyuan.BaseReportType;
import com.ucredit.hermes.model.pengyuan.EducationInfo;

/**
 * 学历信息的service
 *
 * @author caoming
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class EducationInfoService {
    private static Logger logger = LoggerFactory
            .getLogger(EducationInfoService.class);
    private final static String TABLE_NAME = "educational_infos";
    @Autowired
    private LogInfoDAO logInfoDao;
    @Autowired
    private ThirdQueueService thirdQueueService;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private EducationInfoDAO dao;

    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;

    /**
     * @param operationName
     *        查询人姓名
     * @param name
     *        被查询者的姓名
     * @param documentNo
     *        被查询者的证件号
     * @param unitName
     *        查询机构
     * @param queryUserID
     *        操作员登录名
     * @param newReceiveTime
     * @param levelNo
     *        被查询者的学历证编号
     * @param graduateYear
     *        被查询者的毕业年份
     * @param college
     *        被查询者的院校
     * @param constraint
     *        是否强制刷新
     * @param activeDays
     *        有效天数
     * @param newActiveDate
     * @param lendRequestId
     * @param principal
     * @return
     * @throws IllegalArgumentException
     */
    public List<EducationInfo> getEducationInfo(String operationName,
        String name, String documentNo, String unitName,
        String queryUserID, Date newReceiveTime, String levelNo,
        String keyid, int graduateYear, String college, boolean constraint,
        int activeDays, Date newActiveDate, String lendRequestId,
        Principal principal) throws IllegalArgumentException {
        if (documentNo == null || documentNo.length() != 18
                || documentNo.charAt(0) == '0') {
            List<EducationInfo> eList = new ArrayList<>();
            EducationInfo e = new EducationInfo();
            BaseReportType baseReportType = new BaseReportType();
            baseReportType.setResultType(ResultType.FAILURE);
            baseReportType.setErrorMessage("身份证参数错误！");
            baseReportType.setEnabled(true);
            e.setBaseReportType(baseReportType);
            eList.add(e);
            return eList;
        }
        String username = ((Authentication) principal).getName();
        User user = this.userDao.getUserByUserName(username);

        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
                .getDetails();
        //记录日志
        LogInfo logInfo = new LogInfo(operationName, user.getGroup(),
            details.getRemoteAddress(), new Date(),
            EducationInfoService.TABLE_NAME);
        logInfo.setLendRequestId(lendRequestId);
        ThirdQueueTaskResult result = new ThirdQueueTaskResult(name,
            documentNo, unitName, queryUserID, newReceiveTime, levelNo,
            graduateYear, college, JMSType.LAST_EDUCATION_JMS_QUEUE);
        result.setKeyid(keyid);
        List<EducationInfo> educationInfos = this.dao.getEducationInfo(name,
            documentNo, unitName, queryUserID, newReceiveTime, levelNo,
            graduateYear, college);
        Date date = new Date();
        /*
         * if (!educationInfos.isEmpty()
         * && !"".equals(educationInfos.get(0).getBaseReportType()
         * .getErrorMessage())) {
         * return educationInfos;
         * }
         */
        if (educationInfos.isEmpty()) {

            EducationInfo educationInfo = new EducationInfo();
            educationInfo.setDocumentNo(documentNo);
            educationInfo.setName(name);
            educationInfo.setCollege(college);
            educationInfo.setGraduateTime(graduateYear);
            educationInfo.setEducationType(EducationType.EDUCATION);
            //基本信息的设置
            BaseReportType baseReportType = new BaseReportType();
            baseReportType.setCreateTime(date);
            baseReportType.setDataChannel(DataChannel.PENGYUAN);
            baseReportType.setResultType(ResultType.QUERY);
            educationInfo.setBaseReportType(baseReportType);
            educationInfo.setLendRequestId(lendRequestId);
            educationInfo.setKeyid(keyid);
            this.dao.addEntity(educationInfo);
            educationInfos.add(educationInfo);

            EducationInfoService.logger.debug("添加学历的信息：EducationInfo<"
                    + educationInfo.getId() + "> add");
            String birYear = documentNo.length() == 18 ? documentNo.substring(
                6, 10) : "19" + documentNo.substring(6, 8);
                if (Integer.parseInt(birYear) < 1978) {
                    baseReportType.setResultType(ResultType.FAILURE);
                    baseReportType.setEnabled(false);
                    baseReportType.setErrorMessage("出生于1978年之前，不发起查询");
                    return educationInfos;
                }
                this.thirdQueueService.sendMessage(result, DataChannel.PENGYUAN,
                    logInfo, username);
        } else {
            //更新lendRequestId 值为当前查询的值
            educationInfos.get(0).setLendRequestId(lendRequestId);
            BaseReportType baseReportType = educationInfos.get(0)
                    .getBaseReportType();
            Date createTime = baseReportType.getCreateTime();

            // 有效期时间  是否有效判断，以有效日期为基准。当查询时间（new Date()）在查询时间之前，该数据为有效，反之亦然。
            // 当传过来的为数字，数据时间加上此天数在当前时间之后为有效，反之亦然。
            Date activeTime = DateUtils.addDays(createTime, activeDays);
            if (constraint || date.after(newActiveDate)
                    || date.after(activeTime)) {
                if (baseReportType.getResultType() != ResultType.QUERY) {
                    // liuxiao 这里需要增加将旧信息置为disabled的消息
                    baseReportType.setEnabled(false);
                    for (EducationInfo info : educationInfos) {
                        info.setBaseReportType(baseReportType);
                    }
                    educationInfos = new ArrayList<>();
                    EducationInfo educationInfo = new EducationInfo();
                    educationInfo.setDocumentNo(documentNo);
                    educationInfo.setName(name);
                    educationInfo.setCollege(college);
                    educationInfo.setGraduateTime(graduateYear);
                    educationInfo.setEducationType(EducationType.EDUCATION);
                    //基本信息的设置
                    BaseReportType baseReportTypeNew = new BaseReportType();
                    baseReportTypeNew.setCreateTime(date);
                    baseReportTypeNew.setEnabled(true);
                    baseReportTypeNew.setDataChannel(DataChannel.PENGYUAN);
                    baseReportTypeNew.setResultType(ResultType.QUERY);
                    educationInfo.setBaseReportType(baseReportTypeNew);
                    educationInfo.setLendRequestId(lendRequestId);
                    educationInfo.setKeyid(keyid);
                    this.dao.addEntity(educationInfo);
                    educationInfos.add(educationInfo);
                    this.thirdQueueService.sendMessage(result,
                        DataChannel.PENGYUAN, logInfo, username);
                }
            } else {
                //未超时
                logInfo.setRecordId(educationInfos.get(0).getId());
                logInfo.setEndTime(new Date());
                logInfo.setDataChannel(DataChannel.HERMES);
                //未超时也有不可用的情况
                //判断下不可用的情况吧

                if (educationInfos.get(0).getBaseReportType().getResultType()
                        .equals(ResultType.FAILURE)) {
                    logInfo.setResultType(ResultType.FAILURE);
                    this.logInfoDao.addEntity(logInfo);
                    if (educationInfos.get(0).getBaseReportType()
                        .getErrorMessage() != null
                        && educationInfos.get(0).getBaseReportType()
                            .getErrorMessage()
                            .equals(HermesConsts.HERMES_EDUCATION_NOT_FOUND)) {
                        EducationInfoService.logger
                            .info("-----------------------service鹏元学历发送FAILURE_EMPTYDATA"
                                + educationInfos.get(0).getId());
                        this.sendMessage(educationInfos.get(0), username,
                            educationInfos.get(0).getId(),
                            AsyncCode.FAILURE_EMPTYDATA);
                    } else { //不可用和故障任意情况出现，即重查
                        EducationInfoService.logger
                        .info("-----------------------service鹏元学历发送FAILURE"
                                + educationInfos.get(0).getId());
                        this.sendMessage(educationInfos.get(0), username,
                            educationInfos.get(0).getId(), AsyncCode.FAILURE);
                    }

                } else if (educationInfos.get(0).getBaseReportType()
                        .getResultType().equals(ResultType.SUCCESS)) {

                    //可用的时候
                    logInfo.setResultType(ResultType.SUCCESS);
                    this.logInfoDao.addEntity(logInfo);
                    // ray - send valid info
                    EducationInfoService.logger
                    .info("-----------------------service鹏元学历发送success"
                            + educationInfos.get(0).getId());
                    this.sendMessage(educationInfos.get(0), username,
                        educationInfos.get(0).getId(), AsyncCode.SUCCESS);
                    EducationInfoService.logger
                    .info("-----------------------service鹏元学历结束success"
                            + educationInfos.get(0).getId());
                } else {
                    logInfo.setResultType(ResultType.QUERY);
                }
                EducationInfoService.logger.debug("添加日志表LogInfo<"
                        + logInfo.getId() + "> add");
            }
        }

        return educationInfos;
    }

    public EducationInfo updateErrorEducation(String name, String documentNo) {
        EducationInfo edu = this.dao.getErrorEducationInfo(name, documentNo);
        BaseReportType br = edu.getBaseReportType();
        br.setEnabled(false);
        br.setResultType(ResultType.FAILURE);
        br.setErrorMessage("链接Mq异常");
        edu.setBaseReportType(br);
        return edu;
    }

    private void sendMessage(EducationInfo resp, String systemId,
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
