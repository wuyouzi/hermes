package com.ucredit.hermes.model.tongdun;

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
 * 用户传入的地理位置信息，若用户传入resp_detail_type字段包含geoip，则返回结果包含该信息，否则无
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "tongdun_geoip_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GeoipInfo extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -220074923814256053L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * IP地址所处城市
     */
    private String city;
    /**
     * IP地址所处国家
     */
    private String country;
    /**
     * IP地址所处县
     */
    private String county;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 互联网服务提供商，此处为电信
     */
    private String isp;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * long 型ip
     */
    private long lip;
    /**
     * 经度
     */
    private long longitude;
    /**
     * 所在省份
     */
    private String province;

    @Column(name = "fraud_record_id")
    private Integer fraud_record_id;

    @Column(name = "device_info_id")
    private Integer device_info_id;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIsp() {
        return this.isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public long getLip() {
        return this.lip;
    }

    public void setLip(long lip) {
        this.lip = lip;
    }

    public long getLongitude() {
        return this.longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getFraud_record_id() {
        return this.fraud_record_id;
    }

    public void setFraud_record_id(Integer fraud_record_id) {
        this.fraud_record_id = fraud_record_id;
    }

    public Integer getDevice_info_id() {
        return this.device_info_id;
    }

    public void setDevice_info_id(Integer device_info_id) {
        this.device_info_id = device_info_id;
    }

}
