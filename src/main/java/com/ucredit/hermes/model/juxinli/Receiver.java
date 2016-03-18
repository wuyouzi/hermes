package com.ucredit.hermes.model.juxinli;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 收货人 @author zhouwuyuan
 */
@Entity
@Table(name = "receiver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Receiver extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -638135949628956396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 收货人姓名
     */
    private String name;
    /**
     * 收货人电话号码
     */
    @Transient
    private List<String> phone_num_list;

    private String receiver_phone_nums;
    /**
     * 送货金额
     */
    private float amount;
    /**
     * 送货次数
     */
    private Integer count;
    @Column(name = "deliver_address_id")
    private Integer deliver_address_id;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhone_num_list() {
        return this.phone_num_list;
    }

    public void setPhone_num_list(List<String> phone_num_list) {
        this.phone_num_list = phone_num_list;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeliver_address_id() {
        return this.deliver_address_id;
    }

    public void setDeliver_address_id(Integer deliver_address_id) {
        this.deliver_address_id = deliver_address_id;
    }

    public String getReceiver_phone_nums() {
        return this.receiver_phone_nums;
    }

    public void setReceiver_phone_nums(String receiver_phone_nums) {
        this.receiver_phone_nums = receiver_phone_nums;
    }

}
