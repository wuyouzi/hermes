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
 * 呼叫信息统
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "contact_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactDetails extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 4879585841206885036L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 短信条数
     */
    private Integer sms_cnt;
    /**
     * 电话号码
     */
    private String phone_num;
    /**
     * 号码归属地
     */
    private String phone_num_loc;
    /**
     * 最早沟通时间
     */
    private String trans_start;
    /**
     * 呼入次数
     */
    private Integer call_in_cnt;
    /**
     * 呼叫次数
     */
    private Integer call_cnt;
    /**
     * 呼叫时长
     */
    private float call_len;
    /**
     * 呼出次数
     */
    private Integer call_out_cnt;
    /**
     * 最晚沟通时间
     */
    private String trans_end;

    @Column(name = "collection_cantact_id")
    private Integer collection_cantact_id;

    public Integer getSms_cnt() {
        return this.sms_cnt;
    }

    public void setSms_cnt(Integer sms_cnt) {
        this.sms_cnt = sms_cnt;
    }

    public String getPhone_num() {
        return this.phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getPhone_num_loc() {
        return this.phone_num_loc;
    }

    public void setPhone_num_loc(String phone_num_loc) {
        this.phone_num_loc = phone_num_loc;
    }

    public String getTrans_start() {
        return this.trans_start;
    }

    public void setTrans_start(String trans_start) {
        this.trans_start = trans_start;
    }

    public Integer getCall_in_cnt() {
        return this.call_in_cnt;
    }

    public void setCall_in_cnt(Integer call_in_cnt) {
        this.call_in_cnt = call_in_cnt;
    }

    public Integer getCall_cnt() {
        return this.call_cnt;
    }

    public void setCall_cnt(Integer call_cnt) {
        this.call_cnt = call_cnt;
    }

    public float getCall_len() {
        return this.call_len;
    }

    public void setCall_len(float call_len) {
        this.call_len = call_len;
    }

    public Integer getCall_out_cnt() {
        return this.call_out_cnt;
    }

    public void setCall_out_cnt(Integer call_out_cnt) {
        this.call_out_cnt = call_out_cnt;
    }

    public String getTrans_end() {
        return this.trans_end;
    }

    public void setTrans_end(String trans_end) {
        this.trans_end = trans_end;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCollection_cantact_id() {
        return this.collection_cantact_id;
    }

    public void setCollection_cantact_id(Integer collection_cantact_id) {
        this.collection_cantact_id = collection_cantact_id;
    }

}
