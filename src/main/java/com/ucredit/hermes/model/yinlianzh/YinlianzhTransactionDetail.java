package com.ucredit.hermes.model.yinlianzh;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 交易明细
 *
 * @author zhouuwyuan
 */
@Entity
@Table(name = "yinlianzh_transactions_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YinlianzhTransactionDetail extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -8011563786189014967L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 主表 yinlianzh_transactions_info
     */
    @Column(name = "yinlianzh_transactions_info_id")
    private Integer yinlianzhTransactionsInfoId;
    /**
     * 卡号识别码
     */
    private String carDistinguishCode;
    /**
     * 交易时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    @Transient
    private String transactionDateString;
    /**
     * 记账日期
     */
    @Temporal(TemporalType.DATE)
    private Date AccountDate;
    /**
     * 交易币种
     */
    @ManyToOne(optional = true)
    private YinlianzhCurrencyType yinlianzhCurrencyType;
    /**
     * 交易状态
     */
    @ManyToOne(optional = true)
    private YinlianzhResponseCode yinlianzhResponseCode;
    /**
     * 交易类型
     */
    @ManyToOne(optional = true)
    private YinlianzhTransactionType yinlianzhTransactionType;
    /**
     * 收
     */
    @Column(precision = 16, scale = 2)
    private BigDecimal income;
    /**
     * 支
     */
    @Column(precision = 16, scale = 2)
    private BigDecimal outcome;
    /**
     * 商户编号识别码
     */
    @Column(length = 64)
    private String merchantDistinguishCode;
    /**
     * 终端号识别码
     */
    @Column(length = 64)
    private String terminalDistinguishCode;
    /**
     * 发卡行所在地
     */
    @Column(length = 64)
    private String cardLocation;
    /**
     * 卡性质
     */
    @ManyToOne(optional = true)
    private YinlianzhCardNature yinlianzhCardNature;

    /**
     * 卡等级
     */
    @ManyToOne(optional = true)
    private YinlianzhCardGrade yinlianzhCardGrade;

    /**
     * 卡产品
     */
    @ManyToOne(optional = true)
    private YinlianzhCardProduct yinlianzhCardProduct;

    private String shortMid;

    private String trueMid;

    /**
     * 该明细是下载解析的还是直接访问解析的
     */
    private boolean isDowLoad;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarDistinguishCode() {
        return this.carDistinguishCode;
    }

    public void setCarDistinguishCode(String carDistinguishCode) {
        this.carDistinguishCode = carDistinguishCode;
    }

    public Date getTransactionDate() {
        return this.transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getAccountDate() {
        return this.AccountDate;
    }

    public void setAccountDate(Date accountDate) {
        this.AccountDate = accountDate;
    }

    public YinlianzhCurrencyType getYinlianzhCurrencyType() {
        return this.yinlianzhCurrencyType;
    }

    public void setYinlianzhCurrencyType(
            YinlianzhCurrencyType yinlianzhCurrencyType) {
        this.yinlianzhCurrencyType = yinlianzhCurrencyType;
    }

    public YinlianzhResponseCode getYinlianzhResponseCode() {
        return this.yinlianzhResponseCode;
    }

    public void setYinlianzhResponseCode(
            YinlianzhResponseCode yinlianzhResponseCode) {
        this.yinlianzhResponseCode = yinlianzhResponseCode;
    }

    public YinlianzhTransactionType getYinlianzhTransactionType() {
        return this.yinlianzhTransactionType;
    }

    public void setYinlianzhTransactionType(
            YinlianzhTransactionType yinlianzhTransactionType) {
        this.yinlianzhTransactionType = yinlianzhTransactionType;
    }

    public BigDecimal getIncome() {
        return this.income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOutcome() {
        return this.outcome;
    }

    public void setOutcome(BigDecimal outcome) {
        this.outcome = outcome;
    }

    public String getMerchantDistinguishCode() {
        return this.merchantDistinguishCode;
    }

    public void setMerchantDistinguishCode(String merchantDistinguishCode) {
        this.merchantDistinguishCode = merchantDistinguishCode;
    }

    public String getTerminalDistinguishCode() {
        return this.terminalDistinguishCode;
    }

    public void setTerminalDistinguishCode(String terminalDistinguishCode) {
        this.terminalDistinguishCode = terminalDistinguishCode;
    }

    public String getCardLocation() {
        return this.cardLocation;
    }

    public void setCardLocation(String cardLocation) {
        this.cardLocation = cardLocation;
    }

    public YinlianzhCardNature getYinlianzhCardNature() {
        return this.yinlianzhCardNature;
    }

    public void setYinlianzhCardNature(YinlianzhCardNature yinlianzhCardNature) {
        this.yinlianzhCardNature = yinlianzhCardNature;
    }

    public Integer getYinlianzhTransactionsInfoId() {
        return this.yinlianzhTransactionsInfoId;
    }

    public void setYinlianzhTransactionsInfoId(
            Integer yinlianzhTransactionsInfoId) {
        this.yinlianzhTransactionsInfoId = yinlianzhTransactionsInfoId;
    }

    public YinlianzhCardGrade getYinlianzhCardGrade() {
        return this.yinlianzhCardGrade;
    }

    public void setYinlianzhCardGrade(YinlianzhCardGrade yinlianzhCardGrade) {
        this.yinlianzhCardGrade = yinlianzhCardGrade;
    }

    public YinlianzhCardProduct getYinlianzhCardProduct() {
        return this.yinlianzhCardProduct;
    }

    public void setYinlianzhCardProduct(
            YinlianzhCardProduct yinlianzhCardProduct) {
        this.yinlianzhCardProduct = yinlianzhCardProduct;
    }

    public String getShortMid() {
        return this.shortMid;
    }

    public void setShortMid(String shortMid) {
        this.shortMid = shortMid;
    }

    public String getTrueMid() {
        return this.trueMid;
    }

    public void setTrueMid(String trueMid) {
        this.trueMid = trueMid;
    }

    public String getTransactionDateString() {
        return this.transactionDateString;
    }

    public void setTransactionDateString(String transactionDateString) {
        this.transactionDateString = transactionDateString;
    }

    public boolean isDowLoad() {
        return this.isDowLoad;
    }

    public void setDowLoad(boolean isDowLoad) {
        this.isDowLoad = isDowLoad;
    }

}
