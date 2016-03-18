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

/**
 * 送货地址列表
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "deliver_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DeliverAddress extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -1131087593360747716L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 收货地址
     */
    private String address;
    /**
     * 经度
     */
    private float lng;
    /**
     * 纬度
     */
    private float lat;
    /**
     * 地址类型
     */
    private String predict_addr_type;
    /**
     * 开始送货时间
     */
    private String begin_date;
    /**
     * 结束送货时间
     */
    private String end_date;
    /**
     * 总送货金额
     */
    private float total_amount;
    /**
     * 总送货次数
     */
    private float total_count;
    /**
     * 收货人列表
     */
    @OneToMany(targetEntity = Receiver.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "deliver_address_id", updatable = false)
    private List<Receiver> receiver;
    @Column(name = "report_data_id")
    private Integer report_data_id;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLng() {
        return this.lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getLat() {
        return this.lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getPredict_addr_type() {
        return this.predict_addr_type;
    }

    public void setPredict_addr_type(String predict_addr_type) {
        this.predict_addr_type = predict_addr_type;
    }

    public String getBegin_date() {
        return this.begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return this.end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public float getTotal_amount() {
        return this.total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    public float getTotal_count() {
        return this.total_count;
    }

    public void setTotal_count(float total_count) {
        this.total_count = total_count;
    }

    public List<Receiver> getReceiver() {
        return this.receiver;
    }

    public void setReceiver(List<Receiver> receiver) {
        this.receiver = receiver;
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
