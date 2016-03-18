package com.ucredit.hermes.model.juxinli;

import java.util.List;

public class JsonInfo {
    private DataJsonInfoId _id;
    private List<EbusinessExpense> ebusiness_expense;
    private List<DataSource> data_source;
    private List<BehaviorCheck> behavior_check;
    private List<RecentNeed> recent_need;
    private List<CollectionContact> collection_contact;
    private UnionpayExpense unionpay_expense;
    private List<EbusinessContact> ebusiness_contact;
    private Person person;
    private List<MainService> main_service;
    private List<ContactRegion> contact_region;
    private List<ApplicationCheck> application_check;
    private List<TripInfo> trip_info;
    private Report report;
    private List<DeliverAddress> deliver_address;
    private List<ContactList> contact_list;
    private List<CellBehavior> cell_behavior;
    private List<PersonalCellCollect> personal_cell_collect;

    public DataJsonInfoId get_id() {
        return this._id;
    }

    public void set_id(DataJsonInfoId _id) {
        this._id = _id;
    }

    public List<EbusinessExpense> getEbusiness_expense() {
        return this.ebusiness_expense;
    }

    public void setEbusiness_expense(List<EbusinessExpense> ebusiness_expense) {
        this.ebusiness_expense = ebusiness_expense;
    }

    public List<DataSource> getData_source() {
        return this.data_source;
    }

    public void setData_source(List<DataSource> data_source) {
        this.data_source = data_source;
    }

    public List<BehaviorCheck> getBehavior_check() {
        return this.behavior_check;
    }

    public void setBehavior_check(List<BehaviorCheck> behavior_check) {
        this.behavior_check = behavior_check;
    }

    public List<RecentNeed> getRecent_need() {
        return this.recent_need;
    }

    public void setRecent_need(List<RecentNeed> recent_need) {
        this.recent_need = recent_need;
    }

    public List<CollectionContact> getCollection_contact() {
        return this.collection_contact;
    }

    public void setCollection_contact(List<CollectionContact> collection_contact) {
        this.collection_contact = collection_contact;
    }

    public UnionpayExpense getUnionpay_expense() {
        return this.unionpay_expense;
    }

    public void setUnionpay_expense(UnionpayExpense unionpay_expense) {
        this.unionpay_expense = unionpay_expense;
    }

    public List<EbusinessContact> getEbusiness_contact() {
        return this.ebusiness_contact;
    }

    public void setEbusiness_contact(List<EbusinessContact> ebusiness_contact) {
        this.ebusiness_contact = ebusiness_contact;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<MainService> getMain_service() {
        return this.main_service;
    }

    public void setMain_service(List<MainService> main_service) {
        this.main_service = main_service;
    }

    public List<ContactRegion> getContact_region() {
        return this.contact_region;
    }

    public void setContact_region(List<ContactRegion> contact_region) {
        this.contact_region = contact_region;
    }

    public List<ApplicationCheck> getApplication_check() {
        return this.application_check;
    }

    public void setApplication_check(List<ApplicationCheck> application_check) {
        this.application_check = application_check;
    }

    public List<TripInfo> getTrip_info() {
        return this.trip_info;
    }

    public void setTrip_info(List<TripInfo> trip_info) {
        this.trip_info = trip_info;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public List<DeliverAddress> getDeliver_address() {
        return this.deliver_address;
    }

    public void setDeliver_address(List<DeliverAddress> deliver_address) {
        this.deliver_address = deliver_address;
    }

    public List<ContactList> getContact_list() {
        return this.contact_list;
    }

    public void setContact_list(List<ContactList> contact_list) {
        this.contact_list = contact_list;
    }

    public List<CellBehavior> getCell_behavior() {
        return this.cell_behavior;
    }

    public void setCell_behavior(List<CellBehavior> cell_behavior) {
        this.cell_behavior = cell_behavior;
    }

    public List<PersonalCellCollect> getPersonal_cell_collect() {
        return this.personal_cell_collect;
    }

    public void setPersonal_cell_collect(
            List<PersonalCellCollect> personal_cell_collect) {
        this.personal_cell_collect = personal_cell_collect;
    }

}
