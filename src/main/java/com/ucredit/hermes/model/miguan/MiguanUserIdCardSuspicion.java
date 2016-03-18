package com.ucredit.hermes.model.miguan;

import java.util.List;

import javax.persistence.CascadeType;
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
 * 身份证号码存疑
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_userid_card_suspicion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanUserIdCardSuspicion extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 8897767038720867452L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用这个身份证号码绑定的其他姓名
     */
    @OneToMany(targetEntity = MiguanIdCardWithOtherNames.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "card_suspicion_id", updatable = false)
    private List<MiguanIdCardWithOtherNames> idcard_with_other_names;
    /**
     * 用这个身份证绑定的其他手机号码
     */
    @OneToMany(targetEntity = MiguanIdCardWithOtherPhones.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "card_suspicion_id", updatable = false)
    private List<MiguanIdCardWithOtherPhones> idcard_with_other_phones;
    /**
     * 身份证在那些类型的机构中使用过
     */
    @OneToMany(targetEntity = MiguanIdCardAppliedInOrgs.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "card_suspicion_id", updatable = false)
    private List<MiguanIdCardAppliedInOrgs> idcard_applied_in_orgs;

    public List<MiguanIdCardWithOtherNames> getIdcard_with_other_names() {
        return this.idcard_with_other_names;
    }

    public void setIdcard_with_other_names(
            List<MiguanIdCardWithOtherNames> idcard_with_other_names) {
        this.idcard_with_other_names = idcard_with_other_names;
    }

    public List<MiguanIdCardWithOtherPhones> getIdcard_with_other_phones() {
        return this.idcard_with_other_phones;
    }

    public void setIdcard_with_other_phones(
            List<MiguanIdCardWithOtherPhones> idcard_with_other_phones) {
        this.idcard_with_other_phones = idcard_with_other_phones;
    }

    public List<MiguanIdCardAppliedInOrgs> getIdcard_applied_in_orgs() {
        return this.idcard_applied_in_orgs;
    }

    public void setIdcard_applied_in_orgs(
            List<MiguanIdCardAppliedInOrgs> idcard_applied_in_orgs) {
        this.idcard_applied_in_orgs = idcard_applied_in_orgs;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}
