/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Element;

import com.ucredit.hermes.enums.Gender;
import com.ucredit.hermes.enums.InfoType;
import com.ucredit.hermes.enums.InsuranceCurrentStatus;
import com.ucredit.hermes.enums.MarryStatus;
import com.ucredit.hermes.enums.SubjectionRelation;
import com.ucredit.hermes.enums.TreatResult;
import com.ucredit.hermes.enums.UnitType;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.pengyuan.BaseHeadReport;
import com.ucredit.hermes.model.pengyuan.BaseReportType;

/**
 * 解析xml
 *
 * @author caoming
 */
public class ParseXmlUtils {

    /**
     * 设置返回xml的头信息
     *
     * @param e
     * @param baseReportType
     * @return
     */
    public static BaseReportType parseXmlHead(Element e,
            BaseReportType baseReportType) {
        String treatResult = e.attributeValue("treatResult");
        switch (treatResult) {
            case "1":
                baseReportType.setTreatResult(TreatResult.RICHARD);
                break;
            case "2":
                baseReportType.setTreatResult(TreatResult.NORICHARD);
                break;
            case "3":
                baseReportType.setTreatResult(TreatResult.OTHER);
                break;
            default:
                break;
        }
        String subReportTypeCost = e.attributeValue("subReportTypeCost");
        baseReportType.setSubReportTypeCost(subReportTypeCost);
        String treatErrorCode = e.attributeValue("treatErrorCode");
        baseReportType.setTreatErrorCode(treatErrorCode);
        String errorMessage = e.attributeValue("errorMessage");
        baseReportType.setErrorMessage(errorMessage);
        return baseReportType;
    }

    /**
     * 设置日志信息
     *
     * @param element
     * @param logInfo
     * @throws ParseException
     */
    public static void parseXmlSetLogInfo(Element element, LogInfo logInfo)
            throws ParseException {
        Element elementList = element.element("cisReport");
        logInfo.setReportID(elementList.attributeValue("reportID"));
        logInfo.setRefID(elementList.attributeValue("refID"));
        logInfo.setSystemError(Boolean.parseBoolean(elementList
            .attributeValue("hasSystemError")));
        logInfo.setFrozen(Boolean.parseBoolean(elementList
            .attributeValue("isFrozen")));
        logInfo.setEndTime(new Date());
        String buildEndTime = elementList.attributeValue("buildEndTime");
        if (StringUtils.isNotBlank(buildEndTime)) {
            Date date = DateUtils
                    .parseDate(buildEndTime, "yyyy-MM-dd hh:mm:ss");
            logInfo.setBuildEndTime(date);
        }
    }

    /**
     * 性别返回
     *
     * @param gender
     * @return
     */
    public static Gender parseXMLToGender(String gender) {
        switch (gender) {
            case "1":
                return Gender.MALE;
            case "2":
                return Gender.FEMALE;
            case "3":
                return Gender.OTHER;
            default:
                return Gender.OTHER;
        }
    }

    /**
     * 解析婚姻状态
     *
     * @param marryStatus
     * @return
     */
    public static MarryStatus parseXMLToMarryStatus(String marryStatus) {
        switch (marryStatus) {
            case "1":
                return MarryStatus.SPINSTERHOOD;
            case "2":
                return MarryStatus.MARRIED;
            case "3":
                return MarryStatus.UNKNOWN;
            case "4":
                return MarryStatus.DIVORCE;
            case "5":
                return MarryStatus.WIDOWED;
            case "9":
                return MarryStatus.OTHER;
            default:
                return MarryStatus.OTHER;
        }
    }

    /**
     * 解析缴费状态
     *
     * @param currentStatus
     * @return
     */
    public static InsuranceCurrentStatus parseXMLToInsuranceCurrentStatus(
            String currentStatus) {
        switch (currentStatus) {
            case "0":
                return InsuranceCurrentStatus.UNINSURED;
            case "1":
                return InsuranceCurrentStatus.INSURED_PAY;
            case "2":
                return InsuranceCurrentStatus.STOP_PAY;
            case "3":
                return InsuranceCurrentStatus.TEMINATION_PAY;
            default:
                break;
        }
        return null;
    }

    /**
     * 参保单位类型
     *
     * @param unitType
     * @return
     */
    public static UnitType parseXMLUnitType(String unitType) {
        switch (unitType) {
            case "10":
                return UnitType.BUSINESS;
            case "20":
                return UnitType.PUBLIC_INSTITUTION;
            case "21":
                return UnitType.FULL_FUNDING_INSTITUTION;
            case "22":
                return UnitType.BALANCE_ALLOCATION_INSTITUTION;
            case "23":
                return UnitType.RAISE_PUBLIC_INSTITUTION;
            case "30":
                return UnitType.OFFICE;
            case "40":
                return UnitType.SOCIAL_ORGANIZATION;
            case "50":
                return UnitType.PRIVATE_NON_ENTERPRISE_UNIT;
            case "60":
                return UnitType.INDIVIDUAL_BUSINESS_LICENCE;
            case "70":
                return UnitType.SERVICE_CENTER;
            case "99":
                return UnitType.PERSON;
            default:
                break;
        }
        return null;
    }

    /**
     * 解析附表xml头信息
     *
     * @param element
     * @param baseHeadReport
     */
    public static void parseXMLBaseHeadReport(Element element,
            BaseHeadReport baseHeadReport) {
        String treatResult = element.attributeValue("treatResult");
        switch (treatResult) {
            case "1":
                baseHeadReport.setTreatResult(TreatResult.RICHARD);
                break;
            case "2":
                baseHeadReport.setTreatResult(TreatResult.NORICHARD);
                break;
            case "3":
                baseHeadReport.setTreatResult(TreatResult.OTHER);
                break;
            default:
                break;
        }
        String subReportTypeCost = element.attributeValue("subReportTypeCost");
        baseHeadReport.setSubReportTypeCost(subReportTypeCost);
        String treatErrorCode = element.attributeValue("treatErrorCode");
        baseHeadReport.setErrorCode(treatErrorCode);
        String errorMessage = element.attributeValue("errorMessage");
        baseHeadReport.setErrorMessage(errorMessage);
    }

    /**
     * 解析隶属关系
     *
     * @param relation
     * @return
     */
    public static SubjectionRelation parseXmlRelation(String relation) {
        switch (relation) {
            case "1":
                return SubjectionRelation.CENTRE;
            case "2":
                return SubjectionRelation.PROVINCE;
            case "3":
                return SubjectionRelation.SEPARATELY_LISTED_CITY;
            case "4":
                return SubjectionRelation.CITY;
            case "5":
                return SubjectionRelation.COUNTY;
            case "6":
                return SubjectionRelation.TOWN;
            case "7":
                return SubjectionRelation.TROOPS;
            case "9":
                return SubjectionRelation.OTHER;
            default:
                return SubjectionRelation.OTHER;
        }
    }

    /**
     * 本人声明类型
     *
     * @param infoType
     * @return
     */
    public static InfoType parseXmlInfoType(String infoType) {
        switch (infoType) {
            case "1":
                return InfoType.NON_ONESELF_STATEMENT;
            case "2":
                return InfoType.ONESELF_STATEMENT;
            default:
        }
        return null;
    }
}
