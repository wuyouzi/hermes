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
 * 手机号码存疑
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_phone_with_otherid_cards")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanPhoneWithOtherIdcards extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -8048185291985842888L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 绑定时间
     */

    private String susp_updt;
    /**
     * 绑定的身份证
     */
    private String susp_idcard;
    @Column(name = "phone_suspicion_id")
    private Integer phone_suspicion_id;

    public String getSusp_updt() {
        return this.susp_updt;
    }

    public void setSusp_updt(String susp_updt) {
        this.susp_updt = susp_updt;
    }

    public String getSusp_idcard() {
        return this.susp_idcard;
    }

    public void setSusp_idcard(String susp_idcard) {
        this.susp_idcard = susp_idcard;
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
