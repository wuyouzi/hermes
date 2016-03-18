package com.ucredit.hermes.model.yinlianzh;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 查询信息记录
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "yinlianzh_search_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YinlianzhSearchInfo extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 7921170746226333500L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String account;
    private Integer type;
    private String mid;
    /**
     * 商户名称
     */
    private String mName;
    private String posId;
    private String businessCode;
    /**
     * 商户负责人姓名
     */
    private String name;
    private String mobile;
    private String cid;
    private String beginDate;
    private String endDate;
    private String sign;

    private String category;
    @Lob
    private String backData;

    private String backCode;

    private String backMsg;

    private String uuid;

    private String lendRequestId;

    /**
     * 操作用户
     */
    private String username;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBackCode() {
        return this.backCode;
    }

    public void setBackCode(String backCode) {
        this.backCode = backCode;
    }

    public String getBackMsg() {
        return this.backMsg;
    }

    public void setBackMsg(String backMsg) {
        this.backMsg = backMsg;
    }

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
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

    public String getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBackData() {
        return this.backData;
    }

    public void setBackData(String backData) {
        this.backData = backData;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLendRequestId() {
        return this.lendRequestId;
    }

    public void setLendRequestId(String lendRequestId) {
        this.lendRequestId = lendRequestId;
    }

}
