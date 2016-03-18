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
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.PoliceResidenceInfoDAO;
import com.ucredit.hermes.dao.UserDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.User;
import com.ucredit.hermes.model.pengyuan.BaseReportType;
import com.ucredit.hermes.model.pengyuan.PoliceResidenceInfo;

/**
 * 专线个人户籍信息
 *
 * @author caoming
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class PoliceResidenceInfoService {
    private static Logger logger = LoggerFactory
        .getLogger(PoliceResidenceInfoService.class);
    private final static String TABLE_NAME = "police_residence_infos";

    @Autowired
    private PoliceResidenceInfoDAO dao;
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
     * @param activeDate
     * @param principal
     * @return
     */
    public PoliceResidenceInfo getPoliceResidenceInfo(String operationName,
            String name, String documentNo, boolean constraint, int activeDays,
            Date activeDate, Principal principal) {
        String username = ((Authentication) principal).getName();
        User user = this.userDao.getUserByUserName(username);

        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
            .getDetails();
        // 记录日志
        LogInfo logInfo = new LogInfo(operationName, user.getGroup(),
            details.getRemoteAddress(), new Date(),
            PoliceResidenceInfoService.TABLE_NAME);
        ThirdQueueTaskResult result = new ThirdQueueTaskResult(name,
            documentNo, "", JMSType.POLICE_JMS_QUEUE);
        PoliceResidenceInfo policeInfo = this.dao.getPoliceResidenceInfo(name,
            documentNo);

        Date date = new Date();
        if (policeInfo == null) {
            // 插入一条数据
            policeInfo = new PoliceResidenceInfo();
            policeInfo.setDocumentNo(documentNo);
            policeInfo.setName(name);
            BaseReportType baseReportType = new BaseReportType();
            baseReportType.setCreateTime(date);
            baseReportType.setResultType(ResultType.QUERY);
            baseReportType.setDataChannel(DataChannel.PENGYUAN);
            policeInfo.setBaseReportType(baseReportType);
            this.dao.addEntity(policeInfo);
            PoliceResidenceInfoService.logger
                .info("添加个人户籍信息PoliceResidenceInfo<" + policeInfo.getId()
                    + "> add");
            this.thirdQueueService.sendMessage(result, DataChannel.PENGYUAN,
                logInfo, username);
        } else {
            Date createTime = policeInfo.getBaseReportType().getCreateTime();
            // 有效期时间 是否有效判断，以有效日期为基准。当查询时间（new Date()）在查询时间之前，该数据为有效，反之亦然。
            // 当传过来的为数字，数据时间加上此天数在当前时间之后为有效，反之亦然。
            Date activeTime = DateUtils.addDays(createTime, activeDays);
            if (constraint || date.after(activeDate) || date.after(activeTime)) {
                policeInfo.getBaseReportType().setResultType(ResultType.QUERY);
                this.thirdQueueService.sendMessage(result,
                    DataChannel.PENGYUAN, logInfo, username);
            } else {// hermes中的数据为有效数据

                logInfo.setRecordId(policeInfo.getId());
                logInfo.setDataChannel(DataChannel.HERMES);
                logInfo.setEndTime(new Date());

                if (policeInfo.getBaseReportType().getResultType()
                    .equals(ResultType.SUCCESS)
                    && policeInfo.getBaseReportType().isEnabled()) {
                    logInfo.setResultType(ResultType.SUCCESS);
                    // ray - send valid info queried from db
                    this.sendMessage(policeInfo, username, policeInfo.getId(),
                        AsyncCode.SUCCESS);
                } else if (policeInfo.getBaseReportType().getResultType()
                        .equals(ResultType.FAILURE)) {
                    logInfo.setResultType(ResultType.FAILURE);
                    // ray - send valid info queried from db
                    this.sendMessage(policeInfo, username, policeInfo.getId(),
                        AsyncCode.FAILURE);
                } else {
                    logInfo.setResultType(ResultType.QUERY);
                }
                this.logInfoDao.addEntity(logInfo);
                PoliceResidenceInfoService.logger.debug("添加日志表LogInfo<"
                    + logInfo.getId() + "> add");
            }
        }
        return policeInfo;
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
