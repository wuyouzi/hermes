package com.ucredit.hermes.model.crawl.company;


/**
 * @author liuxuanyu 2016年1月7日 下午3:47:51
 */

public class CompanyManagerInfos {
    private Integer id;

    private String errorMessage;
    private String name;
    private String treatResult;
    private Integer companyInfos_id;
    private String position_id;
    private String subReportType_id;
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPosition_id() {
        return this.position_id;
    }

    public void setPosition_id(String position_id) {
        this.position_id = position_id;
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
