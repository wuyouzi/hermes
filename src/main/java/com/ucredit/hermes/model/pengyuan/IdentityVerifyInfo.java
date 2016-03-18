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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.ucredit.hermes.enums.Gender;

/**
 * 身份证号码校验信息
 *
 * @author caoming
 */
@Entity
@Table(name = "identity_verify_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IdentityVerifyInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 6011571214059274704L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 被查询者证件号
     */
    @Column(length = 64)
    @Index(name = "idx_identity_verify_infos_documentNo")
    private String documentNo;

    /**
     * 出生日期，格式：YYYYMMDD
     */
    @Temporal(TemporalType.DATE)
    private Date birthday;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private Gender gender;

    /**
     * 原是发证地区，地区校验位未通过，则返回为空字符串
     */
    @Column(length = 64)
    private String originalAddress;

    /**
     * 校验位结果
     */
    private boolean verifyOfParity;

    /**
     * 地区位结果
     */
    private boolean verifyOfArea;

    /**
     * 出生日期结果
     */
    private boolean verifyOfBirthday;

    /**
     * 是否为18位证件号码
     */
    private boolean is18Identify;

    /**
     * 校验结果，只有上述都为1时，此字段才为1
     */
    private boolean verifyResult;

    private BaseHeadReport baseHeadReport;
    /**
     * 广东社保信息
     */
    @ManyToOne(optional = true)
    private GdSiPersonInfo personInfo;

    public String getDocumentNo() {
        return this.documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getOriginalAddress() {
        return this.originalAddress;
    }

    public void setOriginalAddress(String originalAddress) {
        this.originalAddress = originalAddress;
    }

    public boolean isVerifyOfParity() {
        return this.verifyOfParity;
    }

    public void setVerifyOfParity(boolean verifyOfParity) {
        this.verifyOfParity = verifyOfParity;
    }

    public boolean isVerifyOfArea() {
        return this.verifyOfArea;
    }

    public void setVerifyOfArea(boolean verifyOfArea) {
        this.verifyOfArea = verifyOfArea;
    }

    public boolean isVerifyOfBirthday() {
        return this.verifyOfBirthday;
    }

    public void setVerifyOfBirthday(boolean verifyOfBirthday) {
        this.verifyOfBirthday = verifyOfBirthday;
    }

    public boolean isIs18Identify() {
        return this.is18Identify;
    }

    public void setIs18Identify(boolean is18Identify) {
        this.is18Identify = is18Identify;
    }

    public boolean isVerifyResult() {
        return this.verifyResult;
    }

    public void setVerifyResult(boolean verifyResult) {
        this.verifyResult = verifyResult;
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
        return "IdentityVerifyInfo [documentNo=" + this.documentNo
            + ", birthday=" + this.birthday + ", gender=" + this.gender
            + ", originalAddress=" + this.originalAddress + ", verifyOfParity="
            + this.verifyOfParity + ", verifyOfArea=" + this.verifyOfArea
            + ", verifyOfBirthday=" + this.verifyOfBirthday + ", is18Identify="
            + this.is18Identify + ", verifyResult=" + this.verifyResult;
    }

}
