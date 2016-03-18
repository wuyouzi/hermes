package com.ucredit.hermes.model.crawl.company;


/**
 * @author liuxuanyu 2016年1月7日 下午3:47:20
 */

public class CompanyShareholderInfos {

    private Integer id;

    private String contributiveDate;

    private String contributiveFund;

    private String contributivePercent;

    private String errorMessage;

    private String name;

    private String treatResult;

    private Integer companyInfos_id;

    private String fundCurrency_id;

    private String subReportType_id;

    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContributiveDate() {
        return this.contributiveDate;
    }

    public void setContributiveDate(String contributiveDate) {
        this.contributiveDate = contributiveDate;
    }

    public String getContributiveFund() {
        return this.contributiveFund;
    }

    public void setContributiveFund(String contributiveFund) {
        this.contributiveFund = contributiveFund;
    }

    public String getContributivePercent() {
        return this.contributivePercent;
    }

    public void setContributivePercent(String contributivePercent) {
        this.contributivePercent = contributivePercent;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTreatResult() {
        return this.treatResult;
    }

    public void setTreatResult(String treatResult) {
        this.treatResult = treatResult;
    }

    public Integer getCompanyInfos_id() {
        return this.companyInfos_id;
    }

    public void setCompanyInfos_id(Integer companyInfos_id) {
        this.companyInfos_id = companyInfos_id;
    }

    public String getFundCurrency_id() {
        return this.fundCurrency_id;
    }

    public void setFundCurrency_id(String fundCurrency_id) {
        this.fundCurrency_id = fundCurrency_id;
    }

    public String getSubReportType_id() {
        return this.subReportType_id;
    }

    public void setSubReportType_id(String subReportType_id) {
        this.subReportType_id = subReportType_id;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
