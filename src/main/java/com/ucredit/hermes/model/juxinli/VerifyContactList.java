package com.ucredit.hermes.model.juxinli;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 验真联系人
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "verify_contact_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VerifyContactList extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -5873694841660009258L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 申请人姓名
     */

    private String name;
    /**
     * 申请人身份证号
     */
    private String idcard;
    /**
     * 申请人电话号码
     */
    private String phone;
    /**
     * 住宅电话
     */
    private String home_tel;
    /**
     * 住宅的地址
     */
    private String home_addr;
    /**
     * 公司电话
     */
    private String work_tel;
    /**
     * 配偶电话
     */
    private String couple_phone_num;
    /**
     * 联系人数组
     */
    @Lob
    private String contact_list;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date queryTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date backTime;
    @Lob
    private String errorMessage;
    private AsyncCode errorCode;
    private boolean enabled;

    @Column(name = "grant_authorization_id")
    private Integer grant_authorization_id;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return this.idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHome_tel() {
        return this.home_tel;
    }

    public void setHome_tel(String home_tel) {
        this.home_tel = home_tel;
    }

    public String getHome_addr() {
        return this.home_addr;
    }

    public void setHome_addr(String home_addr) {
        this.home_addr = home_addr;
    }

    public String getWork_tel() {
        return this.work_tel;
    }

    public void setWork_tel(String work_tel) {
        this.work_tel = work_tel;
    }

    public String getCouple_phone_num() {
        return this.couple_phone_num;
    }

    public void setCouple_phone_num(String couple_phone_num) {
        this.couple_phone_num = couple_phone_num;
    }

    public String getContact_list() {
        return this.contact_list;
    }

    public void setContact_list(String contact_list) {
        this.contact_list = contact_list;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getQueryTime() {
        return this.queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public Date getBackTime() {
        return this.backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public AsyncCode getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(AsyncCode errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getGrant_authorization_id() {
        return this.grant_authorization_id;
    }

    public void setGrant_authorization_id(Integer grant_authorization_id) {
        this.grant_authorization_id = grant_authorization_id;
    }

}
