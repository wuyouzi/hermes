package com.ucredit.hermes.model.juxinli;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

@Entity
@Table(name = "ebusiness_expense")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EbusinessExpense extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -6329660877966512565L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 汇总月份
     */
    private String trans_mth;
    /**
     * 本人购物金额
     */
    private float owner_amount;
    /**
     * 本人购物次数
     */
    private int owner_count;
    /**
     * 本人+同住人购物金额
     */
    private float family_amount;
    /**
     * 本人+同住人购物次数
     */
    private int family_count;
    /**
     * 总购物金额
     */
    private float all_amount;
    /**
     * 总购物次数
     */
    private int all_count;
    @Column(name = "report_data_id")
    private Integer report_data_id;

    public String getTrans_mth() {
        return this.trans_mth;
    }

    public void setTrans_mth(String trans_mth) {
        this.trans_mth = trans_mth;
    }

    public float getOwner_amount() {
        return this.owner_amount;
    }

    public void setOwner_amount(float owner_amount) {
        this.owner_amount = owner_amount;
    }

    public int getOwner_count() {
        return this.owner_count;
    }

    public void setOwner_count(int owner_count) {
        this.owner_count = owner_count;
    }

    public float getFamily_amount() {
        return this.family_amount;
    }

    public void setFamily_amount(float family_amount) {
        this.family_amount = family_amount;
    }

    public int getFamily_count() {
        return this.family_count;
    }

    public void setFamily_count(int family_count) {
        this.family_count = family_count;
    }

    public float getAll_amount() {
        return this.all_amount;
    }

    public void setAll_amount(float all_amount) {
        this.all_amount = all_amount;
    }

    public int getAll_count() {
        return this.all_count;
    }

    public void setAll_count(int all_count) {
        this.all_count = all_count;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "report_data_id")
    public Integer getReport_data_id() {
        return this.report_data_id;
    }

    public void setReport_data_id(Integer report_data_id) {
        this.report_data_id = report_data_id;
    }

}
