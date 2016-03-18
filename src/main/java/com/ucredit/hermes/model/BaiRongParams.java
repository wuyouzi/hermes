package com.ucredit.hermes.model;

import java.io.Serializable;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import com.ucredit.hermes.enums.BaiRongResultCode;
import com.ucredit.hermes.enums.ResultType;

/**
 * 百融公司查询参数
 */
@Entity
@Table(name = "bairong_params")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaiRongParams implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5757246857285758021L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dbId;

    /**
     * tokenid不入数据库
     */
    @Transient
    @Column(length = 52, nullable = true)
    private String tokenid;

    /**
     * 身份证号
     */
    @Length(max = 20)
    @Column(length = 20, nullable = true)
    private String id;

    /**
     * 手机号
     */
    @Length(max = 20)
    @Column(length = 20, nullable = true)
    private String cell;

    /**
     * 电子邮件
     */
    @Length(max = 40)
    @Column(length = 40, nullable = true)
    private String mail;

    /**
     * 姓名
     */
    @Length(max = 20)
    @Column(length = 20, nullable = true)
    private String name;

    /**
     * 家庭地址
     */
    @Length(max = 100)
    @Column(length = 100, nullable = true)
    private String home_addr;

    /**
     * 公司地址
     */
    @Length(max = 100)
    @Column(length = 100, nullable = true)
    private String biz_addr;

    /**
     * 公司座机号，区号和分机号用“-”间隔
     * 如：021-55558888-12345
     */
    @Length(max = 20)
    @Column(length = 20, nullable = true)
    private String tel_biz;

    /**
     * 家庭座机号，区号和分机号用“-”间隔
     * 010-22229999
     */
    @Length(max = 20)
    @Column(length = 20, nullable = true)
    private String tel_home;

    /**
     * 申请地址(移动应用为GPS地址)
     * 例如：上海市黄浦区南京东路1号8楼401
     */
    @Length(max = 100)
    @Column(length = 100, nullable = true)
    private String apply_addr;

    /**
     * 单位名称 公司名
     * 例如：
     * 北京市水利局
     */
    @Length(max = 80)
    @Column(length = 80, nullable = true)
    private String biz_workfor;

    /**
     * 联系人姓名 李四
     */
    @Length(max = 20)
    @Column(length = 20, nullable = true)
    private String linkman_name;

    /**
     * 联系人手机号
     */
    @Length(max = 20)
    @Column(length = 20, nullable = true)
    private String linkman_cell;

    /**
     * 单位所属行业 政府机关
     */
    @Length(max = 80)
    @Column(length = 80, nullable = true)
    private String biz_industry;

    /**
     * 学历 本科
     */
    @Length(max = 80)
    @Column(length = 80, nullable = true)
    private String educationallevel;
    /**
     * 婚姻状况 未婚
     * a.未婚、
     * b.已婚、
     * c.离异、
     * d.其他
     */
    @Length(max = 80)
    @Column(length = 80, nullable = true)
    private String marriage;

    /**
     * 银行卡号(信用卡可不提供)
     * 1234567890123456
     */
    @Length(max = 30)
    @Column(length = 30, nullable = true)
    private String bank_id;

    /**
     * 创建时间
     */
    @Column(nullable = true)
    private Date createTime;

    /**
     * 查询时间
     */
    @Column(nullable = true)
    private Date queryTime;

    /**
     * 状态码
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private BaiRongResultCode resultCode;

    /**
     * 状态码描述
     */
    @Column(nullable = true)
    private String resultCodeDesc;

    /**
     * 流水号
     */
    @Column(nullable = true)
    private String swift_number;

    /**
     * 是否有效
     */
    private Boolean enabled;

    private String keyid;
    @Length(max = 50)
    private String gid;
    /**
     * 因为要做成异步使用的，所以建议加上这个状态码
     * 返回结果的状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ResultType resultType;

    @Lob
    @Column(name = "errorMsg", nullable = true)
    private String errorMsg;

    public Integer getDbId() {
        return this.dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public String getTokenid() {
        return this.tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCell() {
        return this.cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getTel_biz() {
        return this.tel_biz;
    }

    public void setTel_biz(String tel_biz) {
        this.tel_biz = tel_biz;
    }

    public String getTel_home() {
        return this.tel_home;
    }

    public void setTel_home(String tel_home) {
        this.tel_home = tel_home;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHome_addr() {
        return this.home_addr;
    }

    public void setHome_addr(String home_addr) {
        this.home_addr = home_addr;
    }

    public String getBiz_addr() {
        return this.biz_addr;
    }

    public void setBiz_addr(String biz_addr) {
        this.biz_addr = biz_addr;
    }

    public String getApply_addr() {
        return this.apply_addr;
    }

    public void setApply_addr(String apply_addr) {
        this.apply_addr = apply_addr;
    }

    public String getEducationallevel() {
        return this.educationallevel;
    }

    public void setEducationallevel(String educationallevel) {
        this.educationallevel = educationallevel;
    }

    public String getBiz_workfor() {
        return this.biz_workfor;
    }

    public void setBiz_workfor(String biz_workfor) {
        this.biz_workfor = biz_workfor;
    }

    public String getBank_id() {
        return this.bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getLinkman_name() {
        return this.linkman_name;
    }

    public void setLinkman_name(String linkman_name) {
        this.linkman_name = linkman_name;
    }

    public String getLinkman_cell() {
        return this.linkman_cell;
    }

    public void setLinkman_cell(String linkman_cell) {
        this.linkman_cell = linkman_cell;
    }

    public String getMarriage() {
        return this.marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getBiz_industry() {
        return this.biz_industry;
    }

    public void setBiz_industry(String biz_industry) {
        this.biz_industry = biz_industry;
    }

    public Date getQueryTime() {
        return this.queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public BaiRongResultCode getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(BaiRongResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCodeDesc() {
        return this.resultCodeDesc;
    }

    public void setResultCodeDesc(String resultCodeDesc) {
        this.resultCodeDesc = resultCodeDesc;
    }

    public String getSwift_number() {
        return this.swift_number;
    }

    public void setSwift_number(String swift_number) {
        this.swift_number = swift_number;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ResultType getResultType() {
        return this.resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getKeyid() {
        return this.keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getGid() {
        return this.gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

}
