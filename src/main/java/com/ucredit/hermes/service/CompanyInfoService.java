/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.ucredit.hermes.dao.CompanyInfoDAO;
import com.ucredit.hermes.dao.UserDAO;
import com.ucredit.hermes.dao.crawl.SearchRelationDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.QueryType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.User;
import com.ucredit.hermes.model.UserGroup;
import com.ucredit.hermes.model.crawl.company.SearchRelation;
import com.ucredit.hermes.model.pengyuan.CompanyInfo;
import com.ucredit.hermes.third.jms.ThirdGenerator;

/**
 * 企业相关信息: service
 *
 * @author liuqianqian
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class CompanyInfoService {
    private static Logger logger = LoggerFactory
        .getLogger(CompanyInfoService.class);
    @Autowired
    private CompanyInfoDAO companyInfoDao;
    @Autowired
    private ThirdQueueService thirdQueueService;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private SearchRelationDAO searchRelationDAO;

    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;

    /**
     * 根据企业名称，注册号，组织机构代码查询
     *
     * @param operationName
     * @param companyName
     *        公司名称
     * @param orgCode
     *        组织机构代码
     * @param registerNo
     *        注册号
     * @param constraint
     *        强制刷新
     * @param activeDays
     *        有效天数
     * @param activeDate
     *        有效日期
     * @param lendRequestId
     * @param principal
     * @return CompanyInfo
     */
    public CompanyInfo getCompanyInfos(String operationName,
            String companyName, String keyid, String orgCode,
            String registerNo, boolean constraint, int activeDays,
            Date activeDate, String lendRequestId, Principal principal) {

        String username = ((Authentication) principal).getName();
        User user = this.userDao.getUserByUserName(username);

        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
            .getDetails();

        UserGroup userGroup = user.getGroup();
        String ip = details.getRemoteAddress();
        CompanyInfo dbCompanyInfo = this.companyInfoDao.getCompanyInfosFilter(
            companyName, orgCode, registerNo);
        Date now = new Date();
        String refId = ThirdGenerator.getBatchNumber(QueryType.CHECK_COMPANY,
            DataChannel.PENGYUAN);
        if (dbCompanyInfo == null) {
            // 插入数据库一条记录，状态为查询中
            dbCompanyInfo = new CompanyInfo();
            dbCompanyInfo.setCompanyName(companyName);
            dbCompanyInfo.setOrgCode(orgCode);
            dbCompanyInfo.setRegisterNo(registerNo);
            dbCompanyInfo.setCreateTime(now);
            dbCompanyInfo.setLastUpdateTime(now);
            dbCompanyInfo.setDataChannel(DataChannel.CRAWL);
            dbCompanyInfo.setResultType(ResultType.QUERY);
            dbCompanyInfo.setRefId(refId);
            dbCompanyInfo.setLendRequestId(lendRequestId);
            dbCompanyInfo.setPengYuanCompanyName(companyName);
            dbCompanyInfo.setKeyid(keyid);

            int companyid = this.companyInfoDao.addEntity(dbCompanyInfo);

            SearchRelation searchRelation = new SearchRelation();
            searchRelation.setFk_id(companyid);
            searchRelation.setIp(ip);
            searchRelation.setLendRequestId(lendRequestId);
            searchRelation.setOperationName(operationName);
            searchRelation.setSystemId(username);
            this.searchRelationDAO.addEntity(searchRelation);
            CompanyInfoService.logger.debug("添加企业基本信息CompanyInfo <"
                + dbCompanyInfo.getId() + "> add");
            // 通过jms发请求
            this.thirdQueueService.sendMessageCrawl(companyid, operationName,
                userGroup, ip, lendRequestId, username);
            this.companyInfoDao.updateEntity(dbCompanyInfo);
        } else {
            // not null
            Date lastUpdateTime = dbCompanyInfo.getLastUpdateTime();
            // 有效期时间 是否有效判断，以有效日期为基准。当查询时间（new Date()）在查询时间之前，该数据为有效，反之亦然。
            // 当传过来的为数字，数据时间加上此天数在当前时间之后为有效，反之亦然。
            Date activeTime = DateUtils.addDays(lastUpdateTime, activeDays);
            // ready to send query to 3rd
            if (constraint || now.after(activeDate) || now.after(activeTime)) {
                if (dbCompanyInfo.getResultType() != ResultType.QUERY) {
                    // send message, disable old
                    dbCompanyInfo.setEnabled(false);
                    this.companyInfoDao.updateEntity(dbCompanyInfo);
                    /**
                     * 新建查询企业，通过refId与返回结果关联
                     */
                    CompanyInfo newCompanyInfo = new CompanyInfo();
                    newCompanyInfo.setCompanyName(companyName);
                    newCompanyInfo.setOrgCode(orgCode);
                    newCompanyInfo.setRegisterNo(registerNo);
                    newCompanyInfo.setCreateTime(now);
                    newCompanyInfo.setLastUpdateTime(now);
                    newCompanyInfo.setDataChannel(DataChannel.CRAWL);
                    newCompanyInfo.setResultType(ResultType.QUERY);
                    newCompanyInfo.setRefId(refId);
                    newCompanyInfo.setPengYuanCompanyName(companyName);
                    newCompanyInfo.setKeyid(keyid);
                    int companyid = this.companyInfoDao
                        .addEntity(newCompanyInfo);
                    SearchRelation searchRelation = new SearchRelation();
                    searchRelation.setFk_id(companyid);
                    searchRelation.setIp(ip);
                    searchRelation.setLendRequestId(lendRequestId);
                    searchRelation.setOperationName(operationName);
                    searchRelation.setSystemId(username);
                    this.searchRelationDAO.addEntity(searchRelation);
                    this.thirdQueueService.sendMessageCrawl(companyid,
                        operationName, userGroup, ip, lendRequestId, username);
                    return newCompanyInfo;
                } else {
                    CompanyInfoService.logger.debug("该数据还未从鹏元返回数据");
                    return dbCompanyInfo;
                }

            } else {
                // local and valid

                if (dbCompanyInfo.getResultType().equals(ResultType.SUCCESS)) {
                    CompanyInfoService.logger
                        .info("----------------------service开始鹏元工商返回success"
                            + dbCompanyInfo.getId());
                    this.sendMessage(dbCompanyInfo, username,
                        dbCompanyInfo.getId(), AsyncCode.SUCCESS);
                    CompanyInfoService.logger
                        .info("-----------------------service结束鹏元工商返回success"
                            + dbCompanyInfo.getId());
                } else if (dbCompanyInfo.getResultType().equals(
                    ResultType.FAILURE)) {
                    if (dbCompanyInfo.getErrorMessage() != null
                        && HermesConsts.HERMES_COMPANY_NOT_FOUND
                            .equals(dbCompanyInfo.getErrorMessage())) {
                        CompanyInfoService.logger
                            .info("--------------------service结束鹏元工商返回FAILURE_EMPTYDATA"
                                + dbCompanyInfo.getId());
                        this.sendMessage(dbCompanyInfo, username,
                            dbCompanyInfo.getId(), AsyncCode.FAILURE_EMPTYDATA);
                    } else {
                        CompanyInfoService.logger
                            .info("---------------------service结束鹏元工商返回FAILURE"
                                + dbCompanyInfo.getId());
                        this.sendMessage(dbCompanyInfo, username,
                            dbCompanyInfo.getId(), AsyncCode.FAILURE);
                    }
                }
                // ray send valid message
                return dbCompanyInfo;
            }
        }
        return dbCompanyInfo;
    }

    public CompanyInfo updateErrorCompany(String companyname) {
        CompanyInfo com = this.companyInfoDao.getEntityByDbName(companyname);
        com.setEnabled(false);
        com.setResultType(ResultType.FAILURE);
        com.setErrorMessage("链接Mq异常");
        return com;

    }

    /**
     * 根据企业名称模糊查询
     *
     * @param companyName
     *        企业名称
     * @return List
     */
    public List<Map<String, Object>> getCompanyByNames(String companyName) {
        return this.companyInfoDao.getCompanyByNames(companyName);
    }

    public List<Map<String, Object>> getCompanyByNames(String companyName,
            Principal principal) {
        String user = principal.getName();
        List<Map<String, Object>> companyInfoList = this
            .getCompanyByNames(companyName);
        this.sendMessage(companyInfoList, user, -1, AsyncCode.SUCCESS);
        return companyInfoList;
    }

    private void sendMessage(CompanyInfo resp, String systemId, int requestId,
            AsyncCode asyncCode) {
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

    private void sendMessage(List<Map<String, Object>> resp, String systemId,
            int requestId, AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider.packageResponse(resp,
                systemId, DataChannelSubType.PENGYUAN_COMPANY_FUZZY, requestId,
                asyncCode);
            this.provider.sendTextMessage(messageBody, systemId,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
