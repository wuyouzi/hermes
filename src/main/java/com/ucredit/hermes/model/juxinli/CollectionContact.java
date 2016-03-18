package com.ucredit.hermes.model.juxinli;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 3.11 联系人信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "collection_contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CollectionContact extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 2914532563022081273L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 电商送货总金额
     */
    private float total_amount;
    /**
     * 最晚出现时间
     */
    private String end_date;
    /**
     * 电商送货总数
     */
    private Integer total_count;
    /**
     * 最早出现时间
     */
    private String begin_date;
    /**
     * 联系人姓名
     */
    private String contact_name;
    /**
     * 呼叫信息统计
     */
    @OneToMany(targetEntity = ContactDetails.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "collection_cantact_id", updatable = false)
    private List<ContactDetails> contact_details;
    @Column(name = "report_data_id")
    private Integer report_data_id;

    public float getTotal_amount() {
        return this.total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    public String getEnd_date() {
        return this.end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Integer getTotal_count() {
        return this.total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public String getBegin_date() {
        return this.begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getContact_name() {
        return this.contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public List<ContactDetails> getContact_details() {
        return this.contact_details;
    }

    public void setContact_details(List<ContactDetails> contact_details) {
        this.contact_details = contact_details;
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
