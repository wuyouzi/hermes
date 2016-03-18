/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.pengyuan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import com.ucredit.hermes.enums.CompanyStatus;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.enums.TreatResult;

/**
 * 企业基本信息表
 *
 * @author liuqianqian
 */
@Entity
@Table(name = "company_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompanyInfo extends BaseModel<Integer> {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7277618326057443098L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 查询的企业名称
     */
    @Column(length = 512, nullable = false)
    @Index(name = "idx_company_infos_company_name")
    private String companyName;
    /**
     * 记录我们发请求的公司名称
     */
    @Column(length = 512)
    @Index(name = "idx_company_infos_pengyuan_company_name")
    private String pengYuanCompanyName;
    /**
     * 组织机构代码
     */
    @Column(length = 16)
    @Index(name = "idx_company_infos_orgCode")
    private String orgCode;
    /**
     * 工商注册号
     */
    @Column(length = 64)
    @Index(name = "idx_company_infos_registerNo")
    private String registerNo;
    /**
     * 法定代表人姓名1，数据源1
     */
    @Column(length = 64)
    private String artificialName;
    /**
     * 法定代表人姓名2，数据源2
     */
    @Column(length = 64)
    private String artificialName2;
    /**
     * 注册日期
     */
    @Temporal(TemporalType.DATE)
    private Date registDate;
    /**
     * 代码证办证日期
     */
    @Temporal(TemporalType.DATE)
    private Date handleDate;
    /**
     * 代码证作废日期
     */
    @Temporal(TemporalType.DATE)
    private Date cancelDate;
    /**
     * 开业日期
     */
    @Temporal(TemporalType.DATE)
    private Date openDate;
    /**
     * 经营开始日期
     */
    @Temporal(TemporalType.DATE)
    private Date manageBeginDate;
    /**
     * 经营截止日期
     */
    @Temporal(TemporalType.DATE)
    private Date manageEndDate;
    /**
     * 企业网址，多个网址以逗号分隔
     */
    @Column(length = 1024)
    private String url;
    /**
     * 企业当前状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private CompanyStatus status;
    /**
     * 机构状态2,与企业当前状态相同
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private CompanyStatus status2;
    /**
     * 币种
     */
    @ManyToOne(optional = true)
    private CurrencyInfo fundCurrency;

    /**
     * 币种返回的原始信息
     */
    private String currencyStr;
    /**
     * 币种2
     */
    @ManyToOne(optional = true)
    private CurrencyInfo fundCurrency2;
    private String currencyStr2;
    /**
     * 注册资金
     */
    @Column(precision = 16, scale = 2)
    private BigDecimal registFund = BigDecimal.ZERO;

    /**
     * 注册资金2
     */
    @Column(precision = 16, scale = 2)
    private BigDecimal registFund2 = BigDecimal.ZERO;

    /**
     * 机构类型
     */
    @ManyToOne(optional = true)
    private OrganTypeInfo organType;

    /**
     * 经济类型
     */
    @ManyToOne(optional = true)
    private EconomicTypeInfo economicType;

    /**
     * 行业类型
     */
    @ManyToOne(optional = true)
    private TradeInfo tradeCode;

    /**
     * 员工人数
     */
    private int companyScale;

    /**
     * 经营范围
     */
    @Lob
    private String manageRange;

    /**
     * 经营许可项目
     */
    @Lob
    private String allowManageProject;

    /**
     * 一般经营项目
     */
    @Lob
    private String generalManageProject;

    /**
     * 经营范围及方式
     */
    @Lob
    private String manageRangeFashion;

    /**
     * 企业类型
     */
    @ManyToOne(optional = true)
    private CorpTypeInfo corpType;

    /**
     * 工商登记机关
     */
    @Column(length = 128)
    private String registerDepartment;

    /**
     * 最后年检年度
     */
    @Temporal(TemporalType.DATE)
    private Date lastCheckYear;

    /**
     * 最后年检日期
     */
    @Temporal(TemporalType.DATE)
    private Date lastCheckDate;

    /**
     * 注销日期
     */
    @Temporal(TemporalType.DATE)
    private Date logoutDate;

    /**
     * 吊销日期
     */
    @Temporal(TemporalType.DATE)
    private Date revokeDate;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime = new Date();

    /**
     * 最后更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdateTime;

    /**
     * 数据源渠道
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private DataChannel dataChannel;

    /**
     * md5值
     */
    @Column(length = 64)
    private String md5;

    /**
     * 子报告ID
     */
    @ManyToOne(optional = true)
    private SubReportInfo subReportType;

    /**
     * 子报告查询状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private TreatResult treatResult;

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
     * 是否有效
     */
    private boolean enabled = true;

    /**
     * 加上乐观锁
     */
    @Version
    private int version;
    @Index(name = "idx_company_infos_ref_id")
    @Column(length = 128)
    private String refId;
    @Index(name = "idx_company_infos_query_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date queryTime;

    /**
     * 记录查询返回结果
     */
    @Lob
    private String data;

    /**
     * 查询发生费用的报告ID,90035,95001
     */
    private String feeReport;

    /**
     * 记录查询报告下子报告查询内容不为空的报告
     */
    private String subReport;

    @Column(length = 64)
    private String hasSystemError;

    /**
     * 以下是所有定义的子表
     */
    /**
     * 企业注册和经营地址及电话信息
     */
    @OneToMany(targetEntity = CompanyContactOperateInfo.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "companyInfos_id", updatable = false)
    private Set<CompanyContactOperateInfo> companyContactOperateInfos;

    /**
     * 企业股东信息
     */
    @OneToMany(targetEntity = CompanyShareholderInfo.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "companyInfos_id", updatable = false)
    private Set<CompanyShareholderInfo> companyShareholderInfos;

    /**
     * 企业高管信息
     */
    @OneToMany(targetEntity = CompanyManagerInfo.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "companyInfos_id", updatable = false)
    private Set<CompanyManagerInfo> companyManagerInfos;

    /**
     * 企业对外股权投资信息
     */
    @OneToMany(targetEntity = CompanyOtherShareholderInfo.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "companyInfos_id", updatable = false)
    private Set<CompanyOtherShareholderInfo> companyOtherShareholderInfos;

    /**
     * 法定代表人在其他机构任职信息
     */
    @OneToMany(targetEntity = LegalOtherManagerInfo.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "companyInfos_id", updatable = false)
    private Set<LegalOtherManagerInfo> legalOtherManagerInfos;

    /**
     * 法定代表人股权投资信息
     */
    @OneToMany(targetEntity = LegalOtherShareholderInfo.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "companyInfos_id", updatable = false)
    private Set<LegalOtherShareholderInfo> legalOtherShareholderInfos;

    /**
     * 企业法院被执行信息
     */
    @OneToMany(targetEntity = CompanyCourtInfo.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "companyInfos_id", updatable = false)
    private Set<CompanyCourtInfo> companyCourtInfos;
    /**
     * 进件号
     */
    @Column(length = 64)
    private String lendRequestId;

    @Column(length = 64)
    private String keyid;

    /**
     * @return the lastUpdateTime
     */
    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    /**
     * @return the pengYuanCompanyName
     */
    public String getPengYuanCompanyName() {
        return this.pengYuanCompanyName;
    }

    /**
     * @param pengYuanCompanyName
     *        the pengYuanCompanyName to set
     */
    public void setPengYuanCompanyName(String pengYuanCompanyName) {
        this.pengYuanCompanyName = pengYuanCompanyName;
    }

    /**
     * @param lastUpdateTime
     *        the lastUpdateTime to set
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the companyContactOperateInfos
     */
    public Set<CompanyContactOperateInfo> getCompanyContactOperateInfos() {
        return this.companyContactOperateInfos;
    }

    /**
     * @param companyContactOperateInfos
     *        the companyContactOperateInfos to set
     */
    public void setCompanyContactOperateInfos(
            Set<CompanyContactOperateInfo> companyContactOperateInfos) {
        this.companyContactOperateInfos = companyContactOperateInfos;
    }

    /**
     * @return the companyShareholderInfos
     */
    public Set<CompanyShareholderInfo> getCompanyShareholderInfos() {
        return this.companyShareholderInfos;
    }

    /**
     * @param companyShareholderInfos
     *        the companyShareholderInfos to set
     */
    public void setCompanyShareholderInfos(
            Set<CompanyShareholderInfo> companyShareholderInfos) {
        this.companyShareholderInfos = companyShareholderInfos;
    }

    /**
     * @return the companyManagerInfos
     */
    public Set<CompanyManagerInfo> getCompanyManagerInfos() {
        return this.companyManagerInfos;
    }

    /**
     * @param companyManagerInfos
     *        the companyManagerInfos to set
     */
    public void setCompanyManagerInfos(
            Set<CompanyManagerInfo> companyManagerInfos) {
        this.companyManagerInfos = companyManagerInfos;
    }

    /**
     * @return the companyOtherShareholderInfos
     */
    public Set<CompanyOtherShareholderInfo> getCompanyOtherShareholderInfos() {
        return this.companyOtherShareholderInfos;
    }

    /**
     * @param companyOtherShareholderInfos
     *        the companyOtherShareholderInfos to set
     */
    public void setCompanyOtherShareholderInfos(
            Set<CompanyOtherShareholderInfo> companyOtherShareholderInfos) {
        this.companyOtherShareholderInfos = companyOtherShareholderInfos;
    }

    /**
     * @return the legalOtherManagerInfos
     */
    public Set<LegalOtherManagerInfo> getLegalOtherManagerInfos() {
        return this.legalOtherManagerInfos;
    }

    /**
     * @param legalOtherManagerInfos
     *        the legalOtherManagerInfos to set
     */
    public void setLegalOtherManagerInfos(
            Set<LegalOtherManagerInfo> legalOtherManagerInfos) {
        this.legalOtherManagerInfos = legalOtherManagerInfos;
    }

    /**
     * @return the legalOtherShareholderInfos
     */
    public Set<LegalOtherShareholderInfo> getLegalOtherShareholderInfos() {
        return this.legalOtherShareholderInfos;
    }

    /**
     * @param legalOtherShareholderInfos
     *        the legalOtherShareholderInfos to set
     */
    public void setLegalOtherShareholderInfos(
            Set<LegalOtherShareholderInfo> legalOtherShareholderInfos) {
        this.legalOtherShareholderInfos = legalOtherShareholderInfos;
    }

    /**
     * @return the companyCourtInfos
     */
    public Set<CompanyCourtInfo> getCompanyCourtInfos() {
        return this.companyCourtInfos;
    }

    /**
     * @param companyCourtInfos
     *        the companyCourtInfos to set
     */
    public void setCompanyCourtInfos(Set<CompanyCourtInfo> companyCourtInfos) {
        this.companyCourtInfos = companyCourtInfos;
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
     * @return the companyName
     */
    public String getCompanyName() {
        return this.companyName;
    }

    /**
     * @param companyName
     *        the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the orgCode
     */
    public String getOrgCode() {
        return this.orgCode;
    }

    /**
     * @param orgCode
     *        the orgCode to set
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * @return the registerNo
     */
    public String getRegisterNo() {
        return this.registerNo;
    }

    /**
     * @param registerNo
     *        the registerNo to set
     */
    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    /**
     * @return the artificialName
     */
    public String getArtificialName() {
        return this.artificialName;
    }

    /**
     * @param artificialName
     *        the artificialName to set
     */
    public void setArtificialName(String artificialName) {
        this.artificialName = artificialName;
    }

    /**
     * @return the artificialName2
     */
    public String getArtificialName2() {
        return this.artificialName2;
    }

    /**
     * @param artificialName2
     *        the artificialName2 to set
     */
    public void setArtificialName2(String artificialName2) {
        this.artificialName2 = artificialName2;
    }

    /**
     * @return the registDate
     */
    public Date getRegistDate() {
        return this.registDate;
    }

    /**
     * @param registDate
     *        the registDate to set
     */
    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    /**
     * @return the handleDate
     */
    public Date getHandleDate() {
        return this.handleDate;
    }

    /**
     * @param handleDate
     *        the handleDate to set
     */
    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }

    /**
     * @return the cancelDate
     */
    public Date getCancelDate() {
        return this.cancelDate;
    }

    /**
     * @param cancelDate
     *        the cancelDate to set
     */
    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    /**
     * @return the openDate
     */
    public Date getOpenDate() {
        return this.openDate;
    }

    /**
     * @param openDate
     *        the openDate to set
     */
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    /**
     * @return the manageBeginDate
     */
    public Date getManageBeginDate() {
        return this.manageBeginDate;
    }

    /**
     * @param manageBeginDate
     *        the manageBeginDate to set
     */
    public void setManageBeginDate(Date manageBeginDate) {
        this.manageBeginDate = manageBeginDate;
    }

    /**
     * @return the manageEndDate
     */
    public Date getManageEndDate() {
        return this.manageEndDate;
    }

    /**
     * @param manageEndDate
     *        the manageEndDate to set
     */
    public void setManageEndDate(Date manageEndDate) {
        this.manageEndDate = manageEndDate;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @param url
     *        the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the status
     */
    public CompanyStatus getStatus() {
        return this.status;
    }

    /**
     * @param status
     *        the status to set
     */
    public void setStatus(CompanyStatus status) {
        this.status = status;
    }

    /**
     * @return String
     */
    @Transient
    public String getStatusString() {
        return this.status == null ? "" : this.status.toString();
    }

    /**
     * @return the status2
     */
    public CompanyStatus getStatus2() {
        return this.status2;
    }

    /**
     * @param status2
     *        the status2 to set
     */
    public void setStatus2(CompanyStatus status2) {
        this.status2 = status2;
    }

    /**
     * @return String
     */
    @Transient
    public String getStatus2String() {
        return this.status2 == null ? "" : this.status2.toString();
    }

    /**
     * @return the fundCurrency
     */
    public CurrencyInfo getFundCurrency() {
        return this.fundCurrency;
    }

    /**
     * @param fundCurrency
     *        the fundCurrency to set
     */
    public void setFundCurrency(CurrencyInfo fundCurrency) {
        this.fundCurrency = fundCurrency;
    }

    /**
     * @return the fundCurrency2
     */
    public CurrencyInfo getFundCurrency2() {
        return this.fundCurrency2;
    }

    /**
     * @param fundCurrency2
     *        the fundCurrency2 to set
     */
    public void setFundCurrency2(CurrencyInfo fundCurrency2) {
        this.fundCurrency2 = fundCurrency2;
    }

    /**
     * @return the registFund
     */
    public BigDecimal getRegistFund() {
        return this.registFund;
    }

    /**
     * @param registFund
     *        the registFund to set
     */
    public void setRegistFund(BigDecimal registFund) {
        this.registFund = registFund;
    }

    /**
     * @return the registFund2
     */
    public BigDecimal getRegistFund2() {
        return this.registFund2;
    }

    /**
     * @param registFund2
     *        the registFund2 to set
     */
    public void setRegistFund2(BigDecimal registFund2) {
        this.registFund2 = registFund2;
    }

    /**
     * @return the organType
     */
    public OrganTypeInfo getOrganType() {
        return this.organType;
    }

    /**
     * @param organType
     *        the organType to set
     */
    public void setOrganType(OrganTypeInfo organType) {
        this.organType = organType;
    }

    /**
     * @return the economicType
     */
    public EconomicTypeInfo getEconomicType() {
        return this.economicType;
    }

    /**
     * @param economicType
     *        the economicType to set
     */
    public void setEconomicType(EconomicTypeInfo economicType) {
        this.economicType = economicType;
    }

    /**
     * @return the tradeCode
     */
    public TradeInfo getTradeCode() {
        return this.tradeCode;
    }

    /**
     * @param tradeCode
     *        the tradeCode to set
     */
    public void setTradeCode(TradeInfo tradeCode) {
        this.tradeCode = tradeCode;
    }

    /**
     * @return the companyScale
     */
    public int getCompanyScale() {
        return this.companyScale;
    }

    /**
     * @param companyScale
     *        the companyScale to set
     */
    public void setCompanyScale(int companyScale) {
        this.companyScale = companyScale;
    }

    /**
     * @return the manageRange
     */
    public String getManageRange() {
        return this.manageRange;
    }

    /**
     * @param manageRange
     *        the manageRange to set
     */
    public void setManageRange(String manageRange) {
        this.manageRange = manageRange;
    }

    /**
     * @return the allowManageProject
     */
    public String getAllowManageProject() {
        return this.allowManageProject;
    }

    /**
     * @param allowManageProject
     *        the allowManageProject to set
     */
    public void setAllowManageProject(String allowManageProject) {
        this.allowManageProject = allowManageProject;
    }

    /**
     * @return the generalManageProject
     */
    public String getGeneralManageProject() {
        return this.generalManageProject;
    }

    /**
     * @param generalManageProject
     *        the generalManageProject to set
     */
    public void setGeneralManageProject(String generalManageProject) {
        this.generalManageProject = generalManageProject;
    }

    /**
     * @return the manageRangeFashion
     */
    public String getManageRangeFashion() {
        return this.manageRangeFashion;
    }

    /**
     * @param manageRangeFashion
     *        the manageRangeFashion to set
     */
    public void setManageRangeFashion(String manageRangeFashion) {
        this.manageRangeFashion = manageRangeFashion;
    }

    /**
     * @return the corpType
     */
    public CorpTypeInfo getCorpType() {
        return this.corpType;
    }

    /**
     * @param corpType
     *        the corpType to set
     */
    public void setCorpType(CorpTypeInfo corpType) {
        this.corpType = corpType;
    }

    /**
     * @return the registerDepartment
     */
    public String getRegisterDepartment() {
        return this.registerDepartment;
    }

    /**
     * @param registerDepartment
     *        the registerDepartment to set
     */
    public void setRegisterDepartment(String registerDepartment) {
        this.registerDepartment = registerDepartment;
    }

    /**
     * @return the lastCheckYear
     */
    public Date getLastCheckYear() {
        return this.lastCheckYear;
    }

    /**
     * @param lastCheckYear
     *        the lastCheckYear to set
     */
    public void setLastCheckYear(Date lastCheckYear) {
        this.lastCheckYear = lastCheckYear;
    }

    /**
     * @return the lastCheckDate
     */
    public Date getLastCheckDate() {
        return this.lastCheckDate;
    }

    /**
     * @param lastCheckDate
     *        the lastCheckDate to set
     */
    public void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    /**
     * @return the logoutDate
     */
    public Date getLogoutDate() {
        return this.logoutDate;
    }

    /**
     * @param logoutDate
     *        the logoutDate to set
     */
    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }

    /**
     * @return the revokeDate
     */
    public Date getRevokeDate() {
        return this.revokeDate;
    }

    /**
     * @param revokeDate
     *        the revokeDate to set
     */
    public void setRevokeDate(Date revokeDate) {
        this.revokeDate = revokeDate;
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

    /**
     * @return the subReportType
     */
    public SubReportInfo getSubReportType() {
        return this.subReportType;
    }

    /**
     * @param subReportType
     *        the subReportType to set
     */
    public void setSubReportType(SubReportInfo subReportType) {
        this.subReportType = subReportType;
    }

    /**
     * @return the treatResult
     */
    public TreatResult getTreatResult() {
        return this.treatResult;
    }

    /**
     * @param treatResult
     *        the treatResult to set
     */
    public void setTreatResult(TreatResult treatResult) {
        this.treatResult = treatResult;
    }

    /**
     * @return String
     */
    @Transient
    public String getTreatResultString() {
        return this.treatResult == null ? "" : this.treatResult.toString();
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * @param errorMessage
     *        the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRefId() {
        return this.refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getQueryTime() {
        return this.queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public String getLendRequestId() {
        return this.lendRequestId;
    }

    public void setLendRequestId(String lendRequestId) {
        this.lendRequestId = lendRequestId;
    }

    public String getFeeReport() {
        return this.feeReport;
    }

    public void setFeeReport(String feeReport) {
        this.feeReport = feeReport;
    }

    public String getSubReport() {
        return this.subReport;
    }

    public void setSubReport(String subReport) {
        this.subReport = subReport;
    }

    public String getKeyid() {
        return this.keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getHasSystemError() {
        return this.hasSystemError;
    }

    public void setHasSystemError(String hasSystemError) {
        this.hasSystemError = hasSystemError;
    }

    public String getCurrencyStr() {
        return this.currencyStr;
    }

    public void setCurrencyStr(String currencyStr) {
        this.currencyStr = currencyStr;
    }

    public String getCurrencyStr2() {
        return this.currencyStr2;
    }

    public void setCurrencyStr2(String currencyStr2) {
        this.currencyStr2 = currencyStr2;
    }

}
