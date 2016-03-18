package com.ucredit.hermes.model.miguan;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ucredit.hermes.model.pengyuan.BaseModel;

@Entity
@Table(name = "miguan_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanResult extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 3708366268266250293L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用户灰度信息
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "user_gray_id")
    private MiguanUserGray user_gray;
    /**
     * 手机号码存疑
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "user_phone_suspicion_id")
    private MiguanUserPhoneSuspicion user_phone_suspicion;
    /**
     * 更新时间
     */
    private String update_time;
    /**
     * 身份证号码存疑
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "user_idcard_suspicion_id")
    private MiguanUserIdCardSuspicion user_idcard_suspicion;
    /**
     * 用户被机构查询历史
     */
    @OneToMany(targetEntity = MiguanUserSearchedHistoryByOrgs.class,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "result_id", updatable = false)
    private List<MiguanUserSearchedHistoryByOrgs> user_searched_history_by_orgs;
    /**
     * 黑名单信息
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "user_blacklist_id")
    private MiguanUserBlacklist user_blacklist;
    /**
     * 基本信息
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "user_basic_id")
    private MiguanUserBasic user_basic;
    /**
     * 用户注册信息情况（来源于聚信立千寻项目）
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "user_register_orgs_id")
    private MiguanUserRegisterOrgs user_register_orgs;

    /**
     * 授权查询蜜罐数据的机构账号
     */
    private String auth_org;
    /**
     * 蜜罐ID
     */
    private String user_grid_id;

    public MiguanUserGray getUser_gray() {
        return this.user_gray;
    }

    public void setUser_gray(MiguanUserGray user_gray) {
        this.user_gray = user_gray;
    }

    public MiguanUserPhoneSuspicion getUser_phone_suspicion() {
        return this.user_phone_suspicion;
    }

    public void setUser_phone_suspicion(
            MiguanUserPhoneSuspicion user_phone_suspicion) {
        this.user_phone_suspicion = user_phone_suspicion;
    }

    public String getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public List<MiguanUserSearchedHistoryByOrgs> getUser_searched_history_by_orgs() {
        return this.user_searched_history_by_orgs;
    }

    public void setUser_searched_history_by_orgs(
            List<MiguanUserSearchedHistoryByOrgs> user_searched_history_by_orgs) {
        this.user_searched_history_by_orgs = user_searched_history_by_orgs;
    }

    public MiguanUserBlacklist getUser_blacklist() {
        return this.user_blacklist;
    }

    public void setUser_blacklist(MiguanUserBlacklist user_blacklist) {
        this.user_blacklist = user_blacklist;
    }

    public MiguanUserBasic getUser_basic() {
        return this.user_basic;
    }

    public void setUser_basic(MiguanUserBasic user_basic) {
        this.user_basic = user_basic;
    }

    public MiguanUserRegisterOrgs getUser_register_orgs() {
        return this.user_register_orgs;
    }

    public void setUser_register_orgs(MiguanUserRegisterOrgs user_register_orgs) {
        this.user_register_orgs = user_register_orgs;
    }

    public String getAuth_org() {
        return this.auth_org;
    }

    public void setAuth_org(String auth_org) {
        this.auth_org = auth_org;
    }

    public String getUser_grid_id() {
        return this.user_grid_id;
    }

    public void setUser_grid_id(String user_grid_id) {
        this.user_grid_id = user_grid_id;
    }

    public MiguanUserIdCardSuspicion getUser_idcard_suspicion() {
        return this.user_idcard_suspicion;
    }

    public void setUser_idcard_suspicion(
            MiguanUserIdCardSuspicion user_idcard_suspicion) {
        this.user_idcard_suspicion = user_idcard_suspicion;
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
