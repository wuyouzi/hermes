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

@Entity
@Table(name = "miguan_phone_applied_in_orgs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanPhoneAppliedInOrgs extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -7060197956202295139L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 机构所属分类
     */
    private String susp_org_type;
    /**
     * 查询时间
     */

    private String susp_updt;
    @Column(name = "phone_suspicion_id")
    private Integer phone_suspicion_id;

    public String getSusp_org_type() {
        return this.susp_org_type;
    }

    public void setSusp_org_type(String susp_org_type) {
        this.susp_org_type = susp_org_type;
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
