package com.ucredit.hermes.model.juxinli;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ucredit.hermes.model.pengyuan.BaseModel;

@Entity
@Table(name = "recent_need")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RecentNeed extends BaseModel<Integer> {
    private static final long serialVersionUID = -8248658309774966384L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 需求信息列表
     */
    @OneToMany(targetEntity = DemandsInfo.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "recent_need_id", updatable = false)
    private List<DemandsInfo> demands_info;
    /**
     * 需求发生月
     */
    private String req_mth;
    /**
     * 总主叫次数
     */
    private int call_out_cnt;
    /**
     * 总被叫次数
     */
    private int call_in_cnt;
    /**
     * 总主叫时间
     */
    private int call_out_time;
    /**
     * 总被叫时间
     */
    private int call_in_time;
    /**
     * 需求类型
     */
    private String req_type;
    @Column(name = "report_data_id")
    private Integer report_data_id;

    public List<DemandsInfo> getDemands_info() {
        return this.demands_info;
    }

    public void setDemands_info(List<DemandsInfo> demands_info) {
        this.demands_info = demands_info;
    }

    public String getReq_mth() {
        return this.req_mth;
    }

    public void setReq_mth(String req_mth) {
        this.req_mth = req_mth;
    }

    public String getReq_type() {
        return this.req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getCall_out_cnt() {
        return this.call_out_cnt;
    }

    public void setCall_out_cnt(int call_out_cnt) {
        this.call_out_cnt = call_out_cnt;
    }

    public int getCall_in_cnt() {
        return this.call_in_cnt;
    }

    public void setCall_in_cnt(int call_in_cnt) {
        this.call_in_cnt = call_in_cnt;
    }

    public int getCall_out_time() {
        return this.call_out_time;
    }

    public void setCall_out_time(int call_out_time) {
        this.call_out_time = call_out_time;
    }

    public int getCall_in_time() {
        return this.call_in_time;
    }

    public void setCall_in_time(int call_in_time) {
        this.call_in_time = call_in_time;
    }

    public Integer getReport_data_id() {
        return this.report_data_id;
    }

    public void setReport_data_id(Integer report_data_id) {
        this.report_data_id = report_data_id;
    }

}
