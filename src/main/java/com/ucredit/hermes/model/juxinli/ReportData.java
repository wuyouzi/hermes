package com.ucredit.hermes.model.juxinli;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 查询报告主信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "report_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReportData extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 2940568231625004105L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String back_id;
    private String token;
    private String version;
    private String uuid;
    private String $Date;
    private String note;
    private String success;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();

    /**
     * 查询时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date queryTime = new Date();
    /**
     * 返回结果时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date backTime;

    @Column(name = "grant_authorization_id")
    private Integer grant_authorization_id;

    private boolean enabled = true;

    private AsyncCode errorCode;
    @Lob
    private String errorMessage;

    /**
     * 报告1还是报告2
     */
    private Integer reportNum;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "person_id")
    private Person person;
    /**
     * 信息验证
     */
    @OneToMany(targetEntity = DataCheck.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<DataCheck> dataChecks;
    /**
     * 电商月消费
     */
    @OneToMany(targetEntity = EbusinessExpense.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<EbusinessExpense> ebusinessExpenses;

    /**
     * 绑定数据源信息
     */
    @OneToMany(targetEntity = DataSource.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<DataSource> dataSources;
    /**
     * 近期需求
     */
    @OneToMany(targetEntity = RecentNeed.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<RecentNeed> recentNeeds;
    /**
     * 联系人信息
     */
    @OneToMany(targetEntity = CollectionContact.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<CollectionContact> collectionContacts;
    /**
     *
     */
    @Transient
    private Set<EbusinessContact> ebusinessContacts;
    /**
     * 常用服务
     */
    @OneToMany(targetEntity = MainService.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<MainService> mainServices;
    /**
     * 联系人区域汇总
     */
    @OneToMany(targetEntity = ContactRegion.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<ContactRegion> contactRegions;
    /**
     * 3.10 出行分析
     */
    @OneToMany(targetEntity = TripInfo.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<TripInfo> tripInfos;
    /**
     * 3.6 送货地址列表
     */
    @OneToMany(targetEntity = DeliverAddress.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<DeliverAddress> deliverAddresses;
    /**
     * 运营商联系人列表
     */
    @OneToMany(targetEntity = ContactList.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<ContactList> contactLists;
    /**
     * 3.8 通话行为分析
     */
    @OneToMany(targetEntity = Behavior.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "report_data_id", updatable = false)
    private Set<Behavior> behaviors;
    /**
     *
     */
    @Transient
    private Set<PersonalCellCollect> personalCellCollect;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String get$Date() {
        return this.$Date;
    }

    public void set$Date(String $Date) {
        this.$Date = $Date;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getBack_id() {
        return this.back_id;
    }

    public void setBack_id(String back_id) {
        this.back_id = back_id;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getGrant_authorization_id() {
        return this.grant_authorization_id;
    }

    public void setGrant_authorization_id(Integer grant_authorization_id) {
        this.grant_authorization_id = grant_authorization_id;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public Integer getReportNum() {
        return this.reportNum;
    }

    public void setReportNum(Integer reportNum) {
        this.reportNum = reportNum;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public AsyncCode getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(AsyncCode errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<DataCheck> getDataChecks() {
        return this.dataChecks;
    }

    public void setDataChecks(Set<DataCheck> dataChecks) {
        this.dataChecks = dataChecks;
    }

    public Set<EbusinessExpense> getEbusinessExpenses() {
        return this.ebusinessExpenses;
    }

    public void setEbusinessExpenses(Set<EbusinessExpense> ebusinessExpenses) {
        this.ebusinessExpenses = ebusinessExpenses;
    }

    public Set<DataSource> getDataSources() {
        return this.dataSources;
    }

    public void setDataSources(Set<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public Set<RecentNeed> getRecentNeeds() {
        return this.recentNeeds;
    }

    public void setRecentNeeds(Set<RecentNeed> recentNeeds) {
        this.recentNeeds = recentNeeds;
    }

    public Set<CollectionContact> getCollectionContacts() {
        return this.collectionContacts;
    }

    public void setCollectionContacts(Set<CollectionContact> collectionContacts) {
        this.collectionContacts = collectionContacts;
    }

    public Set<EbusinessContact> getEbusinessContacts() {
        return this.ebusinessContacts;
    }

    public void setEbusinessContacts(Set<EbusinessContact> ebusinessContacts) {
        this.ebusinessContacts = ebusinessContacts;
    }

    public Set<DeliverAddress> getDeliverAddresses() {
        return this.deliverAddresses;
    }

    public void setDeliverAddresses(Set<DeliverAddress> deliverAddresses) {
        this.deliverAddresses = deliverAddresses;
    }

    public Set<PersonalCellCollect> getPersonalCellCollect() {
        return this.personalCellCollect;
    }

    public void setPersonalCellCollect(
            Set<PersonalCellCollect> personalCellCollect) {
        this.personalCellCollect = personalCellCollect;
    }

    public Set<MainService> getMainServices() {
        return this.mainServices;
    }

    public void setMainServices(Set<MainService> mainServices) {
        this.mainServices = mainServices;
    }

    public Set<ContactRegion> getContactRegions() {
        return this.contactRegions;
    }

    public void setContactRegions(Set<ContactRegion> contactRegions) {
        this.contactRegions = contactRegions;
    }

    public Set<TripInfo> getTripInfos() {
        return this.tripInfos;
    }

    public void setTripInfos(Set<TripInfo> tripInfos) {
        this.tripInfos = tripInfos;
    }

    public Set<ContactList> getContactLists() {
        return this.contactLists;
    }

    public void setContactLists(Set<ContactList> contactLists) {
        this.contactLists = contactLists;
    }

    public Set<Behavior> getBehaviors() {
        return this.behaviors;
    }

    public void setBehaviors(Set<Behavior> behaviors) {
        this.behaviors = behaviors;
    }

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

}
