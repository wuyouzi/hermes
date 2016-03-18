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
 * 用户传入的身份证和手机号，若用户传入resp_detail_type字段包含attribution，则返回结果包含该信息，否则无
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "tongdun_attribution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Attribution extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -6245938267351932474L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 身份证所属省
     */
    @Column(length = 32)
    private String idCardProvince;
    /**
     * 身份证所属市
     */
    @Column(length = 32)
    private String idCardCity;
    /**
     * 身份证所属区
     */
    @Column(length = 128)
    private String county;
    /**
     * 手机号所属省
     */
    @Column(length = 32)
    private String mobileAddressProvince;
    /**
     * 手机号所属市
     */
    @Column(length = 32)
    private String mobileAddressCity;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdCardProvince() {
        return this.idCardProvince;
    }

    public void setIdCardProvince(String idCardProvince) {
        this.idCardProvince = idCardProvince;
    }

    public String getIdCardCity() {
        return this.idCardCity;
    }

    public void setIdCardCity(String idCardCity) {
        this.idCardCity = idCardCity;
    }

    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getMobileAddressProvince() {
        return this.mobileAddressProvince;
    }

    public void setMobileAddressProvince(String mobileAddressProvince) {
        this.mobileAddressProvince = mobileAddressProvince;
    }

    public String getMobileAddressCity() {
        return this.mobileAddressCity;
    }

    public void setMobileAddressCity(String mobileAddressCity) {
        this.mobileAddressCity = mobileAddressCity;
    }

}
