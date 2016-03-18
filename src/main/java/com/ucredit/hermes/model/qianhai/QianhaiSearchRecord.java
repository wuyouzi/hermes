package com.ucredit.hermes.model.qianhai;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * @author zhouwuyuan
 */
@Entity
@Table(name = "qianhai_search_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QianhaiSearchRecord extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 3553615999129859241L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 查询姓名
     */
    private String name;
    /**
     * 证件号码
     */
    private String idNo;
    /**
     * 证件类型
     */
    private String idType;

    private String apply_id;

    private String transDate;
    @Lob
    private String rtMsg;
    private String rtCode;
    private String batchNo;
    @Lob
    private String signatureValue;

    private String oriMessage;
    /**
     * 是否有效
     */
    private boolean enabled = true;

    /**
     * 错误码
     */
    private AsyncCode errorCode;

    /**
     * 错误信息
     */
    @Lob
    private String errorMessage;
    /**
     * 返回结果的状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ResultType resultType;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();

    /**
     * 返回结果时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date backTime;

    @OneToMany(targetEntity = QianhaiRecords.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "search_record_id", updatable = false)
    private List<QianhaiRecords> records;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return this.idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdType() {
        return this.idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AsyncCode getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(AsyncCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResultType getResultType() {
        return this.resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBackTime() {
        return this.backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public String getApply_id() {
        return this.apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransDate() {
        return this.transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getRtMsg() {
        return this.rtMsg;
    }

    public void setRtMsg(String rtMsg) {
        this.rtMsg = rtMsg;
    }

    public String getRtCode() {
        return this.rtCode;
    }

    public void setRtCode(String rtCode) {
        this.rtCode = rtCode;
    }

    public String getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSignatureValue() {
        return this.signatureValue;
    }

    public void setSignatureValue(String signatureValue) {
        this.signatureValue = signatureValue;
    }

    public List<QianhaiRecords> getRecords() {
        return this.records;
    }

    public void setRecords(List<QianhaiRecords> records) {
        this.records = records;
    }

    public String getOriMessage() {
        return this.oriMessage;
    }

    public void setOriMessage(String oriMessage) {
        this.oriMessage = oriMessage;
    }

}
