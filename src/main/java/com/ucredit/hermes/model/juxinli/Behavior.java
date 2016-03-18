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
@Table(name = "cell_behavior")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Behavior extends BaseModel<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     *
     */
    private static final long serialVersionUID = 7020732795005513163L;
    /**
     * 月流量
     */
    private Integer sms_cnt;
    /**
     * 手机号码
     */
    private String cell_phone_num;
    /**
     * 月流量
     */
    private float net_flow;
    /**
     *
     */
    private String total_amount;
    /**
     * 月主叫通话时间
     */
    private float call_out_time;
    /**
     * 月份
     */
    private String cell_mth;
    /**
     * 手机归属地
     */
    private String cell_loc;
    /**
     * 手机运营商
     */
    private String cell_operator;
    /**
     * 月被叫通话时间
     */
    private float call_in_time;
    @Column(name = "report_data_id")
    private Integer report_data_id;

    public Integer getSms_cnt() {
        return this.sms_cnt;
    }

    public void setSms_cnt(Integer sms_cnt) {
        this.sms_cnt = sms_cnt;
    }

    public String getCell_phone_num() {
        return this.cell_phone_num;
    }

    public void setCell_phone_num(String cell_phone_num) {
        this.cell_phone_num = cell_phone_num;
    }

    public float getNet_flow() {
        return this.net_flow;
    }

    public void setNet_flow(float net_flow) {
        this.net_flow = net_flow;
    }

    public String getTotal_amount() {
        return this.total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public float getCall_out_time() {
        return this.call_out_time;
    }

    public void setCall_out_time(float call_out_time) {
        this.call_out_time = call_out_time;
    }

    public String getCell_mth() {
        return this.cell_mth;
    }

    public void setCell_mth(String cell_mth) {
        this.cell_mth = cell_mth;
    }

    public String getCell_loc() {
        return this.cell_loc;
    }

    public void setCell_loc(String cell_loc) {
        this.cell_loc = cell_loc;
    }

    public String getCell_operator() {
        return this.cell_operator;
    }

    public void setCell_operator(String cell_operator) {
        this.cell_operator = cell_operator;
    }

    public float getCall_in_time() {
        return this.call_in_time;
    }

    public void setCall_in_time(float call_in_time) {
        this.call_in_time = call_in_time;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReport_data_id() {
        return this.report_data_id;
    }

    public void setReport_data_id(Integer report_data_id) {
        this.report_data_id = report_data_id;
    }

}
