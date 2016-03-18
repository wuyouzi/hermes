/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import java.util.Date;
import java.util.Set;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.ucredit.hermes.enums.EducationType;
import com.ucredit.hermes.enums.Gender;

/**
 * 学历/学籍信息表
 *
 * @author caoming
 */
@Entity
@Table(name = "educational_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EducationInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -8492991252014823723L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 姓名
     */
    @Column(length = 64)
    @Index(name = "idx_eductation_infos_name")
    private String name;

    /**
     * 证件号码
     */
    @Column(length = 64)
    @Index(name = "idx_eductation_infos_documentNo")
    private String documentNo;

    /**
     * 最高学历
     */
    @Column(length = 32)
    private String degree;

    /**
     * 所学专业
     */
    @Column(length = 64)
    private String specialty;

    /**
     * 毕业院校
     */
    @Column(length = 64)
    private String college;

    /**
     * 毕业时间，格式YYYY
     */
    private int graduateTime;

    /**
     * 毕业年限
     */
    private int graduateYears;

    /**
     * 出生日期，格式yyyyMMdd
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
     * 年龄
     */
    private Integer age;

    /**
     * 原始发证地区
     */
    private String originalAddress;

    /**
     * 件号码校验结果
     */
    private String verifyResult;

    /**
     * 描述信息
     */
    private String riskAndAdviceInfo;

    /**
     * 查询批次号
     */
    @Column(length = 32)
    private String batNo;

    /**
     * 查询操作员登录名
     */
    @Column(length = 32)
    private String queryUserID;

    /**
     * 查询申请时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveTime;

    /**
     * 查询单位名称
     */
    @Column(length = 32)
    private String unitName;

    /**
     * 分支机构名称
     */
    @Column(length = 32)
    private String subOrgan;

    /**
     *
     */
    @Column(length = 32)
    private String reportID;

    /**
     * 学历/学籍信息
     */
    @Transient
    private Set<DegreeCollegeInfo> degreeCollegeInfos;

    /**
     * 信息来源单位，如果查询单位与信息来源单位不同则为-1
     */
    private int infoUnit;

    @ManyToOne
    private InformationSourceUnitType information;

    private BaseReportType baseReportType;

    @Version
    private int version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date queryTime;

    /**
     * 最后更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    /**
     * 信息类型（学历还是学籍）
     */
    @Enumerated(EnumType.STRING)
    private EducationType educationType;

    @Column(length = 64)
    private String lendRequestId;

    @Column(length = 64)
    private String keyid;

    public String getLendRequestId() {
        return this.lendRequestId;
    }

    public void setLendRequestId(String lendRequestId) {
        this.lendRequestId = lendRequestId;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public String getCollege() {
        return this.college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public int getGraduateTime() {
        return this.graduateTime;
    }

    public void setGraduateTime(int graduateTime) {
        this.graduateTime = graduateTime;
    }

    public String getSpecialty() {
        return this.specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getDegree() {
        return this.degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public int getInfoUnit() {
        return this.infoUnit;
    }

    public void setInfoUnit(int infoUnit) {
        this.infoUnit = infoUnit;
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

    public int getGraduateYears() {
        return this.graduateYears;
    }

    public void setGraduateYears(int graduateYears) {
        this.graduateYears = graduateYears;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOriginalAddress() {
        return this.originalAddress;
    }

    public void setOriginalAddress(String originalAddress) {
        this.originalAddress = originalAddress;
    }

    public String getVerifyResult() {
        return this.verifyResult;
    }

    public void setVerifyResult(String verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getRiskAndAdviceInfo() {
        return this.riskAndAdviceInfo;
    }

    public void setRiskAndAdviceInfo(String riskAndAdviceInfo) {
        this.riskAndAdviceInfo = riskAndAdviceInfo;
    }

    public Set<DegreeCollegeInfo> getDegreeCollegeInfos() {
        return this.degreeCollegeInfos;
    }

    public void setDegreeCollegeInfos(Set<DegreeCollegeInfo> degreeCollegeInfos) {
        this.degreeCollegeInfos = degreeCollegeInfos;
    }

    public EducationType getEducationType() {
        return this.educationType;
    }

    public void setEducationType(EducationType educationType) {
        this.educationType = educationType;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getQueryUserID() {
        return this.queryUserID;
    }

    public void setQueryUserID(String queryUserID) {
        this.queryUserID = queryUserID;
    }

    public Date getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSubOrgan() {
        return this.subOrgan;
    }

    public void setSubOrgan(String subOrgan) {
        this.subOrgan = subOrgan;
    }

    public String getBatNo() {
        return this.batNo;
    }

    public void setBatNo(String batNo) {
        this.batNo = batNo;
    }

    public String getReportID() {
        return this.reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public Date getQueryTime() {
        return this.queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getKeyid() {
        return this.keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    @Override
    public String toString() {
        return "EducationInfo [name=" + this.name + ", documentNo="
                + this.documentNo + ", gender=" + this.gender + ", birthday="
                + this.birthday + ", levelNo=" + ", college=" + this.college
            + ", startTime=" + ", graduateTime=" + this.graduateTime
            + ", studyStyle=" + ", studyType=" + ", specialty="
            + this.specialty + ", degree=" + this.degree + ", studyResult="
            + ", photo=" + ", photoStyle=" + ", infoUnit=" + this.infoUnit
                + ", information=" + this.information + ", educationType="
                + this.educationType + "]";
    }

}
