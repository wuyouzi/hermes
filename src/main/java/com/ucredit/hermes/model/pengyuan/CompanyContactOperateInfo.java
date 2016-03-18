/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.enums.CompanyOperateType;
import com.ucredit.hermes.enums.TreatResult;

/**
 * 企业注册和经营地址及电话信息
 *
 * @author liuqianqian
 */
@Entity
@Table(name = "company_contact_operate_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompanyContactOperateInfo extends BaseModel<Integer> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3430492350254529818L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主表 company_infos
     */
    @Column(name="companyInfos_id")
    private Integer companyInfos_id;

    /**
     * 企业注册经营类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private CompanyOperateType type;

    /**
     * 行政区划代码
     */
    @Column(length = 8)
    private String areaCode;

    /**
     * 行政区划代码对应的行政区划名称
     */
    @Column(length = 256)
    private String areaDesc;

    /**
     * 注册地址
     */
    @Column(length = 512)
    private String address;

    /**
     * 注册电话
     */
    @Column(length = 256)
    private String tel;

    /**
     * 邮编
     */
    @Column(length = 6)
    private String postCode;

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
    private String errorMessage;

    /**
     * @return the id
     */
    @Override
    public Integer getId() {
        return this.id;
    }

    /**
     * @param id
     *        the id to set
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyInfos_id() {
        return this.companyInfos_id;
    }

    public void setCompanyInfos_id(Integer companyInfos_id) {
        this.companyInfos_id = companyInfos_id;
    }

    /**
     * @return the type
     */
    public CompanyOperateType getType() {
        return this.type;
    }

    /**
     * @param type
     *        the type to set
     */
    public void setType(CompanyOperateType type) {
        this.type = type;
    }

    /**
     * @return the areaCode
     */
    public String getAreaCode() {
        return this.areaCode;
    }

    /**
     * @param areaCode
     *        the areaCode to set
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * @return the areaDesc
     */
    public String getAreaDesc() {
        return this.areaDesc;
    }

    /**
     * @param areaDesc
     *        the areaDesc to set
     */
    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * @param address
     *        the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return this.tel;
    }

    /**
     * @param tel
     *        the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return the postCode
     */
    public String getPostCode() {
        return this.postCode;
    }

    /**
     * @param postCode
     *        the postCode to set
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * @return the subReportType
     */
    public SubReportInfo getSubReportType() {
        return this.subReportType;
    }

    /**
     * @param subReportType
     *        the subReportType to set
     */
    public void setSubReportType(SubReportInfo subReportType) {
        this.subReportType = subReportType;
    }

    /**
     * @return the treatResult
     */
    public TreatResult getTreatResult() {
        return this.treatResult;
    }

    /**
     * @param treatResult
     *        the treatResult to set
     */
    public void setTreatResult(TreatResult treatResult) {
        this.treatResult = treatResult;
    }

    /**
     * @return String
     */
    @Transient
    public String getTreatResultString() {
        return this.treatResult == null ? "" : this.treatResult.toString();
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * @param errorMessage
     *        the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
