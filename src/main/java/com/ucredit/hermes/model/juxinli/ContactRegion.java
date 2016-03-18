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

/**
 * 联系人区域汇总
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "contact_region")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactRegion extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -8106260711382858523L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 地区名称
     */
    private String region_loc;
    /**
     * 号码数量
     */
    private float region_uniq_num_cnt;
    /**
     * 电话呼入时间百分比
     */
    private float region_call_in_time_pct;
    /**
     * 平均电话呼入时间
     */
    private float region_avg_call_in_time;
    /**
     * 电话呼入时间
     */
    private float region_call_in_time;
    /**
     * 电话呼出
     */
    private float region_call_out_cnt;
    /**
     * 平均电话呼出时间
     */
    private float region_avg_call_out_time;
    /**
     * 平均电话呼入时间
     */
    private float region_call_in_cnt_pct;
    /**
     * 电话呼出时间
     */
    private float region_call_out_time;
    /**
     * 电话呼入
     */
    private float region_call_in_cnt;
    /**
     * 电话呼出时间百分比
     */
    private float region_call_out_time_pct;
    /**
     * 电话呼出次数百分比
     */
    private float region_call_out_cnt_pct;
    @Column(name = "report_data_id")
    private Integer report_data_id;

    public String getRegion_loc() {
        return this.region_loc;
    }

    public float getRegion_uniq_num_cnt() {
        return this.region_uniq_num_cnt;
    }

    public void setRegion_uniq_num_cnt(float region_uniq_num_cnt) {
        this.region_uniq_num_cnt = region_uniq_num_cnt;
    }

    public float getRegion_call_in_time_pct() {
        return this.region_call_in_time_pct;
    }

    public void setRegion_call_in_time_pct(float region_call_in_time_pct) {
        this.region_call_in_time_pct = region_call_in_time_pct;
    }

    public float getRegion_avg_call_in_time() {
        return this.region_avg_call_in_time;
    }

    public void setRegion_avg_call_in_time(float region_avg_call_in_time) {
        this.region_avg_call_in_time = region_avg_call_in_time;
    }

    public float getRegion_call_in_time() {
        return this.region_call_in_time;
    }

    public void setRegion_call_in_time(float region_call_in_time) {
        this.region_call_in_time = region_call_in_time;
    }

    public float getRegion_call_out_cnt() {
        return this.region_call_out_cnt;
    }

    public void setRegion_call_out_cnt(float region_call_out_cnt) {
        this.region_call_out_cnt = region_call_out_cnt;
    }

    public float getRegion_avg_call_out_time() {
        return this.region_avg_call_out_time;
    }

    public void setRegion_avg_call_out_time(float region_avg_call_out_time) {
        this.region_avg_call_out_time = region_avg_call_out_time;
    }

    public float getRegion_call_in_cnt_pct() {
        return this.region_call_in_cnt_pct;
    }

    public void setRegion_call_in_cnt_pct(float region_call_in_cnt_pct) {
        this.region_call_in_cnt_pct = region_call_in_cnt_pct;
    }

    public float getRegion_call_out_time() {
        return this.region_call_out_time;
    }

    public void setRegion_call_out_time(float region_call_out_time) {
        this.region_call_out_time = region_call_out_time;
    }

    public float getRegion_call_in_cnt() {
        return this.region_call_in_cnt;
    }

    public void setRegion_call_in_cnt(float region_call_in_cnt) {
        this.region_call_in_cnt = region_call_in_cnt;
    }

    public float getRegion_call_out_time_pct() {
        return this.region_call_out_time_pct;
    }

    public void setRegion_call_out_time_pct(float region_call_out_time_pct) {
        this.region_call_out_time_pct = region_call_out_time_pct;
    }

    public float getRegion_call_out_cnt_pct() {
        return this.region_call_out_cnt_pct;
    }

    public void setRegion_call_out_cnt_pct(float region_call_out_cnt_pct) {
        this.region_call_out_cnt_pct = region_call_out_cnt_pct;
    }

    public void setRegion_loc(String region_loc) {
        this.region_loc = region_loc;
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
