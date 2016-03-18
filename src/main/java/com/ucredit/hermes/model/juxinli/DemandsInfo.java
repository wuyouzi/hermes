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
@Table(name = "demands_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandsInfo extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 6792391812689047557L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 被叫时间
     */
    private Integer demands_call_in_time;
    /**
     * demands_call_out_cnt
     */
    private Integer demands_call_out_cnt;
    /**
     * 主叫时间
     */
    private Integer demands_call_out_time;
    /**
     * 被叫次数
     */
    private Integer demands_call_in_cnt;
    /**
     * 需求名称
     */
    private String demands_name;
    /**
     *
     */
    @Column(name = "recent_need_id")
    private Integer recent_need_id;

    public Integer getDemands_call_in_time() {
        return this.demands_call_in_time;
    }

    public void setDemands_call_in_time(Integer demands_call_in_time) {
        this.demands_call_in_time = demands_call_in_time;
    }

    public Integer getDemands_call_out_cnt() {
        return this.demands_call_out_cnt;
    }

    public void setDemands_call_out_cnt(Integer demands_call_out_cnt) {
        this.demands_call_out_cnt = demands_call_out_cnt;
    }

    public Integer getDemands_call_out_time() {
        return this.demands_call_out_time;
    }

    public void setDemands_call_out_time(Integer demands_call_out_time) {
        this.demands_call_out_time = demands_call_out_time;
    }

    public Integer getDemands_call_in_cnt() {
        return this.demands_call_in_cnt;
    }

    public void setDemands_call_in_cnt(Integer demands_call_in_cnt) {
        this.demands_call_in_cnt = demands_call_in_cnt;
    }

    public String getDemands_name() {
        return this.demands_name;
    }

    public void setDemands_name(String demands_name) {
        this.demands_name = demands_name;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecent_need_id() {
        return this.recent_need_id;
    }

    public void setRecent_need_id(Integer recent_need_id) {
        this.recent_need_id = recent_need_id;
    }

}
