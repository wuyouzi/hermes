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
 * 阅读兴趣评估
 */
@Entity
@Table(name = "bairong_Media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaiRongMedia implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3501456967584926747L;

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
    @Column(length = 32, nullable = false)
    private String category;
    /**
     * 当前类目下的总浏览天数
     */
    @Column(length = 32, nullable = false)
    private String visitdays;

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

    public String getVisitdays() {
        return this.visitdays;
    }

    public void setVisitdays(String visitdays) {
        this.visitdays = visitdays;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
