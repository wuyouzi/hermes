package com.ucredit.hermes.model.miguan;

import java.util.List;

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
 * 用户注册信息情况（来源于聚信立千寻项目）
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_user_register_orgs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanUserRegisterOrgs extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 5326692529525675052L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 查询手机号码
     */
    private String phone_num;
    /**
     * 注册数量
     */
    private int register_cnt;
    /**
     *
     */
    @Transient
    private List<String> register_orgs;

    private String register_orgsStr;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone_num() {
        return this.phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public int getRegister_cnt() {
        return this.register_cnt;
    }

    public void setRegister_cnt(int register_cnt) {
        this.register_cnt = register_cnt;
    }

    public List<String> getRegister_orgs() {
        return this.register_orgs;
    }

    public void setRegister_orgs(List<String> register_orgs) {
        this.register_orgs = register_orgs;
    }

    public String getRegister_orgsStr() {
        return this.register_orgsStr;
    }

    public void setRegister_orgsStr(String register_orgsStr) {
        this.register_orgsStr = register_orgsStr;
    }

}
