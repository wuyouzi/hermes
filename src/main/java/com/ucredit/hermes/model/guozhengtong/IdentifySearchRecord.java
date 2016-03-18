package com.ucredit.hermes.model.guozhengtong;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.model.pengyuan.BaseModel;

@Entity
@Table(name = "identify_search_records")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IdentifySearchRecord extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 8073343090859297115L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 外键id
     */
    private int identify_id;

    /**
     * 本次查询返回信息来源
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private DataChannel dataChannel;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();

    private String errorMessage;

    private String errorCode;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdentify_id() {
        return this.identify_id;
    }

    public void setIdentify_id(int identify_id) {
        this.identify_id = identify_id;
    }

    public DataChannel getDataChannel() {
        return this.dataChannel;
    }

    public void setDataChannel(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
