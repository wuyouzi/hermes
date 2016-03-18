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
 * 黑名单信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_user_black_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanUserBlacklist extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 6259424958050939841L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 身份证是否在黑名单
     */
    private boolean blacklist_name_with_idcard;
    /**
     * 姓名是否在黑名单
     */
    private boolean blacklist_name_with_phone;
    /**
     * 黑名单分类
     */
    @Transient
    private List<String> blacklist_category;

    private String blacklist_categoryStr;

    public boolean isBlacklist_name_with_idcard() {
        return this.blacklist_name_with_idcard;
    }

    public void setBlacklist_name_with_idcard(boolean blacklist_name_with_idcard) {
        this.blacklist_name_with_idcard = blacklist_name_with_idcard;
    }

    public boolean isBlacklist_name_with_phone() {
        return this.blacklist_name_with_phone;
    }

    public void setBlacklist_name_with_phone(boolean blacklist_name_with_phone) {
        this.blacklist_name_with_phone = blacklist_name_with_phone;
    }

    public List<String> getBlacklist_category() {
        return this.blacklist_category;
    }

    public void setBlacklist_category(List<String> blacklist_category) {
        this.blacklist_category = blacklist_category;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getBlacklist_categoryStr() {
        return this.blacklist_categoryStr;
    }

    public void setBlacklist_categoryStr(String blacklist_categoryStr) {
        this.blacklist_categoryStr = blacklist_categoryStr;
    }

}
