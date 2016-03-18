package com.ucredit.hermes.model.miguan;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 基本信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_user_basic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanUserBasic extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 3575766714607532865L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用户出生地:省份
     */
    private String user_province;
    /**
     * 用户出生地:城市
     */
    private String user_city;
    /**
     * 用户出生地:区县
     */
    private String user_region;
    /**
     * 身份证号码
     */
    private String user_idcard;
    /**
     * 年龄
     */
    private int user_age;
    /**
     * 性别
     */
    private String user_gender;
    /**
     * 手机号码
     */
    private String user_phone;
    /**
     * 身份证是否有效
     */
    private boolean user_idcard_valid;
    /**
     * 姓名
     */
    private String user_name;
    /**
     *
     */
    private String user_phone_city;
    /**
     *
     */
    private String user_phone_province;
    /**
     *
     */
    private String user_phone_operator;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_province() {
        return this.user_province;
    }

    public void setUser_province(String user_province) {
        this.user_province = user_province;
    }

    public String getUser_city() {
        return this.user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_region() {
        return this.user_region;
    }

    public void setUser_region(String user_region) {
        this.user_region = user_region;
    }

    public String getUser_idcard() {
        return this.user_idcard;
    }

    public void setUser_idcard(String user_idcard) {
        this.user_idcard = user_idcard;
    }

    public int getUser_age() {
        return this.user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public String getUser_gender() {
        return this.user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_phone() {
        return this.user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public boolean isUser_idcard_valid() {
        return this.user_idcard_valid;
    }

    public void setUser_idcard_valid(boolean user_idcard_valid) {
        this.user_idcard_valid = user_idcard_valid;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone_city() {
        return this.user_phone_city;
    }

    public void setUser_phone_city(String user_phone_city) {
        this.user_phone_city = user_phone_city;
    }

    public String getUser_phone_province() {
        return this.user_phone_province;
    }

    public void setUser_phone_province(String user_phone_province) {
        this.user_phone_province = user_phone_province;
    }

    public String getUser_phone_operator() {
        return this.user_phone_operator;
    }

    public void setUser_phone_operator(String user_phone_operator) {
        this.user_phone_operator = user_phone_operator;
    }

}
