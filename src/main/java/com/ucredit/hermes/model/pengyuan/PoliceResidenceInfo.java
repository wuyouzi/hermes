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
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.ucredit.hermes.enums.Gender;
import com.ucredit.hermes.enums.MarryStatus;

/**
 * 个人户籍信息
 *
 * @author caoming
 */
@Entity
@Table(name = "police_residence_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PoliceResidenceInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -8650707679891779282L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 姓名
     */
    @Column(length = 64, nullable = false)
    @Index(name = "idx_police_residence_infos_name")
    private String name;

    /**
     * 身份证号
     */
    @Column(length = 64)
    @Index(name = "idx_police_residence_infos_documentNo")
    private String documentNo;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private Gender gender;

    /**
     * 出生日期
     */
    @Temporal(TemporalType.DATE)
    private Date birthday;

    /**
     * 曾用名
     */
    @Column(length = 64)
    private String historyName;

    /**
     * 户籍所在地址
     */
    @Column(length = 128)
    private String registerAddress;

    /**
     * 所属派出所
     */
    @Column(length = 256)
    private String policeStation;

    /**
     * 服务处所
     */
    @Column(length = 256)
    private String serviceStation;

    /**
     * 文化程度
     */
    @Column(length = 256)
    private String educationLevel;

    /**
     * 住址
     */
    @Column(length = 512)
    private String residentAddress;

    /**
     * 民族
     */
    @Column(length = 64)
    private String nation;

    /**
     * 婚姻状况
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private MarryStatus marryStatus;

    /**
     * 照片
     */
    @Lob
    private String photo;

    /**
     * 信息来源单位ID，如果查询单位与信息来源单位不相同则为-1
     */
    private int infoUtil;

    /**
     * 基本主表类型
     */
    private BaseReportType baseReportType;

    @Version
    private int version;

    @ManyToOne(optional = true)
    private GdSiPersonInfo personInfo;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public GdSiPersonInfo getPersonInfo() {
        return this.personInfo;
    }

    public void setPersonInfo(GdSiPersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public BaseReportType getBaseReportType() {
        return this.baseReportType;
    }

    public void setBaseReportType(BaseReportType baseReportType) {
        this.baseReportType = baseReportType;
    }

    public String getDocumentNo() {
        return this.documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHistoryName() {
        return this.historyName;
    }

    public void setHistoryName(String historyName) {
        this.historyName = historyName;
    }

    public String getRegisterAddress() {
        return this.registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getPoliceStation() {
        return this.policeStation;
    }

    public void setPoliceStation(String policeStation) {
        this.policeStation = policeStation;
    }

    public String getServiceStation() {
        return this.serviceStation;
    }

    public void setServiceStation(String serviceStation) {
        this.serviceStation = serviceStation;
    }

    public String getEducationLevel() {
        return this.educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getResidentAddress() {
        return this.residentAddress;
    }

    public void setResidentAddress(String residentAddress) {
        this.residentAddress = residentAddress;
    }

    public String getNation() {
        return this.nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public MarryStatus getMarryStatus() {
        return this.marryStatus;
    }

    public void setMarryStatus(MarryStatus marryStatus) {
        this.marryStatus = marryStatus;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getInfoUtil() {
        return this.infoUtil;
    }

    public void setInfoUtil(int infoUtil) {
        this.infoUtil = infoUtil;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PoliceResidenceInfo [name=" + this.name + ", documentNo="
            + this.documentNo + ", gender=" + this.gender + ", birthday="
            + this.birthday + ", historyName=" + this.historyName
            + ", registerAddress=" + this.registerAddress + ", policeStation="
            + this.policeStation + ", serviceStation=" + this.serviceStation
            + ", educationLevel=" + this.educationLevel + ", residentAddress="
            + this.residentAddress + ", nation=" + this.nation
            + ", marryStatus=" + this.marryStatus + ", photo=" + this.photo
            + ", infoUtil=" + this.infoUtil + "]";
    }

}
