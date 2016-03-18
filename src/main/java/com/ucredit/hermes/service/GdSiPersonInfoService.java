/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service;

import java.security.Principal;
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
import com.ucredit.hermes.dao.GdSiPersonInfoDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
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
import com.ucredit.hermes.model.pengyuan.GdSiPersonInfo;

/**
 * 广东个人社保信息service
 *
 * @author caoming
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class GdSiPersonInfoService {
    private static Logger logger = LoggerFactory
            .getLogger(GdSiPersonInfoService.class);
    private final static String TABLE_NAME = "gd_si_person_infos";
    @Autowired
    private GdSiPersonInfoDAO dao;
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
     * 获取社保信息
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
    public List<GdSiPersonInfo> getGdSiPersonInfo(String operationName,
        String name, String documentNo, boolean constraint, int activeDays,
        Date newActiveDate, Principal principal) {
        String username = ((Authentication) principal).getName();
        User user = this.userDao.getUserByUserName(username);

        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
                .getDetails();
        // 记录日志
        LogInfo logInfo = new LogInfo(operationName, user.getGroup(),
            details.getRemoteAddress(), new Date(),
            GdSiPersonInfoService.TABLE_NAME);
        ThirdQueueTaskResult result = new ThirdQueueTaskResult(name,
            documentNo, "", JMSType.GD_SI_JMS_QUEUE);
        List<GdSiPersonInfo> gdSiPersonInfos = this.dao.getGdSiPersonInfo(name,
            documentNo);

        Date date = new Date();
        if (gdSiPersonInfos.isEmpty()) {
            // 插入一条数据
            GdSiPersonInfo gdSiPersonInfo = new GdSiPersonInfo();
            gdSiPersonInfo.setName(name);
            gdSiPersonInfo.setDocumentNo(documentNo);

            BaseReportType baseReportType = new BaseReportType();
            baseReportType.setCreateTime(date);
            baseReportType.setResultType(ResultType.QUERY);
            baseReportType.setDataChannel(DataChannel.PENGYUAN);
            gdSiPersonInfo.setBaseReportType(baseReportType);

            this.dao.addEntity(gdSiPersonInfo);

            GdSiPersonInfoService.logger.debug("添加广东个人社保信息GdSiPersonInfo<"
                    + gdSiPersonInfo.getId() + "> add");
            this.thirdQueueService.sendMessage(result, DataChannel.PENGYUAN,
                logInfo, username);
        } else {

            GdSiPersonInfo gdSiPersonInfo = gdSiPersonInfos.get(0);
            BaseReportType baseReportType = gdSiPersonInfo.getBaseReportType();
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

                if (gdSiPersonInfo.getBaseReportType().getResultType()
                    .equals(ResultType.SUCCESS)) {
                    logInfo.setResultType(ResultType.SUCCESS);
                    // ray - send message from db
                    // only one message because gdSiPersonInfos.get(0)
                    this.sendMessage(gdSiPersonInfo, username,
                        gdSiPersonInfo.getId(), AsyncCode.SUCCESS);
                } else if (gdSiPersonInfo.getBaseReportType().getResultType()
                    .equals(ResultType.FAILURE)) {
                    logInfo.setResultType(ResultType.FAILURE);
                    // ray - send message from db
                    // only one message because gdSiPersonInfos.get(0)
                    this.sendMessage(gdSiPersonInfo, username,
                        gdSiPersonInfo.getId(), AsyncCode.FAILURE);
                } else {
                    logInfo.setResultType(ResultType.QUERY);
                }
                logInfo.setDataChannel(DataChannel.HERMES);
                logInfo.setRecordId(gdSiPersonInfo.getId());
                logInfo.setEndTime(new Date());
                this.logInfoDao.addEntity(logInfo);
                GdSiPersonInfoService.logger.debug("添加日志表LogInfo<"
                        + logInfo.getId() + "> add");

            }
        }
        return gdSiPersonInfos;
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
