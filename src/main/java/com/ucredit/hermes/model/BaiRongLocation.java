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

import com.ucredit.hermes.enums.BaiRongLocationTypes;

/**
 * 位置信息核查
 */
@Entity
@Table(name = "bairong_locations")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaiRongLocation implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4914338860676767307L;

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
    private BaiRongLocationTypes type;

    /**
     * 参数key
     */
    @Column(length = 32, nullable = false)
    private String key;

    /**
     * 参数值
     */
    @Column(length = 32, nullable = false)
    private String value;

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

    public BaiRongLocationTypes getType() {
        return this.type;
    }

    public void setType(BaiRongLocationTypes type) {
        this.type = type;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
