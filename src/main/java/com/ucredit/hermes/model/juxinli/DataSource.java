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
 * 绑定数据源信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "data_source")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataSource extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 8493867775339248427L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 数据有效性
     */
    private String status;
    /**
     * 账号名称
     */
    private String account;
    /**
     * 数据源名称
     */
    private String name;
    /**
     * 数据类型名称
     */
    private String category_value;
    /**
     * 数据可靠性
     */
    private String reliability;
    /**
     * 数据源标识
     */
    private String key;
    /**
     * 数据类型
     */
    private String category_name;
    /**
     * 绑定时间
     */
    private String binding_time;

    @Column(name = "report_data_id")
    private Integer report_data_id;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_value() {
        return this.category_value;
    }

    public void setCategory_value(String category_value) {
        this.category_value = category_value;
    }

    public String getReliability() {
        return this.reliability;
    }

    public void setReliability(String reliability) {
        this.reliability = reliability;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory_name() {
        return this.category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getBinding_time() {
        return this.binding_time;
    }

    public void setBinding_time(String binding_time) {
        this.binding_time = binding_time;
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
