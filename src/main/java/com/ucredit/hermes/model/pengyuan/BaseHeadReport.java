/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;

import com.ucredit.hermes.enums.TreatResult;
import com.ucredit.hermes.model.NonEqualsBaseModel;

/**
 * 附表主要信息
 * 
 * @author caoming
 */
@Embeddable
public class BaseHeadReport extends NonEqualsBaseModel<BaseHeadReport> {

    /**
     * 
     */
    private static final long serialVersionUID = -7290571276539608180L;
    /**
     * 子报告ID
     */
    @ManyToOne(optional = true)
    private SubReportInfo subReportType;

    /**
     * 子报告查询状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private TreatResult treatResult;

    /**
     * 错误信息
     */
    @Column(length = 128)
    @Length(max = 128)
    private String errorMessage;

    /**
     * 错误代码
     */
    @Column(length = 32)
    @Length(max = 32)
    private String errorCode;

    /**
     * 收费子报告
     */
    @Column(length = 32)
    @Length(max = 32)
    private String subReportTypeCost;

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

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getSubReportTypeCost() {
        return this.subReportTypeCost;
    }

    public void setSubReportTypeCost(String subReportTypeCost) {
        this.subReportTypeCost = subReportTypeCost;
    }

}
