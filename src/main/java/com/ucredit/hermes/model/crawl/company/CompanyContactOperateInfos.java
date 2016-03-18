package com.ucredit.hermes.model.crawl.company;


/**
 * @author liuxuanyu 2016年1月7日 下午3:46:32
 */

public class CompanyContactOperateInfos {
    private Integer id;

    private String address;
    private String areaCode;
    private String areaDesc;
    private String errorMessage;
    private String postCode;
    private String tel;
    private String treatResult;
    private String type;

    private Integer companyInfos_id;
    private String subReportType_id;
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaDesc() {
        return this.areaDesc;
    }

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTreatResult() {
        return this.treatResult;
    }

    public void setTreatResult(String treatResult) {
        this.treatResult = treatResult;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCompanyInfos_id() {
        return this.companyInfos_id;
    }

    public void setCompanyInfos_id(Integer companyInfos_id) {
        this.companyInfos_id = companyInfos_id;
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
