package com.ucredit.hermes.third.jms.crawl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ucredit.hermes.dao.CompanyContactOperateInfoDAO;
import com.ucredit.hermes.dao.CompanyInfoDAO;
import com.ucredit.hermes.dao.CompanyManagerInfoDAO;
import com.ucredit.hermes.dao.CompanyShareholderInfoDAO;
import com.ucredit.hermes.dao.CorpTypeInfoDAO;
import com.ucredit.hermes.dao.CurrencyInfoDAO;
import com.ucredit.hermes.dao.PositionInfoDAO;
import com.ucredit.hermes.dao.TradeInfoDAO;
import com.ucredit.hermes.dao.crawl.SearchRelationDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.CompanyOperateType;
import com.ucredit.hermes.enums.CompanyStatus;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.crawl.company.CompanyContactOperateInfos;
import com.ucredit.hermes.model.crawl.company.CompanyInfos;
import com.ucredit.hermes.model.crawl.company.CompanyManagerInfos;
import com.ucredit.hermes.model.crawl.company.CompanyShareholderInfos;
import com.ucredit.hermes.model.crawl.company.ResponseFromCrawl;
import com.ucredit.hermes.model.crawl.company.SearchRelation;
import com.ucredit.hermes.model.pengyuan.CompanyContactOperateInfo;
import com.ucredit.hermes.model.pengyuan.CompanyInfo;
import com.ucredit.hermes.model.pengyuan.CompanyManagerInfo;
import com.ucredit.hermes.model.pengyuan.CompanyShareholderInfo;
import com.ucredit.hermes.model.pengyuan.CorpTypeInfo;
import com.ucredit.hermes.model.pengyuan.CurrencyInfo;
import com.ucredit.hermes.model.pengyuan.PositionInfo;
import com.ucredit.hermes.model.pengyuan.TradeInfo;
import com.ucredit.hermes.service.ThirdQueueService;
import com.ucredit.hermes.service.crawl.PengyuanCompanyServace;

/**
 * 监听爬取返回的信息进行解析，解析有误发起鹏元查询
 *
 * @author zhouwuyuan
 */
