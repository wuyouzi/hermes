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
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.ucredit.hermes.enums.Gender;

/**
 * 学籍信息
 *
 * @author caoming
 */
@Entity
@Table(name = "education_in_school_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EducationInSchoolInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 424448758538017869L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 被查询者姓名
     */
    @Column(length = 64)
    @Index(name = "idx_education_in_school_infos_name")
    private String name;

    /**
     * 被查询者证件号
     */
    @Column(length = 64)
    @Index(name = "idx_education_in_school_infos_documentNo")
    private String documentNo;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private Gender gender;

    /**
     * 出生日期，格式yyyyMMdd
     */
    @Temporal(TemporalType.DATE)
    private Date birthday;

    /**
     * 就读院校
     */
    @Column(length = 64)
    private String college;

    /**
     * 入学时间，格式yyyy
     */
    private int startTime;

    /**
     * 学历类别，例如：研究生、普通、成人、网络教育等
     */
    @Column(length = 32)
    private String studyType;

    /**
     * 所学专业
     */
    @Column(length = 64)
    private String specialty;

    /**
     * 学历层次
     */
    @Column(length = 32)
    private String degree;

    /**
     * 信息来源单位，如果查询单位与信息来源单位不同则为-1
     */
    private int infoUnit;

    @ManyToOne
    private InformationSourceUnitType information;

    private BaseReportType baseReportType;

    @Version
    private int version;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getInfoUnit() {
        return this.infoUnit;
    }

    public void setInfoUnit(int infoUnit) {
        this.infoUnit = infoUnit;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getCollege() {
        return this.college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getSpecialty() {
        return this.specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getStudyType() {
        return this.studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public String getDegree() {
        return this.degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public InformationSourceUnitType getInformation() {
        return this.information;
    }

    public void setInformation(InformationSourceUnitType information) {
        this.information = information;
    }

    public BaseReportType getBaseReportType() {
        return this.baseReportType;
    }

    public void setBaseReportType(BaseReportType baseReportType) {
        this.baseReportType = baseReportType;
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
        return "EducationInSchoolInfo [name=" + this.name + ", documentNo="
            + this.documentNo + ", gender=" + this.gender + ", birthday="
            + this.birthday + ", college=" + this.college + ", startTime="
            + this.startTime + ", studyType=" + this.studyType + ", specialty="
            + this.specialty + ", degree=" + this.degree + ", infoUtil="
            + this.infoUnit + ", information=" + this.information + "]";
    }

}
