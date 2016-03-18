/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.enums.TreatResult;
import com.ucredit.hermes.model.NonEqualsBaseModel;

/**
 * 所有主表基本字段
 *
 * @author caoming
 */
@Embeddable
public class BaseReportType extends NonEqualsBaseModel<BaseReportType> {

    /**
     *
     */
    private static final long serialVersionUID = 7096668894794386040L;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 数据来源
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private DataChannel dataChannel;

    /**
     * 每条数据的md5值
     */
    @Column(length = 64)
    private String md5;

    /**
     * 子报告ID
     */
    @ManyToOne(optional = true)
    private SubReportInfo subReportType;

    /**
     * 子报告的状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private TreatResult treatResult;

    /**
     * 错误状态码
     */
    @Column(length = 32)
    private String treatErrorCode;

    /**
     * 返回的错误信息
     */
    @Column(length = 128)
    private String errorMessage;

    /**
     * 返回结果的状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ResultType resultType;

    /**
     * 收费子报告
     */
    @Column(length = 32)
    private String subReportTypeCost;
    /**
     * 是否有效
     */
    private boolean enabled = true;

    /**
     * 我方流水号
     */
    @Column(length = 128)
    private String refId;

    public String getTreatErrorCode() {
        return this.treatErrorCode;
    }

    public void setTreatErrorCode(String treatErrorCode) {
        this.treatErrorCode = treatErrorCode;
    }

    public String getSubReportTypeCost() {
        return this.subReportTypeCost;
    }

    public void setSubReportTypeCost(String subReportTypeCost) {
        this.subReportTypeCost = subReportTypeCost;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public DataChannel getDataChannel() {
        return this.dataChannel;
    }

    public void setDataChannel(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public SubReportInfo getSubReportType() {
        return this.subReportType;
    }

    public void setSubReportType(SubReportInfo subReportType) {
        this.subReportType = subReportType;
    }

    public TreatResult getTreatResult() {
        return this.treatResult;
    }

    public void setTreatResult(TreatResult treatResult) {
        this.treatResult = treatResult;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResultType getResultType() {
        return this.resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRefId() {
        return this.refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

}
