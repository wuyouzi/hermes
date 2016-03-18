/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service;

import java.security.Principal;
import java.util.Date;

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
import com.ucredit.hermes.dao.EducationInSchoolInfoDAO;
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
 * 获取学籍信息
 *
 * @author caoming
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class EducationInSchoolInfoService {
    private static Logger logger = LoggerFactory
            .getLogger(EducationInSchoolInfoService.class);
    private final static String TABLE_NAME = "education_in_school_infos";
    @Autowired
    private EducationInSchoolInfoDAO dao;
    @Autowired
    private LogInfoDAO logInfoDao;
    @Autowired
    private ThirdQueueService thirdQueueService;
    @Autowired
    private UserDAO userDao;

    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;

    /**
     * 专线个人户籍信息
     *
     * @param operationName
     * @param name
     * @param documentNo
     * @param constraint
     * @param activeDays
     * @param newActiveDate
     * @param principal
     * @return
     */
    public EducationInfo getEducationInSchoolInfo(String operationName,
            String name, String documentNo, boolean constraint, int activeDays,
            Date newActiveDate, Principal principal) {
        String username = ((Authentication) principal).getName();
        User user = this.userDao.getUserByUserName(username);

        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
                .getDetails();
        // 记录日志
        LogInfo logInfo = new LogInfo(operationName, user.getGroup(),
            details.getRemoteAddress(), new Date(),
            EducationInSchoolInfoService.TABLE_NAME);
        ThirdQueueTaskResult result = new ThirdQueueTaskResult(name,
            documentNo, "", JMSType.IN_SCHOOL_EDUCATION_JMS_QUEUE);
        EducationInfo educationInSchoolInfo = this.dao
                .getEducationInSchoolInfo(name, documentNo);
        Date date = new Date();
        if (educationInSchoolInfo == null) {
            educationInSchoolInfo = new EducationInfo();
            educationInSchoolInfo.setDocumentNo(documentNo);
            educationInSchoolInfo.setName(name);
            educationInSchoolInfo
                .setEducationType(EducationType.EDUCATION_INSCHOOL);
            BaseReportType baseReportType = new BaseReportType();
            baseReportType.setCreateTime(date);
            baseReportType.setResultType(ResultType.QUERY);
            educationInSchoolInfo.setBaseReportType(baseReportType);
            this.dao.addEntity(educationInSchoolInfo);
            EducationInSchoolInfoService.logger
            .info("添加个人学籍信息educationInSchoolInfo<"
                    + educationInSchoolInfo.getId() + "> add");
            this.thirdQueueService.sendMessage(result, DataChannel.PENGYUAN,
                logInfo, username);
        } else {
            BaseReportType baseReportType = educationInSchoolInfo
                    .getBaseReportType();
            Date createTime = baseReportType.getCreateTime();
            // 有效期时间 是否有效判断，以有效日期为基准。当查询时间（new Date()）在查询时间之前，该数据为有效，反之亦然。
            // 当传过来的为数字，数据时间加上此天数在当前时间之后为有效，反之亦然。
            Date activeTime = DateUtils.addDays(createTime, activeDays);
            if (constraint || date.after(newActiveDate)
                    || date.after(activeTime)) {
                if (baseReportType.getResultType() != ResultType.QUERY) {
                    baseReportType.setResultType(ResultType.QUERY);
                    this.thirdQueueService.sendMessage(result,
                        DataChannel.PENGYUAN, logInfo, username);
                }
            } else {// hermes中的数据为有效数据

                if (educationInSchoolInfo.getBaseReportType().getResultType()
                    .equals(ResultType.SUCCESS)) {
                    logInfo.setResultType(ResultType.SUCCESS);
                    // ray - send valid message from db
                    this.sendMessage(educationInSchoolInfo, username,
                        educationInSchoolInfo.getId(), AsyncCode.SUCCESS);
                } else if (educationInSchoolInfo.getBaseReportType()
                    .getResultType().equals(ResultType.FAILURE)) {
                    logInfo.setResultType(ResultType.FAILURE);
                    // ray - send valid message from db
                    this.sendMessage(educationInSchoolInfo, username,
                        educationInSchoolInfo.getId(), AsyncCode.FAILURE);
                } else {
                    logInfo.setResultType(ResultType.QUERY);
                }

                logInfo.setRecordId(educationInSchoolInfo.getId());
                logInfo.setDataChannel(DataChannel.HERMES);
                logInfo.setEndTime(new Date());
                this.logInfoDao.addEntity(logInfo);
                EducationInSchoolInfoService.logger.debug("添加日志表LogInfo<"
                        + logInfo.getId() + "> add");

            }
        }
        return educationInSchoolInfo;
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
