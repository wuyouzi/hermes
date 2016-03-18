package com.ucredit.hermes.model.juxinli;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * app授权表
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "grant_authorization_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GrantAuthorizationInfo extends BaseModel<Integer> {
    private static final long serialVersionUID = 6978090424529190511L;

    public GrantAuthorizationInfo() {
        super();
    }

    public GrantAuthorizationInfo(String name, String idcard, String phone) {
        this.name = name;
        this.idcard = idcard;
        this.phone = phone;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 申请人的姓名
     */
    @Column(length = 64)
    @Index(name = "idx_grant_authorization_infos_name")
    private String name;

    /**
     * 申请人的身份证号码
     */
    @Column(length = 32)
    @Index(name = "idx_grant_authorization_infos_idcard")
    private String idcard;

    /**
     * 申请人的联系电话号码
     */
    @Column(length = 32)
    @Index(name = "idx_grant_authorization_infos_phone")
    private String phone;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();
    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    private String appId;
    /**
     * 是否有效
     */
    private boolean enabled = true;
    @OneToMany(targetEntity = ReportData.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "grant_authorization_id", updatable = false)
    private List<ReportData> reportDatas;

    private ResultType resultType;

    private String keyid;

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

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<ReportData> getReportDatas() {
        return this.reportDatas;
    }

    public void setReportDatas(List<ReportData> reportDatas) {
        this.reportDatas = reportDatas;
    }

    public ResultType getResultType() {
        return this.resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getKeyid() {
        return this.keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

}
