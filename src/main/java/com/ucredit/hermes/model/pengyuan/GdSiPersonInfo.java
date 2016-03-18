/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import org.hibernate.validator.constraints.Length;

import com.ucredit.hermes.enums.InsuranceCurrentStatus;
import com.ucredit.hermes.enums.InsuranceType;
import com.ucredit.hermes.enums.UnitType;

/**
 * 广东个人社保信息
 *
 * @author caoming
 */
@Entity
@Table(name = "gd_si_person_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GdSiPersonInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -7392521390155678788L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 社保账户顺序号，从1开始，依次递增
     */
    private int number;

    /**
     * 被查询者姓名
     */
    @Column(length = 64)
    @Index(name = "idx_gd_si_person_infos_name")
    private String name;

    /**
     * 被查询者证件号
     */
    @Column(length = 64)
    @Index(name = "idx_gd_si_person_infos_documentNo")
    private String documentNo;

    /**
     * 行政区划代码
     */
    @ManyToOne
    private AreaCode areaCode;

    /**
     * 个人缴费状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private InsuranceCurrentStatus currentStatus;

    /**
     * 中断或终止缴费原因
     */
    @ManyToOne
    private StopReason stopReason;

    /**
     * 首次参保时间
     */
    @Temporal(TemporalType.DATE)
    private Date startDate;

    /**
     * 养老保险账户余额，单位：人民币
     */
    @Column(length = 16, scale = 4)
    private BigDecimal endowmentAmount;

    /**
     * 养老账户个人缴费累计金额，单位：人民币
     */
    @Column(length = 16, scale = 4)
    private BigDecimal endowmentAmountPerson;

    /**
     * 养老账户单位缴费累计金额，单位：人民币
     */
    @Column(length = 16, scale = 4)
    private BigDecimal endowmentAmountCorp;

    /**
     * 累计缴费月数
     */
    private int totalMonths;

    /**
     * 从事特殊工种月数
     */
    private int specialJobMonths;

    /**
     * 基本信息获取时间，格式YYYYMM
     */
    @Temporal(TemporalType.DATE)
    private Date baseInfoDate;

    /**
     * 最近正常参保月份，YYYYMM
     */
    @Temporal(TemporalType.DATE)
    private Date lastMonth;

    /**
     * 缴费基数，单位：人民币
     */
    @Column(length = 16, scale = 4)
    private BigDecimal insurePay;

    /**
     * 该月个人养老缴费金额，单位：人民币
     */
    @Column(length = 16, scale = 4)
    private BigDecimal endowmentMoneyMonthPerson;

    /**
     * 该月单位缴费划入个人账户金额，单位：人民币
     */
    @Column(length = 16, scale = 4)
    private BigDecimal endowmentMoneyMonthCorp;

    /**
     * 最近正常参保单位
     */
    @Column(length = 64)
    @Length(max = 64)
    private String lastUnitName;

    /**
     * 参保单位类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private UnitType unitType;

    /**
     * 单位养老参保人数
     */
    private int unitInsureNumber;

    /**
     * 最近参保信息情况信息获取时间，格式YYYYMM
     */
    @Temporal(TemporalType.DATE)
    private Date normalInfoDate;

    /**
     * 参保种类
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private InsuranceType insuranceType;

    /**
     * 最近36月养老参保信息情况信息获取时间，格式YYYYMM
     */
    @Temporal(TemporalType.DATE)
    private Date historyInfoDate;

    /**
     * 过去12个月参保次数,(月参保情况(insuranceState)为9的不计入参保)
     */
    private int timesOfLast12Months;

    /**
     * 过去24个月参保次数，(月参保情况(insuranceState)为9的不计入参保)
     */
    private int timesOfLast24Months;

    /**
     * 最近连续参保次数，(月参保情况(insuranceState)为9的不计入参保)
     */
    private int seriesTimesRecently;

    /**
     * 近36个月内参保单位总数
     */
    private int unitCount;

    private BaseReportType baseReportType;

    /**
     * 个人户籍信息
     */
    @Transient
    private PoliceResidenceInfo policeResidenceInfo;

    /**
     * 个人学历信息
     */
    @Transient
    private List<EducationInfo> educationInfos;

    /**
     * 个人学籍信息
     */
    @Transient
    private List<EducationInSchoolInfo> educationInSchoolInfos;

    /**
     * 广东个人近36个月养老参保信息
     */
    @Transient
    private List<HistorySiPersonInfo> historySiPersonInfos;

    /**
     * 个人所在单位基本信息
     */
    @Transient
    private List<GdSiCorpInfo> gdSiCorpInfos;

    /**
     * 身份证号码校验信息
     */
    @Transient
    private IdentityVerifyInfo identityVerifyInfo;

    /**
     * 近两年历史查询明细
     */
    @Transient
    private List<HistoryQueryInfo> historyQueryInfos;

    /**
     * 特别声明
     */
    @Transient
    private List<SpecialInfo> specialInfos;

    @Version
    private int version;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public PoliceResidenceInfo getPoliceResidenceInfo() {
        return this.policeResidenceInfo;
    }

    public void setPoliceResidenceInfo(PoliceResidenceInfo policeResidenceInfo) {
        this.policeResidenceInfo = policeResidenceInfo;
    }

    public List<EducationInfo> getEducationInfos() {
        return this.educationInfos;
    }

    public void setEducationInfos(List<EducationInfo> educationInfos) {
        this.educationInfos = educationInfos;
    }

    public List<EducationInSchoolInfo> getEducationInSchoolInfos() {
        return this.educationInSchoolInfos;
    }

    public void setEducationInSchoolInfos(
            List<EducationInSchoolInfo> educationInSchoolInfos) {
        this.educationInSchoolInfos = educationInSchoolInfos;
    }

    public List<HistorySiPersonInfo> getHistorySiPersonInfos() {
        return this.historySiPersonInfos;
    }

    public void setHistorySiPersonInfos(
            List<HistorySiPersonInfo> historySiPersonInfos) {
        this.historySiPersonInfos = historySiPersonInfos;
    }

    public List<GdSiCorpInfo> getGdSiCorpInfos() {
        return this.gdSiCorpInfos;
    }

    public void setGdSiCorpInfos(List<GdSiCorpInfo> gdSiCorpInfos) {
        this.gdSiCorpInfos = gdSiCorpInfos;
    }

    public IdentityVerifyInfo getIdentityVerifyInfo() {
        return this.identityVerifyInfo;
    }

    public void setIdentityVerifyInfo(IdentityVerifyInfo identityVerifyInfo) {
        this.identityVerifyInfo = identityVerifyInfo;
    }

    public List<HistoryQueryInfo> getHistoryQueryInfos() {
        return this.historyQueryInfos;
    }

    public void setHistoryQueryInfos(List<HistoryQueryInfo> historyQueryInfos) {
        this.historyQueryInfos = historyQueryInfos;
    }

    public List<SpecialInfo> getSpecialInfos() {
        return this.specialInfos;
    }

    public void setSpecialInfos(List<SpecialInfo> specialInfos) {
        this.specialInfos = specialInfos;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public AreaCode getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(AreaCode areaCode) {
        this.areaCode = areaCode;
    }

    public InsuranceCurrentStatus getCurrentStatus() {
        return this.currentStatus;
    }

    public void setCurrentStatus(InsuranceCurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public StopReason getStopReason() {
        return this.stopReason;
    }

    public void setStopReason(StopReason stopReason) {
        this.stopReason = stopReason;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getEndowmentAmount() {
        return this.endowmentAmount;
    }

    public void setEndowmentAmount(BigDecimal endowmentAmount) {
        this.endowmentAmount = endowmentAmount;
    }

    public BigDecimal getEndowmentAmountPerson() {
        return this.endowmentAmountPerson;
    }

    public void setEndowmentAmountPerson(BigDecimal endowmentAmountPerson) {
        this.endowmentAmountPerson = endowmentAmountPerson;
    }

    public BigDecimal getEndowmentAmountCorp() {
        return this.endowmentAmountCorp;
    }

    public void setEndowmentAmountCorp(BigDecimal endowmentAmountCorp) {
        this.endowmentAmountCorp = endowmentAmountCorp;
    }

    public int getTotalMonths() {
        return this.totalMonths;
    }

    public void setTotalMonths(int totalMonths) {
        this.totalMonths = totalMonths;
    }

    public int getSpecialJobMonths() {
        return this.specialJobMonths;
    }

    public void setSpecialJobMonths(int specialJobMonths) {
        this.specialJobMonths = specialJobMonths;
    }

    public Date getBaseInfoDate() {
        return this.baseInfoDate;
    }

    public void setBaseInfoDate(Date baseInfoDate) {
        this.baseInfoDate = baseInfoDate;
    }

    public Date getLastMonth() {
        return this.lastMonth;
    }

    public void setLastMonth(Date lastMonth) {
        this.lastMonth = lastMonth;
    }

    public BigDecimal getInsurePay() {
        return this.insurePay;
    }

    public void setInsurePay(BigDecimal insurePay) {
        this.insurePay = insurePay;
    }

    public BigDecimal getEndowmentMoneyMonthPerson() {
        return this.endowmentMoneyMonthPerson;
    }

    public void setEndowmentMoneyMonthPerson(
            BigDecimal endowmentMoneyMonthPerson) {
        this.endowmentMoneyMonthPerson = endowmentMoneyMonthPerson;
    }

    public BigDecimal getEndowmentMoneyMonthCorp() {
        return this.endowmentMoneyMonthCorp;
    }

    public void setEndowmentMoneyMonthCorp(BigDecimal endowmentMoneyMonthCorp) {
        this.endowmentMoneyMonthCorp = endowmentMoneyMonthCorp;
    }

    public String getLastUnitName() {
        return this.lastUnitName;
    }

    public void setLastUnitName(String lastUnitName) {
        this.lastUnitName = lastUnitName;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public int getUnitInsureNumber() {
        return this.unitInsureNumber;
    }

    public void setUnitInsureNumber(int unitInsureNumber) {
        this.unitInsureNumber = unitInsureNumber;
    }

    public Date getNormalInfoDate() {
        return this.normalInfoDate;
    }

    public void setNormalInfoDate(Date normalInfoDate) {
        this.normalInfoDate = normalInfoDate;
    }

    public InsuranceType getInsuranceType() {
        return this.insuranceType;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public Date getHistoryInfoDate() {
        return this.historyInfoDate;
    }

    public void setHistoryInfoDate(Date historyInfoDate) {
        this.historyInfoDate = historyInfoDate;
    }

    public int getTimesOfLast12Months() {
        return this.timesOfLast12Months;
    }

    public void setTimesOfLast12Months(int timesOfLast12Months) {
        this.timesOfLast12Months = timesOfLast12Months;
    }

    public int getTimesOfLast24Months() {
        return this.timesOfLast24Months;
    }

    public void setTimesOfLast24Months(int timesOfLast24Months) {
        this.timesOfLast24Months = timesOfLast24Months;
    }

    public int getSeriesTimesRecently() {
        return this.seriesTimesRecently;
    }

    public void setSeriesTimesRecently(int seriesTimesRecently) {
        this.seriesTimesRecently = seriesTimesRecently;
    }

    public int getUnitCount() {
        return this.unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public BaseReportType getBaseReportType() {
        return this.baseReportType;
    }

    public void setBaseReportType(BaseReportType baseReportType) {
        this.baseReportType = baseReportType;
    }

    @Override
    public String toString() {
//       String codeString =  this.areaCode == null ? "" : this.areaCode
//            .toString();
//       String reasonString = this.stopReason == null ? "" :this.stopReason.toString();
        return "GdSiPersonInfo [id=" + this.id + ", number=" + this.number
            + ", name=" + this.name + ", documentNo=" + this.documentNo
            + ", areaCode="
            + (this.areaCode == null ? "" : this.areaCode.toString())
            + ", currentStatus=" + this.currentStatus + ", stopReason="
            + (this.stopReason == null ? "" : this.stopReason.toString())
            + ", startDate=" + this.startDate + ", endowmentAmount="
            + this.endowmentAmount + ", endowmentAmountPerson="
            + this.endowmentAmountPerson + ", endowmentAmountCorp="
            + this.endowmentAmountCorp + ", totalMonths=" + this.totalMonths
            + ", specialJobMonths=" + this.specialJobMonths + ", baseInfoDate="
            + this.baseInfoDate + ", lastMonth=" + this.lastMonth
            + ", insurePay=" + this.insurePay + ", endowmentMoneyMonthPerson="
            + this.endowmentMoneyMonthPerson + ", endowmentMoneyMonthCorp="
            + this.endowmentMoneyMonthCorp + ", lastUnitName="
            + this.lastUnitName + ", unitType=" + this.unitType
            + ", unitInsureNumber=" + this.unitInsureNumber
            + ", normalInfoDate=" + this.normalInfoDate + ", insuranceType="
            + this.insuranceType + ", historyInfoDate=" + this.historyInfoDate
            + ", timesOfLast12Months=" + this.timesOfLast12Months
            + ", timesOfLast24Months=" + this.timesOfLast24Months
            + ", seriesTimesRecently=" + this.seriesTimesRecently
            + ", unitCount=" + this.unitCount + "]";
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
}
