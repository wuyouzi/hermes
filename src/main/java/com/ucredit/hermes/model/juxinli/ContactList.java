package com.ucredit.hermes.model.juxinli;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 运营商联系人
 *
 * @author zhouuwyuan
 */
@Entity
@Table(name = "contact_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactList extends BaseModel<Integer> {
    private static final long serialVersionUID = -1686845847732738605L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 中午联系次数
     */
    private Integer contact_noon;
    /**
     * 号码归属地
     */
    private String phone_num_loc;
    /**
     * 最近三月联系次数
     */
    private Integer contact_3m;
    /**
     * 最近一月联系次数
     */
    private Integer contact_1m;
    /**
     * 周末联系次数
     */
    private Integer contact_weekend;
    /**
     * 关系推测
     */
    private String p_relation;
    /**
     * 周中联系次数
     */
    private Integer contact_weekday;
    /**
     * （可能的）姓名
     */
    private String contact_name;
    /**
     * 呼入次数
     */
    private Integer call_in_cnt;
    /**
     * 呼出次数
     */
    private Integer call_out_cnt;
    /**
     *
     */
    private Integer call_out_len;
    /**
     * 节假日联系次数
     */
    private Integer contact_holiday;
    /**
     * 凌晨联系次数
     */
    private Integer contact_early_morning;
    /**
     * 号码
     */
    private String phone_num;
    /**
     * 晚上联系次数
     */
    private Integer contact_afternoon;
    /**
     * 通话时长
     */
    private Integer call_len;
    /**
     *
     */
    private String needs_type;
    /**
     * 深夜联系次数
     */
    private Integer contact_night;
    /**
     * 通话次数
     */
    private Integer call_cnt;
    /**
     *
     */
    private Integer call_in_len;
    /**
     * 是否全天联系
     */
    private boolean contact_all_day;
    /**
     * 上午联系次数
     */
    private Integer contact_morning;
    /**
     * 最近一周联系次数
     */
    private Integer contact_1w;
    @Column(name = "report_data_id")
    private Integer report_data_id;

    public Integer getContact_noon() {
        return this.contact_noon;
    }

    public void setContact_noon(Integer contact_noon) {
        this.contact_noon = contact_noon;
    }

    public String getPhone_num_loc() {
        return this.phone_num_loc;
    }

    public void setPhone_num_loc(String phone_num_loc) {
        this.phone_num_loc = phone_num_loc;
    }

    public Integer getContact_3m() {
        return this.contact_3m;
    }

    public void setContact_3m(Integer contact_3m) {
        this.contact_3m = contact_3m;
    }

    public Integer getContact_1m() {
        return this.contact_1m;
    }

    public void setContact_1m(Integer contact_1m) {
        this.contact_1m = contact_1m;
    }

    public Integer getContact_weekend() {
        return this.contact_weekend;
    }

    public void setContact_weekend(Integer contact_weekend) {
        this.contact_weekend = contact_weekend;
    }

    public String getP_relation() {
        return this.p_relation;
    }

    public void setP_relation(String p_relation) {
        this.p_relation = p_relation;
    }

    public Integer getContact_weekday() {
        return this.contact_weekday;
    }

    public void setContact_weekday(Integer contact_weekday) {
        this.contact_weekday = contact_weekday;
    }

    public String getContact_name() {
        return this.contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public Integer getCall_in_cnt() {
        return this.call_in_cnt;
    }

    public void setCall_in_cnt(Integer call_in_cnt) {
        this.call_in_cnt = call_in_cnt;
    }

    public Integer getCall_out_cnt() {
        return this.call_out_cnt;
    }

    public void setCall_out_cnt(Integer call_out_cnt) {
        this.call_out_cnt = call_out_cnt;
    }

    public Integer getCall_out_len() {
        return this.call_out_len;
    }

    public void setCall_out_len(Integer call_out_len) {
        this.call_out_len = call_out_len;
    }

    public Integer getContact_holiday() {
        return this.contact_holiday;
    }

    public void setContact_holiday(Integer contact_holiday) {
        this.contact_holiday = contact_holiday;
    }

    public Integer getContact_early_morning() {
        return this.contact_early_morning;
    }

    public void setContact_early_morning(Integer contact_early_morning) {
        this.contact_early_morning = contact_early_morning;
    }

    public String getPhone_num() {
        return this.phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public Integer getContact_afternoon() {
        return this.contact_afternoon;
    }

    public void setContact_afternoon(Integer contact_afternoon) {
        this.contact_afternoon = contact_afternoon;
    }

    public Integer getCall_len() {
        return this.call_len;
    }

    public void setCall_len(Integer call_len) {
        this.call_len = call_len;
    }

    public String getNeeds_type() {
        return this.needs_type;
    }

    public void setNeeds_type(String needs_type) {
        this.needs_type = needs_type;
    }

    public Integer getContact_night() {
        return this.contact_night;
    }

    public void setContact_night(Integer contact_night) {
        this.contact_night = contact_night;
    }

    public Integer getCall_cnt() {
        return this.call_cnt;
    }

    public void setCall_cnt(Integer call_cnt) {
        this.call_cnt = call_cnt;
    }

    public Integer getCall_in_len() {
        return this.call_in_len;
    }

    public void setCall_in_len(Integer call_in_len) {
        this.call_in_len = call_in_len;
    }

    public boolean isContact_all_day() {
        return this.contact_all_day;
    }

    public void setContact_all_day(boolean contact_all_day) {
        this.contact_all_day = contact_all_day;
    }

    public Integer getContact_morning() {
        return this.contact_morning;
    }

    public void setContact_morning(Integer contact_morning) {
        this.contact_morning = contact_morning;
    }

    public Integer getContact_1w() {
        return this.contact_1w;
    }

    public void setContact_1w(Integer contact_1w) {
        this.contact_1w = contact_1w;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReport_data_id() {
        return this.report_data_id;
    }

    public void setReport_data_id(Integer report_data_id) {
        this.report_data_id = report_data_id;
    }

}
