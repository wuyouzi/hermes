package com.ucredit.hermes.model.crawl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

@Entity
@Table(name = "school_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SchoolInfo extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -7037178073340768380L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 北京大学
     */
    private String name;
    /**
     * 主管部门
     */
    private String ChargeDepart;
    /**
     * 所在地
     */
    private String City;
    /**
     * 办学层次
     */
    private String Level;
    /**
     * 985
     */
    private boolean Pro985;
    /**
     * 211
     */
    private boolean Eng211;
    /**
     * 录取批次
     */
    private String AdmissionBatch;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChargeDepart() {
        return this.ChargeDepart;
    }

    public void setChargeDepart(String chargeDepart) {
        this.ChargeDepart = chargeDepart;
    }

    public String getCity() {
        return this.City;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public String getLevel() {
        return this.Level;
    }

    public void setLevel(String level) {
        this.Level = level;
    }

    public boolean isPro985() {
        return this.Pro985;
    }

    public void setPro985(boolean pro985) {
        this.Pro985 = pro985;
    }

    public boolean isEng211() {
        return this.Eng211;
    }

    public void setEng211(boolean eng211) {
        this.Eng211 = eng211;
    }

    public String getAdmissionBatch() {
        return this.AdmissionBatch;
    }

    public void setAdmissionBatch(String admissionBatch) {
        this.AdmissionBatch = admissionBatch;
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
