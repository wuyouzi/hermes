package com.ucredit.hermes.model.miguan;

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
 * 用这个身份证绑定的其他手机号码
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_idCard_with_other_phones")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanIdCardWithOtherPhones extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -5621228710573837323L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 该号码所属省份
     */
    private String susp_phone_province;
    /**
     * 该号码所属运营商
     */
    private String susp_phone_operator;
    /**
     * 该号码最后一次绑定时间
     */

    private String susp_updt;
    /**
     * 存疑手机号码（可能有多个号码）
     */
    private String susp_phone;
    /**
     * 该号码所在地区
     */
    private String susp_phone_city;

    @Column(name = "card_suspicion_id")
    private Integer card_suspicion_id;

    public String getSusp_phone_province() {
        return this.susp_phone_province;
    }

    public void setSusp_phone_province(String susp_phone_province) {
        this.susp_phone_province = susp_phone_province;
    }

    public String getSusp_phone_operator() {
        return this.susp_phone_operator;
    }

    public void setSusp_phone_operator(String susp_phone_operator) {
        this.susp_phone_operator = susp_phone_operator;
    }

    public String getSusp_updt() {
        return this.susp_updt;
    }

    public void setSusp_updt(String susp_updt) {
        this.susp_updt = susp_updt;
    }

    public String getSusp_phone() {
        return this.susp_phone;
    }

    public void setSusp_phone(String susp_phone) {
        this.susp_phone = susp_phone;
    }

    public String getSusp_phone_city() {
        return this.susp_phone_city;
    }

    public void setSusp_phone_city(String susp_phone_city) {
        this.susp_phone_city = susp_phone_city;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCard_suspicion_id() {
        return this.card_suspicion_id;
    }

    public void setCard_suspicion_id(Integer card_suspicion_id) {
        this.card_suspicion_id = card_suspicion_id;
    }

}
