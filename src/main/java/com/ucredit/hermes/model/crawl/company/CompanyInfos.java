package com.ucredit.hermes.model.crawl.company;

import java.util.List;

import javax.persistence.Lob;

/**
 * @author liuxuanyu 2016年1月7日 下午3:45:50
 */
public class CompanyInfos {
    /**
     *
     */
    private Integer id;

    private String allowManageProject;
    private String artificialName;
    private String artificialName2;
    private String cancelDate;
    private String companyName;
    private String companyScale;
    private String createTime;
    private String dataChannel;
    private String enabled;
    private String errorMessage;
    private String generalManageProject;
    private String handleDate;
    private String lastCheckDate;
    private String lastCheckYear;
    private String logoutDate;
    private String manageBeginDate;
    private String manageEndDate;

    @Lob
    private String manageRange;

    @Lob
    private String manageRangeFashion;
    private String md5;
    private String openDate;
    private String orgCode;
    private String registDate;
    private String registFund;

    private String registFund2;
    private String registerDepartment;
    private String registerNo;
    private String resultType;
    private String revokeDate;
    private String status;
    private String status2;
    private String treatResult;
    private String url;
    private String corpType_id;
    private String economicType_id;
    private String fundCurrency_id;
    private String fundCurrency2_id;

    private String organType_id;
    private String subReportType_id;
    private String tradeCode_id;
    private String version;
    private String lastUpdateTime;
    private String pengYuanCompanyName;
    private String queryTime;
    private String refId;

    private String feeReport;

    private String lendRequestId;

    private CompanyContactOperateInfos companyContactOperateInfos;

    private List<CompanyManagerInfos> managerInfolist;

    private List<CompanyShareholderInfos> shareholderInfolist;

    private String companyid;

    private String systemid;

    private String operationName;

    private String ip;

    private String subReport;
    private String keyid;
    private String hasSystemError;
    private String currencyStr;
    private String currencyStr2;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAllowManageProject() {
        return this.allowManageProject;
    }

    public void setAllowManageProject(String allowManageProject) {
        this.allowManageProject = allowManageProject;
    }

    public String getArtificialName() {
        return this.artificialName;
    }

    public void setArtificialName(String artificialName) {
        this.artificialName = artificialName;
    }

    public String getArtificialName2() {
        return this.artificialName2;
    }

    public void setArtificialName2(String artificialName2) {
        this.artificialName2 = artificialName2;
    }

