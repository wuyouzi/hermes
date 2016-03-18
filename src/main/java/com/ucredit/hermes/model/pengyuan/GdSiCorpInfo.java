/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import java.math.BigDecimal;
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

import com.ucredit.hermes.enums.InsuranceCurrentStatus;
import com.ucredit.hermes.enums.SubjectionRelation;
import com.ucredit.hermes.enums.UnitType;

/**
 * 广东省个人所在单位基本信息及参保信息
 *
 * @author caoming
 */
@Entity
@Table(name = "gd_si_corp_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GdSiCorpInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -7965378686157607384L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64)
    @Index(name = "idx_gd_si_corp_infos")
    private String documentNo;

    /**
     * 单位名称
     */
    @Column(length = 512)
    private String corpName;

    /**
     * 单位曾用名
     */
    @Column(length = 512)
    private String oldName;

    /**
     * 行政区划代码
     */
    @ManyToOne
    private AreaCode areaCode;

    /**
     * 参保单位类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private UnitType unitType;

    /**
     * 经济类型
     */
    @ManyToOne
    private EconomicTypeInfo economicTypeInfo;

    /**
     * 隶属关系
     */
    private SubjectionRelation relation;

    /**
     * 行业
     */
    @ManyToOne
    private TradeInfo tradeInfo;
    /**
     * 单位养老缴费状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private InsuranceCurrentStatus currentStatus;

    /**
     * 养老首次参保日期，YYYYMMDD
     */
    @Temporal(TemporalType.DATE)
    private Date startDate;

    /**
     * 养老累计欠缴金额合计，单位：人民币
     */
    @Column(length = 16, scale = 4)
    private BigDecimal oweTotalMoney;

    /**
     * 养老累计欠缴金额，单位：人民币
     */
    @Column(length = 16, scale = 4)
    private BigDecimal oweTotalMoneyUnit;

    /**
     * 基本信息获取时间，YYYYMM，只有年和月
     */
    @Temporal(TemporalType.DATE)
    private Date baseInfoDate;

    /**
     * 最近正常参保月份，YYYYMM
     */
    @Temporal(TemporalType.DATE)
    private Date lastMonth;

    /**
     * 该月养老应缴金额合计，单位：人民币元
     */
    @Column(length = 16, scale = 4)
    private BigDecimal shouldMoneyMonth;

    /**
     * 该月养老实缴金额合计，单位：人民币元
     */
    @Column(length = 16, scale = 4)
    private BigDecimal actualMoneyMonth;
    /**
     * 该月养老补缴金额合计，单位：人民币元
     */
    @Column(length = 16, scale = 4)
    private BigDecimal patchMoneyMonth;

    /**
     * 单位养老缴费基数，单位：人民币元
     */
    @Column(length = 16, scale = 4)
    private BigDecimal insurePay;

    /**
     * 单位养老参保人数
     */
    private int unitInsureNumber;

    /**
     * 信息获取时间，YYYYMM，只有年月，没有日
     */
    private Date normalInfoDate;

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

    public TradeInfo getTradeInfo() {
        return this.tradeInfo;
    }

    public void setTradeInfo(TradeInfo tradeInfo) {
        this.tradeInfo = tradeInfo;
    }

    public SubjectionRelation getRelation() {
        return this.relation;
    }

    public void setRelation(SubjectionRelation relation) {
        this.relation = relation;
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getOldName() {
        return this.oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public AreaCode getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(AreaCode areaCode) {
        this.areaCode = areaCode;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public EconomicTypeInfo getEconomicTypeInfo() {
        return this.economicTypeInfo;
    }

    public void setEconomicTypeInfo(EconomicTypeInfo economicTypeInfo) {
        this.economicTypeInfo = economicTypeInfo;
    }

    public InsuranceCurrentStatus getCurrentStatus() {
        return this.currentStatus;
    }

    public void setCurrentStatus(InsuranceCurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getOweTotalMoney() {
        return this.oweTotalMoney;
    }

    public void setOweTotalMoney(BigDecimal oweTotalMoney) {
        this.oweTotalMoney = oweTotalMoney;
    }

    public BigDecimal getOweTotalMoneyUnit() {
        return this.oweTotalMoneyUnit;
    }

    public void setOweTotalMoneyUnit(BigDecimal oweTotalMoneyUnit) {
        this.oweTotalMoneyUnit = oweTotalMoneyUnit;
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

    public BigDecimal getShouldMoneyMonth() {
        return this.shouldMoneyMonth;
    }

    public void setShouldMoneyMonth(BigDecimal shouldMoneyMonth) {
        this.shouldMoneyMonth = shouldMoneyMonth;
    }

    public BigDecimal getActualMoneyMonth() {
        return this.actualMoneyMonth;
    }

    public void setActualMoneyMonth(BigDecimal actualMoneyMonth) {
        this.actualMoneyMonth = actualMoneyMonth;
    }

    public BigDecimal getPatchMoneyMonth() {
        return this.patchMoneyMonth;
    }

    public void setPatchMoneyMonth(BigDecimal patchMoneyMonth) {
        this.patchMoneyMonth = patchMoneyMonth;
    }

    public BigDecimal getInsurePay() {
        return this.insurePay;
    }

    public void setInsurePay(BigDecimal insurePay) {
        this.insurePay = insurePay;
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
        return "GdSiCorpInfo [corpName=" + this.corpName + ", oldName="
            + this.oldName + ", areaCode="
            + (this.areaCode == null ? "0" : this.areaCode.toString())
            + ", unitType=" + this.unitType + ", economicTypeInfo="
            + this.economicTypeInfo.toString() + ", relation=" + this.relation
            + ", tradeInfo=" + this.tradeInfo.toString() + ", currentStatus="
            + this.currentStatus + ", startDate=" + this.startDate
            + ", oweTotalMoney=" + this.oweTotalMoney + ", oweTotalMoneyUnit="
            + this.oweTotalMoneyUnit + ", baseInfoDate=" + this.baseInfoDate
            + ", lastMonth=" + this.lastMonth + ", shouldMoneyMonth="
            + this.shouldMoneyMonth + ", actualMoneyMonth="
            + this.actualMoneyMonth + ", patchMoneyMonth="
            + this.patchMoneyMonth + ", insurePay=" + this.insurePay
            + ", unitInsureNumber=" + this.unitInsureNumber
            + ", normalInfoDate=" + this.normalInfoDate + "]";
    }

}
