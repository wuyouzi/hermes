package com.ucredit.hermes.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "bairong_Data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaiRongData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dbId;

    @Column(length = 20, nullable = false)
    private Integer baiRongParamsId;

    @Lob
    @Column(name = "data", nullable = true)
    private String data;

    @Lob
    @Column(name = "searchString", nullable = true)
    private String searchString;

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

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSearchString() {
        return this.searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

}
