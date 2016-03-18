/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

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
import com.ucredit.hermes.enums.TableType;

/**
 * 记录表
 *
 * @author caoming
 */
@Entity
@Table(name = "hermes_counts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HermesCount extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -5876717851180500211L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主表id
     */
    private Integer mainId;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();

    /**
     * 子表的记录数
     */
    private Integer count;

    /**
     * 有效性
     */
    private boolean enable = true;

    /**
     * 表类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private TableType type;

    /**
     * 数据来源
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private DataChannel channel;

    public HermesCount() {

    }

    public HermesCount(Integer mainId, Integer count, TableType type,
            DataChannel channel) {
        this.mainId = mainId;
        this.count = count;
        this.type = type;
        this.channel = channel;
    }

    public HermesCount(Integer mainId, Integer count, boolean enable,
            TableType type, DataChannel channel) {
        super();
        this.mainId = mainId;
        this.count = count;
        this.enable = enable;
        this.type = type;
        this.channel = channel;
    }

    public Integer getMainId() {
        return this.mainId;
    }

    public void setMainId(Integer mainId) {
        this.mainId = mainId;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public TableType getType() {
        return this.type;
    }

    public void setType(TableType type) {
        this.type = type;
    }

    public DataChannel getChannel() {
        return this.channel;
    }

    public void setChannel(DataChannel channel) {
        this.channel = channel;
    }

    /*
     * (non-Javadoc)
     * @see com.ucredit.hermes.model.IDOwner#getId()
     */
    @Override
    public Integer getId() {
        // TODO Auto-generated method stub
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
