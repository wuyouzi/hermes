package com.ucredit.hermes.model.miguan;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 蜜罐返回主信息以及hermes系统记录的主信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_reponse_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanResponse extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -6062730851094837846L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 反馈结果备注
     */
    private String note;
    /**
     * 反馈结果是否正确
     */
    private String success;
    /**
     *
     */

    //下面的信息都是hermes自己添加的字段
    /**
     * 查询姓名
     */
    private String name;
    /**
     * 查询身份证
     */
    private String idcard;
    /**
     * 查询电话
     */
    private String phone;

    /**
     * 是否有效
     */
    private boolean enabled = true;

    /**
     * 错误码
     */
    private AsyncCode errorCode;

    /**
     * 错误信息
     */
    @Lob
    private String errorMessage;
    /**
     * 返回结果的状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ResultType resultType;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();

    /**
     * 返回结果时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date backTime;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "grid_info_id")
    private MiguanGridInfo grid_info;

    private String apply_id;

    private String contactType;

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSuccess() {
        return this.success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AsyncCode getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(AsyncCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBackTime() {
        return this.backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
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

    public ResultType getResultType() {
        return this.resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public MiguanGridInfo getGrid_info() {
        return this.grid_info;
    }

    public void setGrid_info(MiguanGridInfo grid_info) {
        this.grid_info = grid_info;
    }

    public String getApply_id() {
        return this.apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public String getContactType() {
        return this.contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

}