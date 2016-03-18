package com.ucredit.hermes.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.enums.BaiRongConsumptionTypes;

/**
 * 百融商品消费评估报告
 */
@Entity
@Table(name = "bairong_Consumption")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaiRongConsumption implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1562764478481847972L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dbId;

    @Column(length = 20, nullable = false)
    private Integer baiRongParamsId;

    /**
     * 类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private BaiRongConsumptionTypes type;

    /**
     * 类目
     */
    @Column(length = 128, nullable = false)
    private String category;

    /**
     * 浏览次数
     */
    @Column(nullable = true)
    private String visits;

    /**
     * 消费次数
     */
    @Column(nullable = true)
    private String number;

    /*
     * 当前类目下的总消费金额
     */
    @Column(nullable = true)
    private String pay;

    /*
     * 当前类目下月消费总金额的最大值（最近12个月）
     */
    @Column(nullable = true)
    private String maxpay;

    @Column(nullable = true)
    private String level_value;

    public Integer getDbId() {
        return this.dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public Integer getBaiRongParamsId() {
        return this.baiRongParamsId;
    }

    public void setBaiRongParamsId(Integer baiRongParamsId) {
        this.baiRongParamsId = baiRongParamsId;
    }

    public BaiRongConsumptionTypes getType() {
        return this.type;
    }

    public void setType(BaiRongConsumptionTypes type) {
        this.type = type;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVisits() {
        return this.visits;
    }

    public void setVisits(String visits) {
        this.visits = visits;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPay() {
        return this.pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getLevel_value() {
        return this.level_value;
    }

    public void setLevel_value(String level_value) {
        this.level_value = level_value;
    }

    public String getMaxpay() {
        return this.maxpay;
    }

    public void setMaxpay(String maxpay) {
        this.maxpay = maxpay;
    }

}
