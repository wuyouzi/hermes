package com.ucredit.hermes.model.juxinli;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
 * 服务细节表
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "service_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceDetails extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 7370978279608787883L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 服务商名字
     */
    private String company_name;
    /**
     * 服务商号码
     */
    private String service_num;
    /**
     * 联系次数
     */
    private Integer service_cnt;
    /**
     * 月联系情况
     */
    @OneToMany(targetEntity = MthDetails.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "service_details_id", updatable = false)
    private List<MthDetails> mth_details;

    @Column(name = "main_service_id")
    private Integer main_service_id;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany_name() {
        return this.company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getService_num() {
        return this.service_num;
    }

    public void setService_num(String service_num) {
        this.service_num = service_num;
    }

    public Integer getService_cnt() {
        return this.service_cnt;
    }

    public void setService_cnt(Integer service_cnt) {
        this.service_cnt = service_cnt;
    }

    public List<MthDetails> getMth_details() {
        return this.mth_details;
    }

    public void setMth_details(List<MthDetails> mth_details) {
        this.mth_details = mth_details;
    }

    public Integer getMain_service_id() {
        return this.main_service_id;
    }

    public void setMain_service_id(Integer main_service_id) {
        this.main_service_id = main_service_id;
    }

}
