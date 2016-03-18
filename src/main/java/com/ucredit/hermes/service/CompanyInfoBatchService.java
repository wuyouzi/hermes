/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.dao.CompanyContactOperateInfoDAO;
import com.ucredit.hermes.dao.CompanyManagerInfoDAO;
import com.ucredit.hermes.dao.CompanyShareholderInfoDAO;
import com.ucredit.hermes.dao.CorpTypeInfoDAO;
import com.ucredit.hermes.dao.CurrencyInfoDAO;
import com.ucredit.hermes.dao.EconomicTypeInfoDAO;
import com.ucredit.hermes.dao.OrganTypeInfoDAO;
import com.ucredit.hermes.dao.PengYuanCountDAO;
import com.ucredit.hermes.dao.PengYuanReportRecordDAO;
import com.ucredit.hermes.dao.PositionInfoDAO;
import com.ucredit.hermes.dao.SubReportInfoDAO;
import com.ucredit.hermes.dao.TradeInfoDAO;
import com.ucredit.hermes.enums.CompanyOperateType;
import com.ucredit.hermes.enums.CompanyStatus;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.QueryType;
import com.ucredit.hermes.enums.ReportType;
import com.ucredit.hermes.enums.TreatResult;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.pengyuan.CompanyContactOperateInfo;
import com.ucredit.hermes.model.pengyuan.CompanyCourtInfo;
import com.ucredit.hermes.model.pengyuan.CompanyInfo;
import com.ucredit.hermes.model.pengyuan.CompanyManagerInfo;
import com.ucredit.hermes.model.pengyuan.CompanyOtherShareholderInfo;
import com.ucredit.hermes.model.pengyuan.CompanyShareholderInfo;
import com.ucredit.hermes.model.pengyuan.CorpTypeInfo;
import com.ucredit.hermes.model.pengyuan.CurrencyInfo;
import com.ucredit.hermes.model.pengyuan.EconomicTypeInfo;
import com.ucredit.hermes.model.pengyuan.LegalOtherManagerInfo;
import com.ucredit.hermes.model.pengyuan.LegalOtherShareholderInfo;
import com.ucredit.hermes.model.pengyuan.OrganTypeInfo;
import com.ucredit.hermes.model.pengyuan.PengYuanCount;
import com.ucredit.hermes.model.pengyuan.PengYuanReportRecord;
import com.ucredit.hermes.model.pengyuan.PositionInfo;
import com.ucredit.hermes.model.pengyuan.SubReportInfo;
import com.ucredit.hermes.model.pengyuan.TradeInfo;

