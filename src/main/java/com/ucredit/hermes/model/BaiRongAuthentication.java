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

@Entity
@Table(name = "bairong_Authentication")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaiRongAuthentication implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6103949683575059L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dbId;

    @Column(length = 20, nullable = false)
    private Integer baiRongParamsId;

    /**
     * 身份证有效命中
     */
    @Column(length = 2, nullable = false)
    private String id;

    /**
     * 手机号有效命中
     */
    @Column(length = 2, nullable = false)
    private String cell;

    /**
     * 邮件有效命中
     */
    @Column(length = 2, nullable = false)
    private String mail;

    /**
     * 姓名有效命中
     */
    @Column(length = 2, nullable = false)
    private String name;

    /**
     * 公司电话有效命中
     */
    @Column(length = 2, nullable = false)
    private String tel_biz;

    /**
     * 家庭电话有效命中
     */
    @Column(length = 2, nullable = false)
    private String tel_home;

    /**
     * 由三位01字符串组成，第一个字符表示id和cell之间的关联关系，1为有关联，0为没有关联；第二个字符表示匹配上的id和mail的关系，取值同上；
     * 第三个字符串表示匹配上的cell和mail之间的关系
     */
    @Column(length = 5, nullable = false)
    private String key_relation;

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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCell() {
        return this.cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey_relation() {
        return this.key_relation;
    }

    public void setKey_relation(String key_relation) {
        this.key_relation = key_relation;
    }

    public String getTel_biz() {
        return this.tel_biz;
    }

    public void setTel_biz(String tel_biz) {
        this.tel_biz = tel_biz;
    }

    public String getTel_home() {
        return this.tel_home;
    }

    public void setTel_home(String tel_home) {
        this.tel_home = tel_home;
    }

}
