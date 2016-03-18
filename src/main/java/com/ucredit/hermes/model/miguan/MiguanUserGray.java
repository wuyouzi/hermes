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
 * 用户灰度信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_user_gray")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanUserGray extends BaseModel<Integer> {
    private static final long serialVersionUID = 7861095584246298066L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 灰度手机号码
     */
    private String user_phone;
    /**
     * 直接联系人在黑名单的数量
     */
    private int contacts_class1_blacklist_cnt;
    /**
     * 间接联系人在黑名单的数量
     */
    private int contacts_class2_blacklist_cnt;
    /**
     * 手机号码灰度分数（可能的值为null ,0~100,参考分数为60分以上）
     */
    private double phone_gray_score;
    /**
     * 一阶联系人总数
     */
    private int contacts_class1_cnt;
    /**
     * 引起二阶黑名单人数
     */
    private int contacts_router_cnt;
    /**
     * 引起占比=引起二阶黑名单人数/一阶联系人总数
     */
    private double contacts_router_ratio;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_phone() {
        return this.user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getContacts_class1_blacklist_cnt() {
        return this.contacts_class1_blacklist_cnt;
    }

    public void setContacts_class1_blacklist_cnt(
            int contacts_class1_blacklist_cnt) {
        this.contacts_class1_blacklist_cnt = contacts_class1_blacklist_cnt;
    }

    public int getContacts_class2_blacklist_cnt() {
        return this.contacts_class2_blacklist_cnt;
    }

    public void setContacts_class2_blacklist_cnt(
            int contacts_class2_blacklist_cnt) {
        this.contacts_class2_blacklist_cnt = contacts_class2_blacklist_cnt;
    }

    public double getPhone_gray_score() {
        return this.phone_gray_score;
    }

    public void setPhone_gray_score(double phone_gray_score) {
        this.phone_gray_score = phone_gray_score;
    }

    public int getContacts_class1_cnt() {
        return this.contacts_class1_cnt;
    }

    public void setContacts_class1_cnt(int contacts_class1_cnt) {
        this.contacts_class1_cnt = contacts_class1_cnt;
    }

    public int getContacts_router_cnt() {
        return this.contacts_router_cnt;
    }

    public void setContacts_router_cnt(int contacts_router_cnt) {
        this.contacts_router_cnt = contacts_router_cnt;
    }

    public double getContacts_router_ratio() {
        return this.contacts_router_ratio;
    }

    public void setContacts_router_ratio(double contacts_router_ratio) {
        this.contacts_router_ratio = contacts_router_ratio;
    }

}
