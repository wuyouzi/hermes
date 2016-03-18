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
 * 出行分析
 *
 * @author zhouuwyuan
 */
@Entity
@Table(name = "trip_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TripInfo extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 8378867749208170642L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 目的地
     */
    private String trip_dest;
    /**
     * 出发地
     */
    private String trip_leave;
    /**
     * 出行结束时间
     */
    private String trip_end_time;
    /**
     * 出行时间类型
     */
    private String trip_type;
    /**
     * 出行开始时间
     */
    private String trip_start_time;
    @Column(name = "report_data_id")
    private Integer report_data_id;

    public String getTrip_dest() {
        return this.trip_dest;
    }

    public void setTrip_dest(String trip_dest) {
        this.trip_dest = trip_dest;
    }

    public String getTrip_leave() {
        return this.trip_leave;
    }

    public void setTrip_leave(String trip_leave) {
        this.trip_leave = trip_leave;
    }

    public String getTrip_end_time() {
        return this.trip_end_time;
    }

    public void setTrip_end_time(String trip_end_time) {
        this.trip_end_time = trip_end_time;
    }

    public String getTrip_type() {
        return this.trip_type;
    }

    public void setTrip_type(String trip_type) {
        this.trip_type = trip_type;
    }

    public String getTrip_start_time() {
        return this.trip_start_time;
    }

    public void setTrip_start_time(String trip_start_time) {
        this.trip_start_time = trip_start_time;
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
