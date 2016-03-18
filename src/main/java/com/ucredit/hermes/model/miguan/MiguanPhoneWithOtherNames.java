package com.ucredit.hermes.model.miguan;

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
 * 用这个手机号码绑定的其他姓名
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_phone_with_other_names")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanPhoneWithOtherNames extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 5889126135209531807L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 绑定姓名
     */
    private String susp_name;
    /**
     * 绑定时间
     */

    private String susp_updt;
    @Column(name = "phone_suspicion_id")
    private Integer phone_suspicion_id;

    public String getSusp_name() {
        return this.susp_name;
    }

    public void setSusp_name(String susp_name) {
        this.susp_name = susp_name;
    }

    public String getSusp_updt() {
        return this.susp_updt;
    }

    public void setSusp_updt(String susp_updt) {
        this.susp_updt = susp_updt;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPhone_suspicion_id() {
        return this.phone_suspicion_id;
    }

    public void setPhone_suspicion_id(Integer phone_suspicion_id) {
        this.phone_suspicion_id = phone_suspicion_id;
    }

}