/**
 * 企业相关信息异步: service
 *
 * @author liuqianqian
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class CompanyInfoBatchService {
    private static Logger logger = LoggerFactory
            .getLogger(CompanyInfoBatchService.class);
    @Autowired
    private CompanyContactOperateInfoDAO companyContactOperateInfoDao;
    /*
     * @Autowired
     * private CompanyCourtInfoDAO companyCourtInfoDao;
     */
    @Autowired
    private CompanyManagerInfoDAO companyManagerInfoDao;
    /*
     * @Autowired
     * private CompanyOtherShareholderInfoDAO companyOtherShareholderInfoDao;
     */
    @Autowired
    private CompanyShareholderInfoDAO companyShareholderInfoDao;
    /*
     * @Autowired
     * private LegalOtherManagerInfoDAO legalOtherManagerInfoDao;
     * @Autowired
     * private LegalOtherShareholderInfoDAO legalOtherShareholderInfoDao;
     */
    @Autowired
    private SubReportInfoDAO subReportInfoDAO;
    @Autowired
    private CurrencyInfoDAO currencyInfoDAO;
    @Autowired
    private OrganTypeInfoDAO organTypeInfoDAO;
    @Autowired
    private EconomicTypeInfoDAO economicTypeInfoDAO;
    @Autowired
    private TradeInfoDAO tradeInfoDAO;
    @Autowired
    private CorpTypeInfoDAO corpTypeInfoDAO;
    @Autowired
    private PositionInfoDAO positionInfoDAO;
    @Autowired
    private PengYuanCountDAO pengYuanCountDAO;

    /**
     * add by xubaoyong
     */
    @Autowired
    private PengYuanReportRecordDAO pengYuanReportRecordDAO;

    /**
     * 企业当前状态ID=1
     */
    private static final String CODE_ONE = "1";
    /**
     * 企业当前状态ID=2
     */
    private static final String CODE_TWO = "2";
    /**
     * 企业当前状态ID=3
     */
    private static final String CODE_THREE = "3";
    /**
     * 企业当前状态ID=4
     */
    private static final String CODE_FOUR = "4";
    /**
     * 企业当前状态ID=5
     */
    private static final String CODE_FIVE = "5";
    /**
     * 企业信息表名
     */
    public static final String COMPANYINFO = "company_infos";

    /**
     * 异步调用第三方接口，将返回数据插入到数据库中
     *
     * @param companyInfoOld
     * @param companyName
     * @param orgCode
     * @param registerNo
     */

    /**
     * 添加子表
     *
     * @param companyInfo
     *        主表
     * @param allList
     *        md5
     * @param cisReportElement
     *        返回的xml的子标签
     * @param isAddSubList
     *        子表是否要添加到数据库
     * @param now
     * @param feeReportSet
     * @param subReportSet
     * @return List<String>
     * @throws Exception
     */
    public List<String> addCompanyInfoSubList(CompanyInfo companyInfo,
        List<String> allList, Element cisReportElement,
        boolean isAddSubList, Date now, Set<String> feeReportSet,
        Set<String> subReportSet) throws Exception {
        if (StringUtils.isEmpty(companyInfo.getRefId())) {
            throw new Exception("企业 refID 不能为空");
        }
        List<CompanyContactOperateInfo> companyContactOperateInfoList = new LinkedList<>();
        //企业注册地址及电话
        this.companyContactOperateInfoEntity(companyContactOperateInfoList,
            cisReportElement, true, companyInfo, allList, feeReportSet,
            subReportSet);
        //企业经营地址及电话
        this.companyContactOperateInfoEntity(companyContactOperateInfoList,
            cisReportElement, false, companyInfo, allList, feeReportSet,
            subReportSet);
        //企业股东信息
        List<CompanyShareholderInfo> companyShareholderInfoList = this
                .companyShareholderInfoEntity(cisReportElement, companyInfo,
                    allList, feeReportSet, subReportSet);
        //企业高管信息
        List<CompanyManagerInfo> companyManagerInfoList = this
                .companyManagerInfoEntity(cisReportElement, companyInfo, allList,
                    feeReportSet, subReportSet);
        //企业对外股权投资信息
        //List<CompanyOtherShareholderInfo> companyOtherShareholderInfoList = this
        //   .companyOtherShareholderInfoEntity(cisReportElement, companyInfo,
        //      allList);
        //法定代表人在其他机构任职信息
        //List<LegalOtherManagerInfo> legalOtherManagerInfoList = this
        //   .legalOtherManagerEntity(cisReportElement, companyInfo, allList);
        //第二级节点法定代表人股权投资信息
        //List<LegalOtherShareholderInfo> legalOtherShareholderInfoList = this
        //   .legalOtherShareholderEntity(cisReportElement, companyInfo, allList);
        //企业法院被执行信息
        //List<CompanyCourtInfo> companyCourtInfoList = this.companyCourtEntity(
        //   cisReportElement, companyInfo, allList);
        /*
         * Set<CompanyManagerInfo> companyManagerInfos = new HashSet<>();
         * for(CompanyManagerInfo c :companyManagerInfoList){
         * companyManagerInfos.add(c);
         * }
         * companyInfo.setCompanyManagerInfos(companyManagerInfos);
         * Set<CompanyContactOperateInfo> companyContactOperateInfos = new
         * HashSet<>();
         * for(CompanyContactOperateInfo c:companyContactOperateInfoList){
         * companyContactOperateInfos.add(c);
         * }
         * companyInfo.setCompanyContactOperateInfos(companyContactOperateInfos);
         * Set<CompanyShareholderInfo> companyShareholderInfos = new
         * HashSet<>();
         * for(CompanyShareholderInfo c:companyShareholderInfoList){
         * companyShareholderInfos.add(c);
         * }
         * companyInfo.setCompanyShareholderInfos(companyShareholderInfos);
         */

        if (isAddSubList) {//是否要添加
            /**
             * 企业注册和经营地址及电话信息
             */
            this.companyContactOperateInfoDao.batchAddEntities(
                companyContactOperateInfoList,
                companyContactOperateInfoList.size());
            this.addPengYuanReportRecord(cisReportElement,
                "registerContactInfos", companyInfo.getRefId(), now);
            this.addPengYuanReportRecord(cisReportElement,
                "manageContactInfos", companyInfo.getRefId(), now);
            /**
             * 企业股东信息
             */
            this.companyShareholderInfoDao.batchAddEntities(
                companyShareholderInfoList, companyShareholderInfoList.size());
            this.addPengYuanReportRecord(cisReportElement,
                "nationalCorpShareholderInfo", companyInfo.getRefId(), now);

            /**
             * 企业高管信息
             */
            this.companyManagerInfoDao.batchAddEntities(companyManagerInfoList,
                companyManagerInfoList.size());

            this.addPengYuanReportRecord(cisReportElement,
                "corpTopManagerInfo", companyInfo.getRefId(), now);

            /**
             * 企业对外投资信息
             */
            //this.companyOtherShareholderInfoDao.batchAddEntities(
            //    companyOtherShareholderInfoList,
            //   companyOtherShareholderInfoList.size());
            //this.addPengYuanReportRecord(cisReportElement,
            //    "nationalCorpOtherShareholderInfo", companyInfo.getRefId(), now);
            /**
             * 法定代表人在其他机构任职信息
             */
            //this.legalOtherManagerInfoDao.batchAddEntities(
            //    legalOtherManagerInfoList, legalOtherManagerInfoList.size());
            // this.addPengYuanReportRecord(cisReportElement,
            //    "frOtherCorpTopManagerInfo", companyInfo.getRefId(), now);

            /**
             * 法定代表人股权投资信息
             */
            //this.legalOtherShareholderInfoDao.batchAddEntities(
            //   legalOtherShareholderInfoList,
            //   legalOtherShareholderInfoList.size());
            // this.addPengYuanReportRecord(cisReportElement,
            //   "nationalFrOtherCorpShareholderInfo", companyInfo.getRefId(),
            // now);
            /**
             * 法院被执行信息
             */
            // this.companyCourtInfoDao.batchAddEntities(companyCourtInfoList,
            //   companyCourtInfoList.size());
            //this.addPengYuanReportRecord(cisReportElement, "courtInfos",
            //  companyInfo.getRefId(), now);

            Set<CompanyManagerInfo> companyManagerInfos = new HashSet<>();
            for (CompanyManagerInfo c : companyManagerInfoList) {
                companyManagerInfos.add(c);
            }
            companyInfo.setCompanyManagerInfos(companyManagerInfos);
            Set<CompanyContactOperateInfo> companyContactOperateInfos = new HashSet<>();
            for (CompanyContactOperateInfo c : companyContactOperateInfoList) {
                companyContactOperateInfos.add(c);
            }
            companyInfo
            .setCompanyContactOperateInfos(companyContactOperateInfos);
            Set<CompanyShareholderInfo> companyShareholderInfos = new HashSet<>();
            for (CompanyShareholderInfo c : companyShareholderInfoList) {
                companyShareholderInfos.add(c);
            }
            companyInfo.setCompanyShareholderInfos(companyShareholderInfos);

            CompanyInfoBatchService.logger.debug("添加企业基本信息和添加其他的子表信息 <"
                    + companyInfo.getId() + "> add");
        }
        return allList;
    }

    /**
     * 添加子表
     *
     * @param companyInfo
     *        主表
     * @param allList
     *        md5
     * @param cisReportElement
     *        返回的xml的子标签
     * @param isAddSubList
     *        子表是否要添加到数据库
     * @param now
     * @param feeReportSet
     * @param subReportSet
     * @return List<String>
     * @throws Exception
     */
    public List<String> addCompanyInfoSubList1(CompanyInfo companyInfo,
            List<String> allList, Element cisReportElement,
            boolean isAddSubList, Date now, Set<String> feeReportSet,
            Set<String> subReportSet) throws Exception {
        if (StringUtils.isEmpty(companyInfo.getRefId())) {
            throw new Exception("企业 refID 不能为空");
        }
        List<CompanyContactOperateInfo> companyContactOperateInfoList = new LinkedList<>();

        //企业经营地址及电话
        this.companyContactOperateInfoEntity(companyContactOperateInfoList,
            cisReportElement, false, companyInfo, allList, feeReportSet,
            subReportSet);
        if (isAddSubList) {//是否要添加
            /**
             * 企业注册和经营地址及电话信息
             */
            this.companyContactOperateInfoDao.batchAddEntities(
                companyContactOperateInfoList,
                companyContactOperateInfoList.size());
            this.addPengYuanReportRecord(cisReportElement,
                "manageContactInfos", companyInfo.getRefId(), now);

            Set<CompanyContactOperateInfo> companyContactOperateInfos = new HashSet<>();
            for (CompanyContactOperateInfo c : companyContactOperateInfoList) {
                companyContactOperateInfos.add(c);
            }
            companyInfo
                .setCompanyContactOperateInfos(companyContactOperateInfos);
            CompanyInfoBatchService.logger.debug("添加企业基本信息和添加其他的子表信息 <"
                + companyInfo.getId() + "> add");
        }
        return allList;
    }

    /**
     * 企业信息
     *
     * @param companyInfo
     * @param cisReport
     * @param allList
     * @param feeReportSet
     * @param subReportSet
     * @throws ParseException
     */
    public void setCompanyInfo(CompanyInfo companyInfo, Element cisReport,
            List<String> allList, Set<String> feeReportSet,
            Set<String> subReportSet) throws ParseException {
        //企业基本信息
        Element corpBaseNationalInfo = cisReport
                .element("corpBaseNationalInfo");
        if (corpBaseNationalInfo != null) {
            //第三级节点
            List<Element> itemList = corpBaseNationalInfo.elements("item");
            if (!itemList.isEmpty()) {
                feeReportSet.add(HermesConsts.PENG_YUAN_95001);
                subReportSet.add(HermesConsts.PENG_YUAN_95001_21301);
            }
            for (Element item : itemList) {
                //第二级属性子报告表
                companyInfo.setSubReportType(this.getSubReportInfo(
                    corpBaseNationalInfo, "subReportType"));
                //枚举
                companyInfo.setTreatResult(CompanyInfoBatchService
                    .treatResult(corpBaseNationalInfo
                        .attributeValue("treatResult")));
                //错误信息
                companyInfo.setErrorMessage(corpBaseNationalInfo
                    .attributeValue("errorMessage"));

                // if (StringUtils.isEmpty(companyInfo.getCompanyName())) {
                companyInfo.setCompanyName(CompanyInfoBatchService.elementText(
                    item, "corpName", allList));
                // }
                // if (StringUtils.isEmpty(companyInfo.getOrgCode())) {

                companyInfo.setOrgCode(CompanyInfoBatchService.elementText(
                    item, "orgCode", allList));
                // }
                // if (StringUtils.isEmpty(companyInfo.getRegisterNo())) {
                companyInfo.setRegisterNo(CompanyInfoBatchService.elementText(
                    item, "registerNo", allList));
                //  }
                companyInfo.setArtificialName(CompanyInfoBatchService
                    .elementText(item, "artificialName", allList));
                companyInfo.setArtificialName2(CompanyInfoBatchService
                    .elementText(item, "artificialName2", allList));
                String registDate = CompanyInfoBatchService.elementText(item,
                    "registDate", allList);
                if (StringUtils.isNotBlank(registDate)) {
                    companyInfo.setRegistDate(DateUtils.parseDate(registDate,
                            "yyyyMMdd"));
                }
                String handleDate = CompanyInfoBatchService.elementText(item,
                    "handleDate", allList);
                if (StringUtils.isNotBlank(handleDate)) {
                    companyInfo.setHandleDate(DateUtils.parseDate(handleDate,
                            "yyyyMMdd"));
                }
                String cancelDate = CompanyInfoBatchService.elementText(item,
                    "cancelDate", allList);
                if (StringUtils.isNotBlank(cancelDate)) {
                    companyInfo.setCancelDate(DateUtils.parseDate(cancelDate,
                            "yyyyMMdd"));
                }
                companyInfo.setUrl(CompanyInfoBatchService.elementText(item,
                    "url", allList));

                //枚举
                companyInfo.setStatus(this.companyStatus(item
                    .elementText("statusID")));
                allList.add(item.elementText("statusID"));
                allList.add(item.elementText("flag"));
                //枚举
                companyInfo.setStatus2(this.getStatusText(item, "statusID2",
                    "statusCaption2", allList));
                String registFundValue = CompanyInfoBatchService.elementText(
                    item, "registFund", allList);
                companyInfo.setRegistFund(new BigDecimal(StringUtils
                    .isBlank(registFundValue) ? "0" : registFundValue));
                //币种子表
                CurrencyInfo c = this.queryCurrencyByNcode(item,
                    "fundCurrency", allList);
                if (c.getNcode() != null) {
                    companyInfo.setFundCurrency(c);
                } else {
                    companyInfo.setCurrencyStr(c.getName());
                }
                String registFundValue2 = CompanyInfoBatchService.elementText(
                    item, "registFund2", allList);
                companyInfo.setRegistFund2(new BigDecimal(StringUtils
                    .isBlank(registFundValue2) ? "0" : registFundValue2));
                //币种子表
                CurrencyInfo c1 = this.queryCurrencyByNcode(item,
                    "fundCurrency2", allList);

                if (c1.getNcode() != null) {
                    companyInfo.setFundCurrency2(c1);
                } else {
                    companyInfo.setCurrencyStr2(c1.getName());
                }
                //机构类型子表
                String organType = item.elementText("organType");
                if (StringUtils.isNotBlank(organType)) {
                    OrganTypeInfo oinfo = this.organTypeInfoDAO
                            .getOrganTypeByCode(organType);
                    if (oinfo != null) {
                        companyInfo.setOrganType(oinfo);
                    }
                    allList.add(organType);
                }
                //经济类型子表
                String economicType = item.elementText("economicType");
                if (StringUtils.isNotBlank(economicType)) {
                    EconomicTypeInfo einfo = this.economicTypeInfoDAO
                            .getEconomicTypeByCode(economicType);
                    if (einfo != null) {
                        companyInfo.setEconomicType(einfo);
                    }
                    allList.add(economicType);
                }
                //行业类型子表
                String tradeCode = item.elementText("tradeCode");
                if (StringUtils.isNotBlank(tradeCode)) {
                    TradeInfo tinfo = this.tradeInfoDAO
                            .getTradeByCode(tradeCode);
                    if (tinfo != null) {
                        companyInfo.setTradeCode(tinfo);
                    }
                    allList.add(tradeCode);
                    allList.add(item.elementText("tradeName1"));
                    allList.add(item.elementText("tradeName2"));
                    allList.add(item.elementText("tradeName3"));
                    allList.add(item.elementText("tradeName4"));
                }
                String staffNumberValue = CompanyInfoBatchService.elementText(
                    item, "staffNumber", allList);
                companyInfo.setCompanyScale(StringUtils
                    .isBlank(staffNumberValue) ? 0 : Integer
                        .parseInt(staffNumberValue));
                companyInfo.setManageRange(CompanyInfoBatchService.elementText(
                    item, "manageRange", allList));
                String openDate = CompanyInfoBatchService.elementText(item,
                    "openDate", allList);
                if (StringUtils.isNotBlank(openDate)) {
                    companyInfo.setOpenDate(DateUtils.parseDate(openDate,
                            "yyyyMMdd"));
                }
                String manageBeginDate = CompanyInfoBatchService.elementText(
                    item, "manageBeginDate", allList);
                if (StringUtils.isNotBlank(manageBeginDate)) {
                    companyInfo.setManageBeginDate(DateUtils.parseDate(
                        manageBeginDate, "yyyyMMdd"));
                }
                String manageEndDate = CompanyInfoBatchService.elementText(
                    item, "manageEndDate", allList);
                if (StringUtils.isNotBlank(manageEndDate)) {
                    companyInfo.setManageEndDate(DateUtils.parseDate(
                        manageEndDate, "yyyyMMdd"));
                }
                //企业类型子表
                companyInfo.setCorpType(this.getCorpTypeInfo(item,
                    "corpTypeID", "corpTypeCaption", allList));
                companyInfo.setAllowManageProject(CompanyInfoBatchService
                    .elementText(item, "allowManageProject", allList));
                companyInfo.setGeneralManageProject(CompanyInfoBatchService
                    .elementText(item, "generalManageProject", allList));
                companyInfo.setManageRangeFashion(CompanyInfoBatchService
                    .elementText(item, "manageRangeFashion", allList));
                companyInfo.setRegisterDepartment(CompanyInfoBatchService
                    .elementText(item, "registerDepartment", allList));
                String lastCheckYear = CompanyInfoBatchService.elementText(
                    item, "lastCheckYear", allList);
                if (StringUtils.isNotBlank(lastCheckYear)) {
                    companyInfo.setLastCheckYear(DateUtils.parseDate(
                        lastCheckYear, "yyyy"));
                }
                String lastCheckDate = CompanyInfoBatchService.elementText(
                    item, "lastCheckDate", allList);
                if (StringUtils.isNotBlank(lastCheckDate)) {
                    companyInfo.setLastCheckDate(DateUtils.parseDate(
                        lastCheckDate, "yyyyMMdd"));
                }
                String logoutDate = CompanyInfoBatchService.elementText(item,
                    "logoutDate", allList);
                if (StringUtils.isNotBlank(logoutDate)) {
                    companyInfo.setLogoutDate(DateUtils.parseDate(logoutDate,
                            "yyyyMMdd"));
                }
                String revokeDate = CompanyInfoBatchService.elementText(item,
                    "revokeDate", allList);
                if (StringUtils.isNotBlank(revokeDate)) {
                    companyInfo.setRevokeDate(DateUtils.parseDate(revokeDate,
                            "yyyyMMdd"));
                }
                companyInfo.setDataChannel(DataChannel.PENGYUAN);
            }
        }
    }

    /**
     * 企业注册地址及电话或者企业经营地址及电话
     *
     * @param companyContactOperateInfoList
     * @param elementList
     * @param isRegister
     *        注册还是经营
     * @param companyInfo
     * @param allList
     * @return List
     */
    private List<CompanyContactOperateInfo> companyContactOperateInfoEntity(
        List<CompanyContactOperateInfo> companyContactOperateInfoList,
        Element elementList, boolean isRegister, CompanyInfo companyInfo,
        List<String> allList, Set<String> feeReportSet,
        Set<String> subReportSet) {
        //List<CompanyContactOperateInfo> companyContactOperateInfoList = new LinkedList<>();
        Element registerContactInfo = null;

        if (isRegister) {//注册
            registerContactInfo = elementList.element("registerContactInfos");
        } else {//经营
            registerContactInfo = elementList.element("manageContactInfos");
        }
        if (registerContactInfo != null) {
            //第三级节点
            List<Element> itemList = registerContactInfo.elements("item");
            if (!itemList.isEmpty() && isRegister) {//注册
                feeReportSet.add(HermesConsts.PENG_YUAN_95001);
                subReportSet.add(HermesConsts.PENG_YUAN_95001_21611);
            } else if (!itemList.isEmpty()) {//经营
                feeReportSet.add(HermesConsts.PENG_YUAN_90035);
                subReportSet.add(HermesConsts.PENG_YUAN_90035_21612);
            }
            for (Element item : itemList) {
                CompanyContactOperateInfo companyContactOperateInfo = new CompanyContactOperateInfo();

                companyContactOperateInfo.setCompanyInfos_id(companyInfo
                    .getId());//主对象
                //第二级属性子报告
                companyContactOperateInfo.setSubReportType(this
                    .getSubReportInfo(registerContactInfo, "subReportType"));
                //枚举
                companyContactOperateInfo
                .setTreatResult(CompanyInfoBatchService
                    .treatResult(registerContactInfo
                        .attributeValue("treatResult")));
                //错误信息
                companyContactOperateInfo.setErrorMessage(registerContactInfo
                    .attributeValue("errorMessage"));
                if (isRegister) {//注册
                    companyContactOperateInfo
                    .setType(CompanyOperateType.REGISTER);
                } else {//经营
                    companyContactOperateInfo
                    .setType(CompanyOperateType.OPERATE);
                }
                companyContactOperateInfo.setAreaCode(CompanyInfoBatchService
                    .elementText(item, "areaCode", allList));
                companyContactOperateInfo.setAreaDesc(CompanyInfoBatchService
                    .elementText(item, "areaDesc", allList));
                companyContactOperateInfo.setAddress(CompanyInfoBatchService
                    .elementText(item, "address", allList));
                companyContactOperateInfo.setTel(CompanyInfoBatchService
                    .elementText(item, "tel", allList));
                companyContactOperateInfo.setPostCode(CompanyInfoBatchService
                    .elementText(item, "postCode", allList));
                companyContactOperateInfoList.add(companyContactOperateInfo);
            }
        }
        return companyContactOperateInfoList;
    }

    /**
     * 企业股东信息
     *
     * @param elementList
     * @param companyInfo
     * @param allList
     * @return List
     * @throws ParseException
     */
    private List<CompanyShareholderInfo> companyShareholderInfoEntity(
        Element elementList, CompanyInfo companyInfo, List<String> allList,
        Set<String> feeReportSet, Set<String> subReportSet)
                throws ParseException {
        List<CompanyShareholderInfo> companyShareholderInfoList = new ArrayList<>();
        Element nationalCorpShareholderInfo = elementList
                .element("nationalCorpShareholderInfo");

        if (nationalCorpShareholderInfo != null) {
            List<Element> itemList = nationalCorpShareholderInfo
                    .elements("item");
            if (!itemList.isEmpty()) {
                feeReportSet.add(HermesConsts.PENG_YUAN_95001);
                subReportSet.add(HermesConsts.PENG_YUAN_95001_22101);
            }
            for (Element item : itemList) {
                CompanyShareholderInfo companyShareholderInfo = new CompanyShareholderInfo();
                companyShareholderInfo.setCompanyInfos_id(companyInfo.getId());
                //第二级属性子报告
                companyShareholderInfo.setSubReportType(this.getSubReportInfo(
                    nationalCorpShareholderInfo, "subReportType"));
                //枚举
                companyShareholderInfo.setTreatResult(CompanyInfoBatchService
                    .treatResult(nationalCorpShareholderInfo
                        .attributeValue("treatResult")));
                //错误信息
                companyShareholderInfo
                .setErrorMessage(nationalCorpShareholderInfo
                    .attributeValue("errorMessage"));

                companyShareholderInfo.setName(CompanyInfoBatchService
                    .elementText(item, "name", allList));
                String contributiveFund = CompanyInfoBatchService.elementText(
                    item, "contributiveFund", allList);
                companyShareholderInfo.setContributiveFund(new BigDecimal(
                    StringUtils.isBlank(contributiveFund) ? "0"
                        : contributiveFund));
                String contributivePercent = CompanyInfoBatchService
                        .elementText(item, "contributivePercent", allList);
                companyShareholderInfo.setContributivePercent(new BigDecimal(
                    StringUtils.isBlank(contributivePercent) ? "0"
                        : contributivePercent));
                //币种子表
                CurrencyInfo c2 = this.queryCurrencyByNcode(item, "currency",
                    allList);
                if (c2.getNcode() != null) {
                    companyShareholderInfo.setFundCurrency(c2);
                }
                String contributiveDate = CompanyInfoBatchService.elementText(
                    item, "contributiveDate", allList);
                if (StringUtils.isNotBlank(contributiveDate)) {
                    companyShareholderInfo.setContributiveDate(DateUtils
                        .parseDate(contributiveDate, "yyyyMMdd"));
                }
                companyShareholderInfoList.add(companyShareholderInfo);
            }
        }
        return companyShareholderInfoList;

    }

    /**
     * 企业高管信息
     *
     * @param elementList
     * @param sdf
     * @param companyInfo
     * @param allList
     * @return List
     */
    private List<CompanyManagerInfo> companyManagerInfoEntity(
        Element elementList, CompanyInfo companyInfo, List<String> allList,
        Set<String> feeReportSet, Set<String> subReportSet) {
        List<CompanyManagerInfo> companyManagerInfoList = new ArrayList<>();
        Element corpTopManagerInfo = elementList.element("corpTopManagerInfo");

        if (corpTopManagerInfo != null) {
            List<Element> itemList = corpTopManagerInfo.elements("item");
            if (!itemList.isEmpty()) {
                feeReportSet.add(HermesConsts.PENG_YUAN_95001);
                subReportSet.add(HermesConsts.PENG_YUAN_95001_22102);
            }
            for (Element item : itemList) {
                CompanyManagerInfo companyManagerInfo = new CompanyManagerInfo();
                companyManagerInfo.setCompanyInfos_id(companyInfo.getId());
                //第二级属性子报告
                companyManagerInfo.setSubReportType(this.getSubReportInfo(
                    corpTopManagerInfo, "subReportType"));
                //枚举
                companyManagerInfo.setTreatResult(CompanyInfoBatchService
                    .treatResult(corpTopManagerInfo
                        .attributeValue("treatResult")));
                //错误信息
                companyManagerInfo.setErrorMessage(corpTopManagerInfo
                    .attributeValue("errorMessage"));
                companyManagerInfo.setName(CompanyInfoBatchService.elementText(
                    item, "name", allList));
                //职务子表
                String positionID = item.elementText("positionID");
                if (StringUtils.isNotBlank(positionID)) {
                    PositionInfo sinfo = this.positionInfoDAO
                            .getPositionByCode(positionID);
                    if (sinfo != null) {
                        companyManagerInfo.setPosition(sinfo);
                    }
                    allList.add(positionID);
                    allList.add(item.elementText("positionCaption"));
                }
                companyManagerInfoList.add(companyManagerInfo);
            }
        }
        return companyManagerInfoList;
    }

    /**
     * 企业对外股权投资信息
     *
     * @param elementList
     * @param companyInfo
     * @param allList
     * @return List
     */
    public List<CompanyOtherShareholderInfo> companyOtherShareholderInfoEntity(
        Element elementList, CompanyInfo companyInfo, List<String> allList) {

        List<CompanyOtherShareholderInfo> companyOtherShareholderInfoList = new ArrayList<>();
        Element nationalCorpOtherShareholderInfo = elementList
                .element("nationalCorpOtherShareholderInfo");
        if (nationalCorpOtherShareholderInfo != null) {
            List<Element> itemList = nationalCorpOtherShareholderInfo
                    .elements("item");
            for (Element item : itemList) {
                CompanyOtherShareholderInfo companyOtherShareholderInfo = new CompanyOtherShareholderInfo();
                companyOtherShareholderInfo.setCompanyInfos_id(companyInfo
                    .getId());
                //第二级属性子报告
                companyOtherShareholderInfo.setSubReportType(this
                    .getSubReportInfo(nationalCorpOtherShareholderInfo,
                            "subReportType"));
                //枚举
                companyOtherShareholderInfo
                .setTreatResult(CompanyInfoBatchService
                    .treatResult(nationalCorpOtherShareholderInfo
                        .attributeValue("treatResult")));
                companyOtherShareholderInfo
                .setErrorMessage(nationalCorpOtherShareholderInfo
                    .attributeValue("errorMessage"));
                companyOtherShareholderInfo.setCorpName(CompanyInfoBatchService
                    .elementText(item, "corpName", allList));
                companyOtherShareholderInfo
                .setRegisterNo(CompanyInfoBatchService.elementText(item,
                    "registNo", allList));

                //企业类型子表
                companyOtherShareholderInfo.setCorpType(this.getCorpTypeInfo(
                    item, "corpTypeID", "corpTypeCaption", allList));
                String registFund = CompanyInfoBatchService.elementText(item,
                    "registFund", allList);
                companyOtherShareholderInfo.setRegistFund(new BigDecimal(
                    StringUtils.isBlank(registFund) ? "0" : registFund));
                //枚举
                companyOtherShareholderInfo.setStatus(this.getStatusText(item,
                    "statusID", "statusCaption", allList));
                companyOtherShareholderInfo
                .setRegisterDepartment(CompanyInfoBatchService.elementText(
                    item, "registerDepartment", allList));
                String contributiveFund = CompanyInfoBatchService.elementText(
                    item, "contributiveFund", allList);
                companyOtherShareholderInfo.setContributiveFund(new BigDecimal(
                    StringUtils.isBlank(contributiveFund) ? "0"
                        : contributiveFund));
                String contributivePercent = CompanyInfoBatchService
                        .elementText(item, "contributivePercent", allList);
                companyOtherShareholderInfo
                .setContributivePercent(new BigDecimal(StringUtils
                    .isBlank(contributivePercent) ? "0"
                        : contributivePercent));
                //币种子表
                CurrencyInfo c3 = this.queryCurrencyByNcode(item, "currency",
                    allList);
                if (c3.getNcode() != null) {
                    companyOtherShareholderInfo.setFundCurrency(c3);
                }
                companyOtherShareholderInfoList
                .add(companyOtherShareholderInfo);
            }
        }
        return companyOtherShareholderInfoList;
    }

    /**
     * 法定代表人在其他机构任职信息
     *
     * @param elementList
     * @param companyInfo
     * @param allList
     * @return List
     */
    public List<LegalOtherManagerInfo> legalOtherManagerEntity(
        Element elementList, CompanyInfo companyInfo, List<String> allList) {

        List<LegalOtherManagerInfo> legalOtherManagerInfoList = new ArrayList<>();
        Element frOtherCorpTopManagerInfo = elementList
                .element("frOtherCorpTopManagerInfo");
        if (frOtherCorpTopManagerInfo != null) {
            List<Element> itemList = frOtherCorpTopManagerInfo.elements("item");
            for (Element item : itemList) {
                LegalOtherManagerInfo legalOtherManagerInfo = new LegalOtherManagerInfo();
                legalOtherManagerInfo.setCompanyInfos_id(companyInfo.getId());
                //第二级属性子报告
                legalOtherManagerInfo.setSubReportType(this.getSubReportInfo(
                    frOtherCorpTopManagerInfo, "subReportType"));
                //枚举
                legalOtherManagerInfo.setTreatResult(CompanyInfoBatchService
                    .treatResult(frOtherCorpTopManagerInfo
                        .attributeValue("treatResult")));
                legalOtherManagerInfo.setErrorMessage(frOtherCorpTopManagerInfo
                    .attributeValue("errorMessage"));

                legalOtherManagerInfo.setCorpName(CompanyInfoBatchService
                    .elementText(item, "corpName", allList));
                legalOtherManagerInfo.setRegisterNo(CompanyInfoBatchService
                    .elementText(item, "registNo", allList));
                //企业类型子表
                legalOtherManagerInfo.setCorpType(this.getCorpTypeInfo(item,
                    "corpTypeID", "corpTypeCaption", allList));
                String registFund = CompanyInfoBatchService.elementText(item,
                    "registFund", allList);
                legalOtherManagerInfo.setRegistFund(new BigDecimal(StringUtils
                    .isBlank(registFund) ? "0" : registFund));
                //枚举
                legalOtherManagerInfo.setStatus(this.getStatusText(item,
                    "statusID", "statusCaption", allList));
                legalOtherManagerInfo
                .setRegisterDepartment(CompanyInfoBatchService.elementText(
                    item, "registerDepartment", allList));
                //职务子表
                String positionID = item.elementText("positionID");
                if (StringUtils.isNotBlank(positionID)) {
                    PositionInfo posinfo = this.positionInfoDAO
                            .getPositionByCode(positionID);
                    if (posinfo != null) {
                        legalOtherManagerInfo.setPosition(posinfo);
                    }
                    allList.add(positionID);
                    allList.add(item.elementText("positionCaption"));
                }
                legalOtherManagerInfoList.add(legalOtherManagerInfo);
            }
        }
        return legalOtherManagerInfoList;
    }

    /**
     * 法定代表人股权投资信息
     *
     * @param elementList
     * @param companyInfo
     * @param allList
     * @return List
     */
    public List<LegalOtherShareholderInfo> legalOtherShareholderEntity(
        Element elementList, CompanyInfo companyInfo, List<String> allList) {

        List<LegalOtherShareholderInfo> LegalOtherShareholderInfoList = new ArrayList<>();
        Element nationalFrOtherCorpShareholderInfo = elementList
                .element("nationalFrOtherCorpShareholderInfo");
        if (nationalFrOtherCorpShareholderInfo != null) {
            List<Element> itemList = nationalFrOtherCorpShareholderInfo
                    .elements("item");
            for (Element item : itemList) {
                LegalOtherShareholderInfo LegalOtherShareholderInfo = new LegalOtherShareholderInfo();
                LegalOtherShareholderInfo.setCompanyInfos_id(companyInfo
                    .getId());
                //第二级属性子报告
                LegalOtherShareholderInfo.setSubReportType(this
                    .getSubReportInfo(nationalFrOtherCorpShareholderInfo,
                            "subReportType"));
                //枚举
                LegalOtherShareholderInfo
                .setTreatResult(CompanyInfoBatchService
                    .treatResult(nationalFrOtherCorpShareholderInfo
                        .attributeValue("treatResult")));
                LegalOtherShareholderInfo
                .setErrorMessage(nationalFrOtherCorpShareholderInfo
                    .attributeValue("errorMessage"));

                LegalOtherShareholderInfo.setCorpName(CompanyInfoBatchService
                    .elementText(item, "corpName", allList));
                LegalOtherShareholderInfo.setRegisterNo(CompanyInfoBatchService
                    .elementText(item, "registNo", allList));
                //企业类型子表
                LegalOtherShareholderInfo.setCorpType(this.getCorpTypeInfo(
                    item, "corpTypeID", "corpTypeCaption", allList));
                String registFund = CompanyInfoBatchService.elementText(item,
                    "registFund", allList);
                LegalOtherShareholderInfo.setRegistFund(new BigDecimal(
                    StringUtils.isBlank(registFund) ? "0" : registFund));
                //枚举
                LegalOtherShareholderInfo.setStatus(this.getStatusText(item,
                    "statusID", "statusCaption", allList));
                LegalOtherShareholderInfo
                .setRegisterDepartment(CompanyInfoBatchService.elementText(
                    item, "registerDepartment", allList));
                String contributiveFund = CompanyInfoBatchService.elementText(
                    item, "contributiveFund", allList);
                LegalOtherShareholderInfo.setContributiveFund(new BigDecimal(
                    StringUtils.isBlank(contributiveFund) ? "0"
                        : contributiveFund));
                String contributivePercent = CompanyInfoBatchService
                        .elementText(item, "contributivePercent", allList);
                LegalOtherShareholderInfo
                .setContributivePercent(new BigDecimal(StringUtils
                    .isBlank(contributivePercent) ? "0"
                        : contributivePercent));
                //币种子表 是人民币
                String currency = item.elementText("currency");
                if (StringUtils.isNotBlank(currency)) {
                    CurrencyInfo cinfo = this.currencyInfoDAO
                            .getCurrencyByName(currency);
                    if (cinfo != null) {
                        LegalOtherShareholderInfo.setFundCurrency(cinfo);
                    }
                    allList.add(currency);
                }
                LegalOtherShareholderInfoList.add(LegalOtherShareholderInfo);
            }
        }
        return LegalOtherShareholderInfoList;
    }

    /**
     * 企业法院被执行信息
     *
     * @param elementList
     * @param companyInfo
     * @param allList
     * @return List
     * @throws ParseException
     */
    public List<CompanyCourtInfo> companyCourtEntity(Element elementList,
        CompanyInfo companyInfo, List<String> allList)
                throws ParseException {

        List<CompanyCourtInfo> companyCourtInfoList = new ArrayList<>();
        Element courtInfo = elementList.element("courtInfos");
        if (courtInfo != null) {
            List<Element> itemList = courtInfo.elements("item");
            for (Element item : itemList) {
                CompanyCourtInfo companyCourtInfo = new CompanyCourtInfo();
                companyCourtInfo.setCompanyInfos_id(companyInfo.getId());
                //第二级属性子报告
                companyCourtInfo.setSubReportType(this.getSubReportInfo(
                    courtInfo, "subReportType"));
                //枚举
                if (CompanyInfoBatchService.CODE_ONE.equals(courtInfo
                    .attributeValue("treatResult"))) {
                    companyCourtInfo.setTreatResult(TreatResult.RICHARD);
                } else if (CompanyInfoBatchService.CODE_TWO.equals(courtInfo
                    .attributeValue("treatResult"))) {
                    companyCourtInfo.setTreatResult(TreatResult.NORICHARD);
                } else {
                    companyCourtInfo.setTreatResult(TreatResult.OTHER);
                }
                companyCourtInfo.setErrorMessage(courtInfo
                    .attributeValue("errorMessage"));

                companyCourtInfo.setCorpName(CompanyInfoBatchService
                    .elementText(item, "corpName", allList));
                String currency = item.elementText("currency");
                //币种子表  个人法院被执行信息币种特殊，所以有此判断
                if ("元".equals(currency)) {
                    companyCourtInfo.setFundCurrency(this.currencyInfoDAO
                        .getCurrencyByCode("CNY"));
                    allList.add(currency);
                }

                companyCourtInfo.setBelongings(CompanyInfoBatchService
                    .elementText(item, "belongings", allList));
                companyCourtInfo.setExecCourt(CompanyInfoBatchService
                    .elementText(item, "execCourt", allList));
                companyCourtInfo.setStatus(CompanyInfoBatchService.elementText(
                    item, "status", allList));
                companyCourtInfo.setRecordNo(CompanyInfoBatchService
                    .elementText(item, "recordNo", allList));
                String recordTime = CompanyInfoBatchService.elementText(item,
                    "recordTime", allList);
                if (StringUtils.isNotBlank(recordTime)) {
                    companyCourtInfo.setRecordTime(DateUtils.parseDate(
                        recordTime, "yyyyMMdd"));
                }
                String infoDate = CompanyInfoBatchService.elementText(item,
                    "infoDate", allList);
                if (StringUtils.isNotBlank(infoDate)) {
                    companyCourtInfo.setInfoDate(DateUtils.parseDate(infoDate,
                            "yyyyMMdd"));
                }
                companyCourtInfoList.add(companyCourtInfo);
            }
        }
        return companyCourtInfoList;
    }

    /**
     * 日志对象
     *
     * @param document
     * @param logInfo
     * @param now
     * @return LogInfo
     * @throws ParseException
     */
    public LogInfo logEntity(Document document, LogInfo logInfo, Date now)
            throws ParseException {
        if (document != null) {
            Element root = document.getRootElement();
            //第一级节点
            List<Element> cisReportList = root.elements("cisReport");
            Element cisReport = cisReportList.get(0);

            logInfo.setReportID(cisReport.attributeValue("reportID"));
            logInfo.setRefID(cisReport.attributeValue("refID"));
            logInfo.setSystemError(Boolean.parseBoolean(cisReport
                .attributeValue("hasSystemError")));
            logInfo.setFrozen(Boolean.parseBoolean(cisReport
                .attributeValue("isFrozen")));
            String buildEndTime = cisReport.attributeValue("buildEndTime");
            if (StringUtils.isNotBlank(buildEndTime)) {
                logInfo.setBuildEndTime(DateUtils.parseDate(buildEndTime,
                        "yyyy-MM-dd hh:mm:ss"));
            }

        }
        logInfo.setEndTime(now);
        logInfo.setLastUpdateTime(now);
        return logInfo;
    }

    /**
     * 通过code 判断企业当前状态
     *
     * @param code
     * @return CompanyStatus
     */
    public CompanyStatus companyStatus(String code) {
        if (CompanyInfoBatchService.CODE_ONE.equals(code)) {
            return CompanyStatus.OPENED;
        } else if (CompanyInfoBatchService.CODE_TWO.equals(code)) {
            return CompanyStatus.REVOKE;
        } else if (CompanyInfoBatchService.CODE_THREE.equals(code)) {
            return CompanyStatus.CANCELLATION;
        } else if (CompanyInfoBatchService.CODE_FOUR.equals(code)) {
            return CompanyStatus.MOVE;
        } else if (CompanyInfoBatchService.CODE_FIVE.equals(code)) {
            return CompanyStatus.CLOSE;
        } else {
            return CompanyStatus.OTHER;
        }
    }

    /**
     * 通过code 判断子报告查询状态
     *
     * @param code
     * @return TreatResult
     */
    private static TreatResult treatResult(String code) {
        if (CompanyInfoBatchService.CODE_ONE.equals(code)) {
            return TreatResult.RICHARD;
        } else if (CompanyInfoBatchService.CODE_TWO.equals(code)) {
            return TreatResult.NORICHARD;
        } else if (CompanyInfoBatchService.CODE_THREE.equals(code)) {
            return TreatResult.OTHER;
        }
        return null;
    }

    /**
     * 返回的结果标记只要有一个1就返回true
     *
     * @param elementList
     * @return boolean
     */
    public boolean getTreatResultValue(Element elementList) {
        return elementList.attributeValue("treatResult").contains("1");
    }

    /**
     * 返回节点中的值，并且添加到list里面
     *
     * @param item
     * @param name
     * @param allList
     * @param alllist
     * @return String
     */
    private static String elementText(Element item, String name,
            List<String> allList) {
        String value = item.elementText(name);
        if (StringUtils.isNotBlank(value)) {
            allList.add(value);
        }
        return value;
    }

    /**
     * @param item
     * @param allList
     * @param statusID
     * @param statusCaption
     * @return
     */
    private CompanyStatus getStatusText(Element item, String statusID,
            String statusCaption, List<String> allList) {
        CompanyStatus value = this.companyStatus(item.elementText(statusID));
        allList.add(item.elementText(statusID));
        allList.add(item.elementText(statusCaption));
        return value;
    }

    /**
     * 通过编号获取子报告
     *
     * @param element
     * @param name
     * @return SubReportInfo
     */
    private SubReportInfo getSubReportInfo(Element element, String name) {
        //第二级属性子报告表
        String value = element.attributeValue(name);
        if (StringUtils.isNotBlank(value)) {
            return this.subReportInfoDAO.getSubReportByCode(value,
                ReportType.NORMAL_REPORT);
        }
        return null;
    }

    /**
     * 通过编号获取币种
     *
     * @param item
     * @param name
     * @param allList
     * @return CurrencyInfo
     */
    private CurrencyInfo queryCurrencyByNcode(Element item, String name,
            List<String> allList) {
        String currencyNcode = item.elementText(name);
        if (!"null".equals(currencyNcode)
                && StringUtils.isNotBlank(currencyNcode)) {
            allList.add(currencyNcode);
            int currencyCode = 0;
            try {
                currencyCode = Integer.parseInt(currencyNcode);
                CurrencyInfo c = this.currencyInfoDAO
                        .getCurrencyByNcode(currencyCode);
                if (c != null) {
                    return c;
                } else {
                    CurrencyInfo c1 = new CurrencyInfo();
                    c1.setName(currencyNcode);
                    return c1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            CurrencyInfo c = new CurrencyInfo();
            c.setName(currencyNcode);
            return c;
        } else {
            CurrencyInfo c = new CurrencyInfo();
            c.setName(currencyNcode);
            return c;
        }
    }

    /**
     * 通过编号获取企业类型
     *
     * @param item
     * @param corpTypeID
     * @param corpTypeCaption
     * @param allList
     * @return CorpTypeInfo
     */
    private CorpTypeInfo getCorpTypeInfo(Element item, String corpTypeID,
            String corpTypeCaption, List<String> allList) {
        String corpTypeIDValue = item.elementText(corpTypeID);
        if (StringUtils.isNotBlank(corpTypeIDValue)) {
            allList.add(corpTypeIDValue);
            allList.add(item.elementText(corpTypeCaption));
            return this.corpTypeInfoDAO.getCorpTypeByCode(corpTypeIDValue);
        } else {
            return null;
        }
    }

    /**
     * 鹏元方记录的计费次数
     *
     * @param costCountElement
     * @param id
     */
    public void addPengYuanCount(Element costCountElement, int id) {
        List<Element> itemElements = costCountElement.elements("item");
        for (Element e : itemElements) {
            PengYuanCount pengYuanCount = new PengYuanCount(
                e.elementText("subReportType"), Integer.valueOf(e
                    .elementText("count")), id);
            this.pengYuanCountDAO.addEntity(pengYuanCount);
        }
    }

    public Map<String, Object> getCountItemInfo(Element cisReportElement,
        String itemFlag) {
        Map<String, Object> ret = new HashMap<>();
        Element typeElement = cisReportElement.element(itemFlag);
        String treatResult = typeElement.attributeValue("treatResult");
        String subReportType = typeElement.attributeValue("subReportType");
        ret.put("subReportType", subReportType);
        ret.put("subReportLength", 0);
        if ("1".equals(treatResult)) {
            List<Element> itemList = typeElement.elements("item");
            ret.put("subReportLength", itemList.size());
        }
        return ret;
    }

    /**
     * 记录PengYuanReportRecord日志
     *
     * @param cisReportElement
     * @param itemFlag
     * @param refId
     * @param resultTime
     */
    public void addPengYuanReportRecord(Element cisReportElement,
            String itemFlag, String refId, Date resultTime) {
        Map<String, Object> map = this.getCountItemInfo(cisReportElement,
            itemFlag);
        String pengYuanReturnRefId = cisReportElement.attributeValue("refID");
        String pengYuanRePortId = cisReportElement.attributeValue("reportID");
        int companyLength = (int) map.get("subReportLength");
        if (companyLength > 0) {
            PengYuanReportRecord logitem = new PengYuanReportRecord();
            logitem.setCount(companyLength);
            logitem.setDataChannel(DataChannel.PENGYUAN);
            logitem.setMainId(refId);
            logitem.setQueryType(QueryType.CHECK_COMPANY);
            logitem.setSubReportCode((String) map.get("subReportType"));
            logitem.setPengYuanReturnRefId(pengYuanReturnRefId);
            logitem.setPengYuanRePortId(pengYuanRePortId);
            logitem.setResultTime(resultTime);

            this.pengYuanReportRecordDAO.addEntity(logitem);
        }
    }
}
