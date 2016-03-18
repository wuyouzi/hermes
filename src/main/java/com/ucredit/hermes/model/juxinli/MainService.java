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
 * 常用服务
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "main_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MainService extends BaseModel<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -6521803439787357283L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 服务商类型
     */
    private String company_type;
    /**
     * 总联系次数
     */
    private String total_service_cnt;
    /**
     * 常用服务细节
     */
    @OneToMany(targetEntity = ServiceDetails.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "main_service_id", updatable = false)
    private List<ServiceDetails> service_details;

    @Column(name = "report_data_id")
    private Integer report_data_id;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany_type() {
        return this.company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getTotal_service_cnt() {
        return this.total_service_cnt;
    }

    public void setTotal_service_cnt(String total_service_cnt) {
        this.total_service_cnt = total_service_cnt;
    }

    public List<ServiceDetails> getService_details() {
        return this.service_details;
    }

    public void setService_details(List<ServiceDetails> service_details) {
        this.service_details = service_details;
    }

    public Integer getReport_data_id() {
        return this.report_data_id;
    }

    public void setReport_data_id(Integer report_data_id) {
        this.report_data_id = report_data_id;
    }

}
