package com.ucredit.hermes.model.yinlianzh;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 查询商户账单返回的基本信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "yinlianzh_merchant_bill_base_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YinlianzhMerchantBillBaseInfo extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 8357671803507589550L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 提交码
     */
    @Column(length = 32)
    private String resCode;

    /**
     * 提交码的中文描述
     */
    @Column(length = 64)
    private String resMsg;

    /**
     * 本商户(MID汇总)总收入，单位：分
     */
    private Long totalReceipt;
    /**
     * 本商户(MID汇总)总支出，单位：分
     */
    private Long totalExpence;
    /**
     * 本商户(MID汇总)总的交易笔数
     */
    private Integer totalCount;
    /**
     * 本商户下的交易明细汇总生成的下载文件的标识符
     */

    private String fileUuid;

    @Column(length = 1024)
    private String fileUuids;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();

    /**
     * 查询时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date queryTime;
    /**
     * 返回时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date backTime;

    @Transient
    private String createTimeString;
    /**
     * 商户信息列表
     */
    /*
     * @OneToMany(targetEntity = YinlianzhTransactionsInfo.class,
     * cascade = CascadeType.ALL)
     * @Fetch(FetchMode.JOIN)
     * @JoinColumn(name = "yinlianzh_merchant_bill_base_info_id",
     * updatable = false)
     */
    @Transient
    private Set<YinlianzhTransactionsInfo> transactionsInfos;

    /**
     * 数字签名
     */
    private String sign;

    /**
     * 是否有效
     */
    private boolean enabled = true;

    /**
     * 是否返回账单明细0：不返回，1：返回
     */
    private Integer type;

    /**
     * 查询开始时间
     */
    @Temporal(TemporalType.DATE)
    private Date beginDate;

    /**
     * 查询结束时间
     */
    @Temporal(TemporalType.DATE)
    private Date endDate;

    /**
     * 多个查询时的类型
     */
    private String category;

    /**
     * 是否已下载过
     */
    private boolean ifDowload;

    private String lendRequestId;

    private String mName;

    private String posId;

    private String businessCode;

    private String name;

    private String mobile;

    private String cid;

    private boolean ifLast;

    private int searchTotal;

    private String searchMid;

    private String username;

    public boolean isIfDowload() {
        return this.ifDowload;
    }

    /**
     * 当分批查询时，记录查询的次数
     */
    private int searchNum;

    public void setIfDowload(boolean ifDowload) {
        this.ifDowload = ifDowload;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getResCode() {
        return this.resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return this.resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public Long getTotalReceipt() {
        return this.totalReceipt;
    }

    public void setTotalReceipt(Long totalReceipt) {
        this.totalReceipt = totalReceipt;
    }

    public Long getTotalExpence() {
        return this.totalExpence;
    }

    public void setTotalExpence(Long totalExpence) {
        this.totalExpence = totalExpence;
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getFileUuid() {
        return this.fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public Set<YinlianzhTransactionsInfo> getTransactionsInfos() {
        return this.transactionsInfos;
    }

    public void setTransactionsInfos(
            Set<YinlianzhTransactionsInfo> transactionsInfos) {
        this.transactionsInfos = transactionsInfos;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLendRequestId() {
        return this.lendRequestId;
    }

    public void setLendRequestId(String lendRequestId) {
        this.lendRequestId = lendRequestId;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeString() {
        return this.createTimeString;
    }

    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
    }

    public Date getQueryTime() {
        return this.queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public Date getBackTime() {
        return this.backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public int getSearchNum() {
        return this.searchNum;
    }

    public void setSearchNum(int searchNum) {
        this.searchNum = searchNum;
    }

    public String getmName() {
        return this.mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getPosId() {
        return this.posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getBusinessCode() {
        return this.businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFileUuids() {
        return this.fileUuids;
    }

    public void setFileUuids(String fileUuids) {
        this.fileUuids = fileUuids;
    }

    public boolean isIfLast() {
        return this.ifLast;
    }

    public void setIfLast(boolean ifLast) {
        this.ifLast = ifLast;
    }

    public int getSearchTotal() {
        return this.searchTotal;
    }

    public void setSearchTotal(int searchTotal) {
        this.searchTotal = searchTotal;
    }

    public String getSearchMid() {
        return this.searchMid;
    }

    public void setSearchMid(String searchMid) {
        this.searchMid = searchMid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
