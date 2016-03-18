package com.ucredit.hermes.model.tongdun;

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

import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.pengyuan.BaseModel;

@Entity
@Table(name = "tongdun_fraud_record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TongdunFraudRecord extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 5291775309860271505L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Condition condition;

    /**
     * 查询时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();
    /**
     * 返回时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date backTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date queryTime;
    /**
     * 是否有效
     */
    private boolean enabled = true;
    /**
     * 返回结果的状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ResultType resultType;

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private AsyncCode errorCode;

    private String errorMessage;

    @ManyToOne(optional = true)
    private FraudApiResponse fraudApiResponse;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
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

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ResultType getResultType() {
        return this.resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public FraudApiResponse getFraudApiResponse() {
        return this.fraudApiResponse;
    }

    public void setFraudApiResponse(FraudApiResponse fraudApiResponse) {
        this.fraudApiResponse = fraudApiResponse;
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

    public Date getQueryTime() {
        return this.queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

}
