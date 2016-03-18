/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.ucredit.hermes.enums.InfoType;

/**
 * 声明信息
 *
 * @author caoming
 */
@Entity
@Table(name = "special_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpecialInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 3869572602786543542L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 证件号
     */
    @Column(length = 64)
    @Index(name = "idx_special_infos_documentNo")
    private String documentNo;

    /**
     * 信息类型：1：非本人声明，2：本人声明。
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private InfoType infoType;

    /**
     * 信息来源单位中文名称，若是本人声明，则此处为中文“本人声明”
     */
    @Column(length = 64)
    private String infoUnit;

    @ManyToOne
    private InformationSourceUnitType infomation;

    /**
     * 信息提供单位的来源部门
     */
    @Column(length = 256)
    private String infoDepartment;

    /**
     * 信息描述事件的发生时间，格式YYYYMMDD
     */
    @Temporal(TemporalType.DATE)
    private Date occurDate;

    /**
     * 特别信息内容描述
     */
    @Lob
    private String content;

    /**
     * 信息获取日期，格式YYYYMMDD
     */
    @Temporal(TemporalType.DATE)
    private Date infoDate;

    private BaseHeadReport baseHeadReport;

    /**
     * 社保信息
     */
    @ManyToOne(optional = true)
    private GdSiPersonInfo personInfo;

    public String getDocumentNo() {
        return this.documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public InfoType getInfoType() {
        return this.infoType;
    }

    public void setInfoType(InfoType infoType) {
        this.infoType = infoType;
    }

    public String getInfoUnit() {
        return this.infoUnit;
    }

    public void setInfoUnit(String infoUnit) {
        this.infoUnit = infoUnit;
    }

    public InformationSourceUnitType getInfomation() {
        return this.infomation;
    }

    public void setInfomation(InformationSourceUnitType infomation) {
        this.infomation = infomation;
    }

    public String getInfoDepartment() {
        return this.infoDepartment;
    }

    public void setInfoDepartment(String infoDepartment) {
        this.infoDepartment = infoDepartment;
    }

    public Date getOccurDate() {
        return this.occurDate;
    }

    public void setOccurDate(Date occurDate) {
        this.occurDate = occurDate;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getInfoDate() {
        return this.infoDate;
    }

    public void setInfoDate(Date infoDate) {
        this.infoDate = infoDate;
    }

    public BaseHeadReport getBaseHeadReport() {
        return this.baseHeadReport;
    }

    public void setBaseHeadReport(BaseHeadReport baseHeadReport) {
        this.baseHeadReport = baseHeadReport;
    }

    public GdSiPersonInfo getPersonInfo() {
        return this.personInfo;
    }

    public void setPersonInfo(GdSiPersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    /*
     * (non-Javadoc)
     * @see com.ucredit.hermes.model.IDOwner#getId()
     */
    @Override
    public Integer getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * @see com.ucredit.hermes.model.IDOwner#setId(java.lang.Object)
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SpecialInfo [infoType=" + this.infoType + ", infoUnit="
            + this.infoUnit + ", infomation=" + this.infomation.toString()
            + ", infoDepartment=" + this.infoDepartment + ", occurDate="
            + this.occurDate + ", content=" + this.content + ", infoDate="
            + this.infoDate + "]";
    }

}