@Component("companyFromCrawlJMSQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class CompanyFromCrawlJMSQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
        .getLogger(CompanyFromCrawlJMSQueueListener.class);
    @Autowired
    private CompanyInfoDAO companyInfoDao;
    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;
    @Autowired
    private CompanyManagerInfoDAO companyManagerInfoDao;
    @Autowired
    private CompanyShareholderInfoDAO companyShareholderInfoDao;
    @Autowired
    private CompanyContactOperateInfoDAO companyContactOperateInfoDao;
    @Autowired
    private TradeInfoDAO tradeInfoDAO;
    @Autowired
    private CorpTypeInfoDAO corpTypeInfoDAO;
    @Autowired
    private PositionInfoDAO positionInfoDAO;
    @Autowired
    private CurrencyInfoDAO currencyInfoDAO;
    @Autowired
    private PengyuanCompanyServace pengyuanCompanyServace;
    @Autowired
    private SearchRelationDAO searchRelationDAO;
    @Autowired
    private ThirdQueueService thirdQueueService;

    @Override
    public void onMessage(Message message) {
        CompanyInfo dbcompanyInfo = null;
        int companyid = 0;
        try {
            String data = "";
            if (message instanceof ObjectMessage) {
                ObjectMessage om = (ObjectMessage) message;
                data = om.toString();
            } else if (message instanceof TextMessage) {
                data = ((TextMessage) message).getText();
            }
            CompanyFromCrawlJMSQueueListener.logger.info("接收到消息：" + data);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ResponseFromCrawl reportInfo = gson.fromJson(data,
                ResponseFromCrawl.class);
            CompanyInfos comC = reportInfo.getJson();
            companyid = Integer.parseInt(comC.getCompanyid());
            dbcompanyInfo = this.companyInfoDao.getEntityByDbId(companyid);
            if (dbcompanyInfo == null) {
                throw new Exception(String.format("根据id=%d没有查询到公司", companyid));
            }
            //爬取平台返回信息正确
            if ("0".equals(reportInfo.getStatus())) {//对爬取回来的工商信息进行解析
                dbcompanyInfo.getKeyid();
                dbcompanyInfo = (CompanyInfo) this.convertConpany(comC,
                    dbcompanyInfo);
                dbcompanyInfo.setId(companyid);
                dbcompanyInfo = this.companyInfoAssignment(comC, dbcompanyInfo);

                //爬取成功，去鹏元查询90035报告
                SearchRelation searchRelation = this.searchRelationDAO
                    .getSearchRelationByFkId(companyid);
                this.thirdQueueService.sendMessageV2(companyid,
                    searchRelation.getOperationName(), null,
                    searchRelation.getIp(), searchRelation.getLendRequestId(),
                    searchRelation.getSystemId(), "report90035");
            } else {//爬取平台返回没有信息
                dbcompanyInfo.setResultType(ResultType.FAILURE);
                dbcompanyInfo.setEnabled(false);
                dbcompanyInfo.setErrorMessage("mq中爬取平台返回status！=0");
                //重新发起查询，走鹏元流程
                SearchRelation searchRelation = this.searchRelationDAO
                    .getSearchRelationByFkId(companyid);
                this.pengyuanCompanyServace.searchFromPengyuan(
                    searchRelation.getOperationName(), null,
                    searchRelation.getIp(), searchRelation.getLendRequestId(),
                    searchRelation.getSystemId(),
                    dbcompanyInfo.getCompanyName(), dbcompanyInfo.getKeyid());
            }
        } catch (Exception e) {
            CompanyFromCrawlJMSQueueListener.logger.error("工商爬取信息mq获取查询条件出错"
                + e.getMessage());
            if (dbcompanyInfo != null) {
                dbcompanyInfo.setResultType(ResultType.FAILURE);
                dbcompanyInfo.setEnabled(false);
                dbcompanyInfo.setErrorMessage("mq中爬取平台" + e.getMessage());
                //重新发起查询，走鹏元流程
                SearchRelation searchRelation = this.searchRelationDAO
                    .getSearchRelationByFkId(companyid);
                this.pengyuanCompanyServace.searchFromPengyuan(
                    searchRelation.getOperationName(), null,
                    searchRelation.getIp(), searchRelation.getLendRequestId(),
                    searchRelation.getSystemId(),
                    dbcompanyInfo.getCompanyName(), dbcompanyInfo.getKeyid());
            }
            e.printStackTrace();
        }

    }

    /**
     * 将爬取过来的company对象和本地存储对象相同属性做一个映射
     *
     * @param crawl
     *        爬取的对象
     * @param local
     *        本地存储对象
     * @return
     */
    public Object convertConpany(Object crawl, Object local) {
        //获得爬取对象的属性
        Field[] fieldsCrawl = crawl.getClass().getDeclaredFields();
        //获得本地存储对象的属性
        Field[] fieldsCom = local.getClass().getDeclaredFields();
        for (Field field : fieldsCrawl) {
            String fieldName = field.getName();
            for (Field fieldC : fieldsCom) {
                String fieldNameC = fieldC.getName();
                //当crawl对象和本地对象的属性和类型都相同时，值做映射
                if (fieldName.equals(fieldNameC)
                        && field.getType().toString()
                        .equals(fieldC.getType().toString())) {
                    try {
                        Field fieldData = local.getClass().getDeclaredField(
                            fieldNameC);
                        fieldData.setAccessible(true);
                        field.setAccessible(true);
                        Object value = field.get(crawl);
                        fieldData.set(local, value);
                    } catch (NoSuchFieldException | SecurityException
                            | IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return local;
    }

    /**
     * 可以映射的属性值处理完后进行其他值处理
     *
     * @param com
     * @param comL
     * @throws ParseException
     */
    public CompanyInfo companyInfoAssignment(CompanyInfos com, CompanyInfo comL)
            throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if (com.getManageBeginDate() != null
                && !"".equals(com.getManageBeginDate())) {
            comL.setManageBeginDate(sf.parse(com.getManageBeginDate()));
        }
        if (com.getManageEndDate() != null
                && !"".equals(com.getManageEndDate())) {
            comL.setManageEndDate(sf.parse(com.getManageEndDate()));
        }
        if (com.getOpenDate() != null && !"".equals(com.getOpenDate())) {
            comL.setOpenDate(sf.parse(com.getOpenDate()));
        }
        if (com.getRegistDate() != null && !"".equals(com.getRegistDate())) {
            comL.setRegistDate(sf.parse(com.getRegistDate()));
        }
        if (com.getRegistFund() != null) {
            comL.setRegistFund(new BigDecimal(com.getRegistFund().trim()));
        }
        if (com.getRegistFund2() != null) {
            comL.setRegistFund2(new BigDecimal(com.getRegistFund2().trim()));
        }
        String comStatus = com.getStatus();
        String comStatus2 = com.getStatus();
        comL.setStatus(CompanyStatus.getCompanyStatus(comStatus));
        comL.setStatus2(CompanyStatus.getCompanyStatus(comStatus2));
        String tradeCode_id = com.getTradeCode_id();
        if (tradeCode_id != null && !"".equals(tradeCode_id)) {
            TradeInfo tradeInfo = this.tradeInfoDAO.getEntityByID(Integer
                .parseInt(tradeCode_id));
            comL.setTradeCode(tradeInfo);
        }
        String corpType_id = com.getCorpType_id();
        if (corpType_id != null && !"".equals(corpType_id)) {
            CorpTypeInfo c = this.corpTypeInfoDAO.getEntityByID(Integer
                .parseInt(corpType_id));
            comL.setCorpType(c);
        }
        //companyContactOperateInfo赋值
        CompanyContactOperateInfos companyContactOperateInfos = com
                .getCompanyContactOperateInfos();
        CompanyContactOperateInfo companyContactOperateInfo = new CompanyContactOperateInfo();
        companyContactOperateInfo = (CompanyContactOperateInfo) this
                .convertConpany(companyContactOperateInfos,
                    companyContactOperateInfo);
        String type = companyContactOperateInfos.getType();
        companyContactOperateInfo.setType(CompanyOperateType
            .getCompanyOperateType(type));
        Set<CompanyContactOperateInfo> companyContactOperateInfosL = new HashSet<>();
        companyContactOperateInfo.setCompanyInfos_id(comL.getId());
        this.companyContactOperateInfoDao.addEntity(companyContactOperateInfo);
        companyContactOperateInfosL.add(companyContactOperateInfo);
        comL.setCompanyContactOperateInfos(companyContactOperateInfosL);
        //managerInfolist 赋值
        List<CompanyManagerInfos> companyManagerInfos = com
                .getManagerInfolist();
        Set<CompanyManagerInfo> companyManagerInfosL = new HashSet<>();
        for (CompanyManagerInfos c : companyManagerInfos) {
            CompanyManagerInfo cl = new CompanyManagerInfo();
            cl = (CompanyManagerInfo) this.convertConpany(c, cl);
            cl.setCompanyInfos_id(comL.getId());
            //position赋值
            String position_id = c.getPosition_id();
            if (position_id != null && !"".equals(position_id)) {
                PositionInfo p = this.positionInfoDAO.getEntityByID(Integer
                    .parseInt(position_id));
                cl.setPosition(p);
            }
            this.companyManagerInfoDao.addEntity(cl);
            companyManagerInfosL.add(cl);
        }
        comL.setCompanyManagerInfos(companyManagerInfosL);
        //shareholderInfolist 赋值
        List<CompanyShareholderInfos> shareholderInfolist = com
                .getShareholderInfolist();
        Set<CompanyShareholderInfo> shareholderInfolistL = new HashSet<>();
        for (CompanyShareholderInfos c : shareholderInfolist) {
            CompanyShareholderInfo cl = new CompanyShareholderInfo();
            cl = (CompanyShareholderInfo) this.convertConpany(c, cl);
            cl.setCompanyInfos_id(comL.getId());
            String fundCurrency_id = c.getFundCurrency_id();
            if (fundCurrency_id != null && !"".equals(fundCurrency_id)) {
                CurrencyInfo currencyInfo = this.currencyInfoDAO
                        .getEntityByID(Integer.parseInt(fundCurrency_id));
                comL.setFundCurrency(currencyInfo);
            }
            String contributiveFund = c.getContributiveFund();
            if (contributiveFund != null) {
                cl.setContributiveFund(new BigDecimal(contributiveFund));
            }
            String contributivePercent = c.getContributivePercent();
            if (contributivePercent != null) {
                cl.setContributivePercent(new BigDecimal(contributivePercent));
            }
            this.companyShareholderInfoDao.addEntity(cl);
            shareholderInfolistL.add(cl);
        }
        comL.setCompanyShareholderInfos(shareholderInfolistL);
        return comL;
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
