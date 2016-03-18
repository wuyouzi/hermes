package com.ucredit.hermes.model.yinlianzh;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 商户相关信息
 *
 * @author zhouuwyuan
 */
@Entity
@Table(name = "yinlianzh_transactions_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YinlianzhTransactionsInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -4042533840823345034L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主表 yinlianzh_merchant_bill_base_info
     */
    @Column(name = "yinlianzh_merchant_bill_base_info_id")
    private Integer yinlianzhMerchantBillBaseInfoId;
    /**
     * 商户编号
     */
    @Column(length = 512)
    private String mid;
    /**
     * 返回的加密的商户编号
     */
    @Column(length = 64)
    private String encryptMid;
    /**
     * 返回的缺省的mid编号
     */
    @Column(length = 64)
    private String shortMid;
    /**
     * 准确的商户名称
     */
    @Column(length = 64)
    private String name;
    /**
     * 终端台数
     */
    private Integer posCount;

    /**
     * 所以终端编号列表
     */
    @Lob
    private String posIds;
    /**
     * 所有终端编号列表
     */
    @Lob
    private String mcc;
    /**
     * 商户经营所在城市
     */
    @Column(length = 64)
    private String city;

    /**
     * 详细地址
     */
    private String address;
    /**
     * 收入，单位：分
     */
    private Long receipt;
    /**
     * 支出，单位：分
     */
    private Long expense;
    /**
     * 交易笔数
     */
    private Integer transCount;
    /**
     * 第一个交易日期
     */
    @Temporal(TemporalType.DATE)
    private Date firstTransDate;
    /**
     * 最后一个交易日期
     */
    @Temporal(TemporalType.DATE)
    private Date lastTransDate;
    /**
     * 该商户下的交易明细生成的下载文件的标识符
     */
    private String fileUuid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date backTime = new Date();

    @Transient
    private String backTimeString;
    /**
     * 查询类型，0表示不返回交易流水，1表示返回
     */
    private Integer type;

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 交易明细
     */

    /*
     * @OneToMany(targetEntity = YinlianzhTransactionDetail.class,
     * cascade = CascadeType.ALL)
     * @Fetch(FetchMode.JOIN)
     * @JoinColumn(name = "yinlianzh_transactions_info_id", updatable = false)
     */
    @Transient
    private Set<YinlianzhTransactionDetail> transactionDetails;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYinlianzhMerchantBillBaseInfoId() {
        return this.yinlianzhMerchantBillBaseInfoId;
    }

    public void setYinlianzhMerchantBillBaseInfoId(
            Integer yinlianzhMerchantBillBaseInfoId) {
        this.yinlianzhMerchantBillBaseInfoId = yinlianzhMerchantBillBaseInfoId;
    }

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosCount() {
        return this.posCount;
    }

    public void setPosCount(Integer posCount) {
        this.posCount = posCount;
    }

    public String getPosIds() {
        return this.posIds;
    }

    public void setPosIds(String posIds) {
        this.posIds = posIds;
    }

    public String getMcc() {
        return this.mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getReceipt() {
        return this.receipt;
    }

    public void setReceipt(Long receipt) {
        this.receipt = receipt;
    }

    public Long getExpense() {
        return this.expense;
    }

    public void setExpense(Long expense) {
        this.expense = expense;
    }

    public Integer getTransCount() {
        return this.transCount;
    }

    public void setTransCount(Integer transCount) {
        this.transCount = transCount;
    }

    public Date getFirstTransDate() {
        return this.firstTransDate;
    }

    public void setFirstTransDate(Date firstTransDate) {
        this.firstTransDate = firstTransDate;
    }

    public Date getLastTransDate() {
        return this.lastTransDate;
    }

    public void setLastTransDate(Date lastTransDate) {
        this.lastTransDate = lastTransDate;
    }

    public String getFileUuid() {
        return this.fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public Set<YinlianzhTransactionDetail> getTransactionDetails() {
        return this.transactionDetails;
    }

    public void setTransactionDetails(
            Set<YinlianzhTransactionDetail> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public String getEncryptMid() {
        return this.encryptMid;
    }

    public void setEncryptMid(String encryptMid) {
        this.encryptMid = encryptMid;
    }

    public String getShortMid() {
        return this.shortMid;
    }

    public void setShortMid(String shortMid) {
        this.shortMid = shortMid;
    }

    public Date getBackTime() {
        return this.backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public String getBackTimeString() {
        return this.backTimeString;
    }

    public void setBackTimeString(String backTimeString) {
        this.backTimeString = backTimeString;
    }

}
