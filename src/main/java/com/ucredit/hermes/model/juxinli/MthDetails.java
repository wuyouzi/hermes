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
 * 月联系情况表
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "mth_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MthDetails extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -6097213739548177553L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 联系月份
     */
    private String interact_mth;
    /**
     * 月联系次数
     */
    private Integer interact_cnt;

    @Column(name = "service_details_id")
    private Integer service_details_id;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getInteract_mth() {
        return this.interact_mth;
    }

    public void setInteract_mth(String interact_mth) {
        this.interact_mth = interact_mth;
    }

    public Integer getInteract_cnt() {
        return this.interact_cnt;
    }

    public void setInteract_cnt(Integer interact_cnt) {
        this.interact_cnt = interact_cnt;
    }

    public Integer getService_details_id() {
        return this.service_details_id;
    }

    public void setService_details_id(Integer service_details_id) {
        this.service_details_id = service_details_id;
    }

}
