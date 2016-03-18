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
 * 手机号码存疑
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_user_phone_suspicion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanUserPhoneSuspicion extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 5735220435824044573L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用这个手机号码绑定的其他身份证
     */
    @OneToMany(targetEntity = MiguanPhoneWithOtherIdcards.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "phone_suspicion_id", updatable = true)
    private List<MiguanPhoneWithOtherIdcards> phone_with_other_idcards;
    /**
     * 用这个手机号码绑定的其他姓名
     */
    @OneToMany(targetEntity = MiguanPhoneWithOtherNames.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "phone_suspicion_id", updatable = true)
    private List<MiguanPhoneWithOtherNames> phone_with_other_names;
    /**
     * 电话号码在那些类型的机构中使用过
     */
    @OneToMany(targetEntity = MiguanPhoneAppliedInOrgs.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "phone_suspicion_id", updatable = true)
    private List<MiguanPhoneAppliedInOrgs> phone_applied_in_orgs;

    public List<MiguanPhoneWithOtherIdcards> getPhone_with_other_idcards() {
        return this.phone_with_other_idcards;
    }

    public void setPhone_with_other_idcards(
            List<MiguanPhoneWithOtherIdcards> phone_with_other_idcards) {
        this.phone_with_other_idcards = phone_with_other_idcards;
    }

    public List<MiguanPhoneWithOtherNames> getPhone_with_other_names() {
        return this.phone_with_other_names;
    }

    public void setPhone_with_other_names(
            List<MiguanPhoneWithOtherNames> phone_with_other_names) {
        this.phone_with_other_names = phone_with_other_names;
    }

    public List<MiguanPhoneAppliedInOrgs> getPhone_applied_in_orgs() {
        return this.phone_applied_in_orgs;
    }

    public void setPhone_applied_in_orgs(
            List<MiguanPhoneAppliedInOrgs> phone_applied_in_orgs) {
        this.phone_applied_in_orgs = phone_applied_in_orgs;
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
