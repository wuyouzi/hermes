/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.guozhengtong;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.Gender;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 身份信息表
 *
 * @author liuqianqian
 */
@Entity
@Table(name = "identity_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IdentityInfo extends BaseModel<Integer> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3672612874838289524L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 姓名
     */

    @Column(length = 32)
    @Index(name = "idx_identity_infos_name")
    private String name;

    /**
     * 身份证号
     */

    @Column(length = 18, nullable = false)
    @Index(name = "idx_identity_infos_identitycard")
    private String identitycard;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private Gender sex;

    /**
     * 出生日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    /**
     * 住址
     */

    @Column(length = 64)
    private String address;

    /**
     * 照片
     */
    @Lob
    private String photo;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();

    /**
     * 发起查询时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date queryTime = new Date();

    /**
     * 查询返回时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date backTime = new Date();

    /**
     * 数据源渠道
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private DataChannel dataChannel;

    /**
     * md5值
     */

    @Column(length = 64)
    private String md5;

    /**
     * 返回结果的状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ResultType resultType;

    /**
     * 当前数据是否有效
     */
    private boolean enabled = true;

    /**
     * 返回错误信息
     */
    @Column(length = 128)
    private String errorMessage;
    @Column(length = 64)
    private String keyid;

    private String errorCode;

    private String messageStatus1;

    private String messageInfo1;

    private String messageStatus2;

    private String messageInfo2;

    private String compStatus;

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the resultType
     */
    public ResultType getResultType() {
        return this.resultType;
    }

    /**
     * @param resultType
     *        the resultType to set
     */
    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    /**
     * @return the id
     */
    @Override
    public Integer getId() {
        return this.id;
    }

    /**
     * @param id
     *        the id to set
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *        the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the identitycard
     */
    public String getIdentitycard() {
        return this.identitycard;
    }

    /**
     * @param identitycard
     *        the identitycard to set
     */
    public void setIdentitycard(String identitycard) {
        this.identitycard = identitycard;
    }

    /**
     * @return the sex
     */
    public Gender getSex() {
        return this.sex;
    }

    /**
     * @param sex
     *        the sex to set
     */
    public void setSex(Gender sex) {
        this.sex = sex;
    }

    /**
     * @return String
     */
    @Transient
    public String getSexString() {
        return this.sex == null ? "" : this.sex.toString();
    }

    /**
     * @return the birthday
     */
    public Date getBirthday() {
        return this.birthday;
    }

    /**
     * @param birthday
     *        the birthday to set
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * @param address
     *        the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the photo
     */
    public String getPhoto() {
        return this.photo;
    }

    /**
     * @param photo
     *        the photo to set
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * @param createTime
     *        the createTime to set
     */
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

    /**
     * @return the dataChannel
     */
    public DataChannel getDataChannel() {
        return this.dataChannel;
    }

    /**
     * @param dataChannel
     *        the dataChannel to set
     */
    public void setDataChannel(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
    }

    /**
     * @return the md5
     */
    public String getMd5() {
        return this.md5;
    }

    /**
     * @param md5
     *        the md5 to set
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getKeyid() {
        return this.keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessageStatus1() {
        return this.messageStatus1;
    }

    public void setMessageStatus1(String messageStatus1) {
        this.messageStatus1 = messageStatus1;
    }

    public String getMessageStatus2() {
        return this.messageStatus2;
    }

    public void setMessageStatus2(String messageStatus2) {
        this.messageStatus2 = messageStatus2;
    }

    public String getCompStatus() {
        return this.compStatus;
    }

    public void setCompStatus(String compStatus) {
        this.compStatus = compStatus;
    }

    public String getMessageInfo1() {
        return this.messageInfo1;
    }

    public void setMessageInfo1(String messageInfo1) {
        this.messageInfo1 = messageInfo1;
    }

    public String getMessageInfo2() {
        return this.messageInfo2;
    }

    public void setMessageInfo2(String messageInfo2) {
        this.messageInfo2 = messageInfo2;
    }

    @Override
    public String toString() {
        return "IdentityInfo [name=" + this.name + ", identitycard="
            + this.identitycard + ", sex=" + this.sex + ", birthday="
            + this.birthday + ", address=" + this.address + ", photo="
            + this.photo + "]";
    }

}