    public String getCancelDate() {
        return this.cancelDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyScale() {
        return this.companyScale;
    }

    public void setCompanyScale(String companyScale) {
        this.companyScale = companyScale;
    }

    public String getDataChannel() {
        return this.dataChannel;
    }

    public void setDataChannel(String dataChannel) {
        this.dataChannel = dataChannel;
    }

    public String getEnabled() {
        return this.enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getGeneralManageProject() {
        return this.generalManageProject;
    }

    public void setGeneralManageProject(String generalManageProject) {
        this.generalManageProject = generalManageProject;
    }

    public String getHandleDate() {
        return this.handleDate;
    }

    public void setHandleDate(String handleDate) {
        this.handleDate = handleDate;
    }

    public String getLastCheckDate() {
        return this.lastCheckDate;
    }

    public void setLastCheckDate(String lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public String getLastCheckYear() {
        return this.lastCheckYear;
    }

    public void setLastCheckYear(String lastCheckYear) {
        this.lastCheckYear = lastCheckYear;
    }

    public String getLogoutDate() {
        return this.logoutDate;
    }

    public void setLogoutDate(String logoutDate) {
        this.logoutDate = logoutDate;
    }

    public String getManageBeginDate() {
        return this.manageBeginDate;
    }

    public void setManageBeginDate(String manageBeginDate) {
        this.manageBeginDate = manageBeginDate;
    }

    public String getManageEndDate() {
        return this.manageEndDate;
    }

    public void setManageEndDate(String manageEndDate) {
        this.manageEndDate = manageEndDate;
    }

    public String getManageRange() {
        return this.manageRange;
    }

    public void setManageRange(String manageRange) {
        this.manageRange = manageRange;
    }

    public String getManageRangeFashion() {
        return this.manageRangeFashion;
    }

    public void setManageRangeFashion(String manageRangeFashion) {
        this.manageRangeFashion = manageRangeFashion;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getOpenDate() {
        return this.openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getRegistDate() {
        return this.registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public String getRegistFund() {
        return this.registFund;
    }

    public void setRegistFund(String registFund) {
        this.registFund = registFund;
    }

    public String getRegistFund2() {
        return this.registFund2;
    }

    public void setRegistFund2(String registFund2) {
        this.registFund2 = registFund2;
    }

    public String getRegisterDepartment() {
        return this.registerDepartment;
    }

    public void setRegisterDepartment(String registerDepartment) {
        this.registerDepartment = registerDepartment;
    }

    public String getRegisterNo() {
        return this.registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getResultType() {
        return this.resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getRevokeDate() {
        return this.revokeDate;
    }

    public void setRevokeDate(String revokeDate) {
        this.revokeDate = revokeDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus2() {
        return this.status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public String getTreatResult() {
        return this.treatResult;
    }

    public void setTreatResult(String treatResult) {
        this.treatResult = treatResult;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCorpType_id() {
        return this.corpType_id;
    }

    public void setCorpType_id(String corpType_id) {
        this.corpType_id = corpType_id;
    }

    public String getEconomicType_id() {
        return this.economicType_id;
    }

    public void setEconomicType_id(String economicType_id) {
        this.economicType_id = economicType_id;
    }

    public String getFundCurrency_id() {
        return this.fundCurrency_id;
    }

    public void setFundCurrency_id(String fundCurrency_id) {
        this.fundCurrency_id = fundCurrency_id;
    }

    public String getFundCurrency2_id() {
        return this.fundCurrency2_id;
    }

    public void setFundCurrency2_id(String fundCurrency2_id) {
        this.fundCurrency2_id = fundCurrency2_id;
    }

    public String getOrganType_id() {
        return this.organType_id;
    }

    public void setOrganType_id(String organType_id) {
        this.organType_id = organType_id;
    }

    public String getSubReportType_id() {
        return this.subReportType_id;
    }

    public void setSubReportType_id(String subReportType_id) {
        this.subReportType_id = subReportType_id;
    }

    public String getTradeCode_id() {
        return this.tradeCode_id;
    }

    public void setTradeCode_id(String tradeCode_id) {
        this.tradeCode_id = tradeCode_id;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPengYuanCompanyName() {
        return this.pengYuanCompanyName;
    }

    public String getRefId() {
        return this.refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getFeeReport() {
        return this.feeReport;
    }

    public void setFeeReport(String feeReport) {
        this.feeReport = feeReport;
    }

    public String getLendRequestId() {
        return this.lendRequestId;
    }

    public void setLendRequestId(String lendRequestId) {
        this.lendRequestId = lendRequestId;
    }

    public CompanyContactOperateInfos getCompanyContactOperateInfos() {
        return this.companyContactOperateInfos;
    }

    public void setCompanyContactOperateInfos(
            CompanyContactOperateInfos companyContactOperateInfos) {
        this.companyContactOperateInfos = companyContactOperateInfos;
    }

    public List<CompanyManagerInfos> getManagerInfolist() {
        return this.managerInfolist;
    }

    public void setManagerInfolist(List<CompanyManagerInfos> managerInfolist) {
        this.managerInfolist = managerInfolist;
    }

    public List<CompanyShareholderInfos> getShareholderInfolist() {
        return this.shareholderInfolist;
    }

    public void setShareholderInfolist(
            List<CompanyShareholderInfos> shareholderInfolist) {
        this.shareholderInfolist = shareholderInfolist;
    }

    public String getCompanyid() {
        return this.companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getSystemid() {
        return this.systemid;
    }

    public void setSystemid(String systemid) {
        this.systemid = systemid;
    }

    public String getOperationName() {
        return this.operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubReport() {
        return this.subReport;
    }

    public void setSubReport(String subReport) {
        this.subReport = subReport;
    }

    public String getKeyid() {
        return this.keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getHasSystemError() {
        return this.hasSystemError;
    }

    public void setHasSystemError(String hasSystemError) {
        this.hasSystemError = hasSystemError;
    }

    public String getCurrencyStr() {
        return this.currencyStr;
    }

    public void setCurrencyStr(String currencyStr) {
        this.currencyStr = currencyStr;
    }

    public String getCurrencyStr2() {
        return this.currencyStr2;
    }

    public void setCurrencyStr2(String currencyStr2) {
        this.currencyStr2 = currencyStr2;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getQueryTime() {
        return this.queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public void setPengYuanCompanyName(String pengYuanCompanyName) {
        this.pengYuanCompanyName = pengYuanCompanyName;
    }

}
