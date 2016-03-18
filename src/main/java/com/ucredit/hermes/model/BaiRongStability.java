package com.ucredit.hermes.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 百融查询结果：稳定性评估
 */
@Entity
@Table(name = "bairong_Stability")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaiRongStability implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6094100146647286416L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dbId;

    @Column(length = 20, nullable = false)
    private Integer baiRongParamsId;

    /**
     * 百融身份证匹配的数量
     */
    private String id_number;

    /**
     * 百融电话号码匹配的数量
     */
    private String cell_number;

    /**
     * 百融电话号码匹配的数量
     */
    private String cell_firstTime;

    /**
     * 百融邮件匹配的数量
     */
    private String mail_number;

    /**
     * 百融姓名匹配的数量
     */
    private String name_number;

    /**
     * 百融电话匹配的数量
     */
    private String tel_number;

    /**
     * 百融地址匹配的数量
     */
    private String addr_number;

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

    public String getId_number() {
        return this.id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getCell_number() {
        return this.cell_number;
    }

    public void setCell_number(String cell_number) {
        this.cell_number = cell_number;
    }

    public String getCell_firstTime() {
        return this.cell_firstTime;
    }

    public void setCell_firstTime(String cell_firstTime) {
        this.cell_firstTime = cell_firstTime;
    }

    public String getMail_number() {
        return this.mail_number;
    }

    public void setMail_number(String mail_number) {
        this.mail_number = mail_number;
    }

    public String getName_number() {
        return this.name_number;
    }

    public void setName_number(String name_number) {
        this.name_number = name_number;
    }

    public String getTel_number() {
        return this.tel_number;
    }

    public void setTel_number(String tel_number) {
        this.tel_number = tel_number;
    }

    public String getAddr_number() {
        return this.addr_number;
    }

    public void setAddr_number(String addr_number) {
        this.addr_number = addr_number;
    }

}
