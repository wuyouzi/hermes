package com.ucredit.hermes.model.juxinli;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 数据检查点
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "data_check")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataCheck extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -8143805238257568593L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 检查点类别
     */
    private String category;
    /**
     * 检查项目
     */
    private String check_point;
    /**
     * 检查结果
     */
    private String result;
    /**
     * 证据
     */
    @Lob
    private String evidence;
    /**
     * 检查点类型：application或behavior
     */

    private String type;
    @Column(name = "report_data_id")
    private Integer report_data_id;

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCheck_point() {
        return this.check_point;
    }

    public void setCheck_point(String check_point) {
        this.check_point = check_point;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEvidence() {
        return this.evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReport_data_id() {
        return this.report_data_id;
    }

    public void setReport_data_id(Integer report_data_id) {
        this.report_data_id = report_data_id;
    }

}
