package com.ucredit.hermes.model.miguan;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * @author zhouwuyuan
 */
@Entity
@Table(name = "miguan_grid_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MiguanGridInfo extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 9109067634490495610L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 反馈结果是否正确
     */
    private String status;
    /**
     * 数据更新时间
     */

    private String update_time;
    /**
     * 错误码
     */
    private String error_code;
    /**
     * 错误码备注
     */
    private String error_msg;

    /**
     *
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "result_id")
    private MiguanResult result;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getError_code() {
        return this.error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return this.error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public MiguanResult getResult() {
        return this.result;
    }

    public void setResult(MiguanResult result) {
        this.result = result;
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
