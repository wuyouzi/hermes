package com.ucredit.hermes.model.blacklist;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * @author LiuHao
 */
@Entity
@Table(name = "black_lists")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BlackList extends BaseModel<Integer> {
    private static final long serialVersionUID = -8402662370984891422L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 值
     */
    @Column(length = 64, nullable = false)
    @Length(max = 64)
    @NotEmpty
    private String value;
    /*
     * 类型
     */
    @Column(length = 32, nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private BlackListValueType type;
    /**
     * 创建时间
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();
    /*
     * 数据渠道
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataChannel dataChannel;
    /*
     * 备注
     */
    @Column(length = 300)
    private String comment;
    /**
     * 关联的进件ID
     */
    @Column(length = 64)
    private String lendRequestId;
    /**
     * 是否有效
     * false 无效 true 有效
     */
    private boolean enabled = true;
    /**
     * 安盛黑名单获取时间（如果是友信的黑名单则是此进件被拒贷的终定时间）
     */
    private Date effectTime;

    /*
     * get/set方法
     */
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BlackListValueType getType() {
        return this.type;
    }

    public void setType(BlackListValueType type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public DataChannel getDataChannel() {
        return this.dataChannel;
    }

    public void setDataChannel(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLendRequestId() {
        return this.lendRequestId;
    }

    public void setLendRequestId(String lendRequestId) {
        this.lendRequestId = lendRequestId;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static long getSerialversionuid() {
        return BlackList.serialVersionUID;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getEffectTime() {
        return this.effectTime;
    }

    public void setEffectTime(Date effectTime) {
        this.effectTime = effectTime;
    }

}