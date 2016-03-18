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
 * 用户被机构查询历史
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_user_searched_history_by_orgs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanUserSearchedHistoryByOrgs extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 8959764736650958548L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 主动查询用户信息的机构类型
     */
    private String searched_org;
    /**
     * 是否是本机构查询
     */
    private boolean org_self;
    /**
     * 查询时间
     */
    private String searched_date;
    @Column(name = "result_id")
    private Integer result_id;

    public String getSearched_org() {
        return this.searched_org;
    }

    public void setSearched_org(String searched_org) {
        this.searched_org = searched_org;
    }

    public boolean isOrg_self() {
        return this.org_self;
    }

    public void setOrg_self(boolean org_self) {
        this.org_self = org_self;
    }

    public String getSearched_date() {
        return this.searched_date;
    }

    public void setSearched_date(String searched_date) {
        this.searched_date = searched_date;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResult_id() {
        return this.result_id;
    }

    public void setResult_id(Integer result_id) {
        this.result_id = result_id;
    }

}
