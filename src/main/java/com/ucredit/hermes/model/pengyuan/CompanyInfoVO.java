/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;


/**
 * 企业所有信息表
 * 
 * @author liuqianqian
 */
public class CompanyInfoVO {

    /**
     * 结果操作类型
     */
    private String resultValue;

    /**
     * 企业基本信息
     */
    private CompanyInfo companyInfo;

    /**
     * 企业注册和经营地址及电话信息
     */
    private CompanyContactOperateInfo companyContactOperateInfo;

    /**
     * 企业股东信息
     */
    private CompanyShareholderInfo companyShareholderInfo;

    /**
     * 企业高管信息
     */
    private CompanyManagerInfo companyManagerInfo;

    /**
     * 企业对外股权投资信息
     */
    private CompanyOtherShareholderInfo companyOtherShareholderInfo;

    /**
     * 法定代表人在其他机构任职信息
     */
    private LegalOtherManagerInfo legalOtherManagerInfo;

    /**
     * 法定代表人股权投资信息
     */
    private LegalOtherShareholderInfo legalOtherShareholderInfo;

    /**
     * 企业法院被执行信息
     */
    private CompanyCourtInfo companyCourtInfo;

    /**
     * @return the resultValue
     */
    public String getResultValue() {
        return this.resultValue;
    }

    /**
     * @param resultValue
     *        the resultValue to set
     */
    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    /**
     * @return the companyInfo
     */
    public CompanyInfo getCompanyInfo() {
        return this.companyInfo;
    }

    /**
     * @param companyInfo
     *        the companyInfo to set
     */
    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    /**
     * @return the companyContactOperateInfo
     */
    public CompanyContactOperateInfo getCompanyContactOperateInfo() {
        return this.companyContactOperateInfo;
    }

    /**
     * @param companyContactOperateInfo
     *        the companyContactOperateInfo to set
     */
    public void setCompanyContactOperateInfo(
            CompanyContactOperateInfo companyContactOperateInfo) {
        this.companyContactOperateInfo = companyContactOperateInfo;
    }

    /**
     * @return the companyShareholderInfo
     */
    public CompanyShareholderInfo getCompanyShareholderInfo() {
        return this.companyShareholderInfo;
    }

    /**
     * @param companyShareholderInfo
     *        the companyShareholderInfo to set
     */
    public void setCompanyShareholderInfo(
            CompanyShareholderInfo companyShareholderInfo) {
        this.companyShareholderInfo = companyShareholderInfo;
    }

    /**
     * @return the companyManagerInfo
     */
    public CompanyManagerInfo getCompanyManagerInfo() {
        return this.companyManagerInfo;
    }

    /**
     * @param companyManagerInfo
     *        the companyManagerInfo to set
     */
    public void setCompanyManagerInfo(CompanyManagerInfo companyManagerInfo) {
        this.companyManagerInfo = companyManagerInfo;
    }

    /**
     * @return the companyOtherShareholderInfo
     */
    public CompanyOtherShareholderInfo getCompanyOtherShareholderInfo() {
        return this.companyOtherShareholderInfo;
    }

    /**
     * @param companyOtherShareholderInfo
     *        the companyOtherShareholderInfo to set
     */
    public void setCompanyOtherShareholderInfo(
            CompanyOtherShareholderInfo companyOtherShareholderInfo) {
        this.companyOtherShareholderInfo = companyOtherShareholderInfo;
    }

    /**
     * @return the legalOtherManagerInfo
     */
    public LegalOtherManagerInfo getLegalOtherManagerInfo() {
        return this.legalOtherManagerInfo;
    }

    /**
     * @param legalOtherManagerInfo
     *        the legalOtherManagerInfo to set
     */
    public void setLegalOtherManagerInfo(
            LegalOtherManagerInfo legalOtherManagerInfo) {
        this.legalOtherManagerInfo = legalOtherManagerInfo;
    }

    /**
     * @return the legalOtherShareholderInfo
     */
    public LegalOtherShareholderInfo getLegalOtherShareholderInfo() {
        return this.legalOtherShareholderInfo;
    }

    /**
     * @param legalOtherShareholderInfo
     *        the legalOtherShareholderInfo to set
     */
    public void setLegalOtherShareholderInfo(
            LegalOtherShareholderInfo legalOtherShareholderInfo) {
        this.legalOtherShareholderInfo = legalOtherShareholderInfo;
    }

    /**
     * @return the companyCourtInfo
     */
    public CompanyCourtInfo getCompanyCourtInfo() {
        return this.companyCourtInfo;
    }

    /**
     * @param companyCourtInfo
     *        the companyCourtInfo to set
     */
    public void setCompanyCourtInfo(CompanyCourtInfo companyCourtInfo) {
        this.companyCourtInfo = companyCourtInfo;
    }

}
