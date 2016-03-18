package com.ucredit.hermes.model.tongdun;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 展示策略集的内容，为JSON Array格式
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "tongdun_policy_set")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PolicySet extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -1189056925126537978L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String policy_uuid;
    /**
     * 策略结果，此处是审核
     */
    private String policy_decision;
    /**
     * 策略模式，共有三种：1.首次匹配：取第一条命中的规则作为返回结果；2.最坏匹配：取命中规则中决策结果最坏的作为返回结果；
     * 3.权重模式：对所有命中规则进行加权处理作为最后的返回结果。
     */
    private String policy_mode;
    /**
     * 策略分数，根据命中规则分数而来，不同模式计算方式不同。
     */
    private long policy_score;
    /**
     * 策略名
     */
    private String policy_name;
    /**
     * 策略要识别的风险类型的标识，此处表示账户盗用，其它标识详见资源中心-风险决策服务
     */
    private String risk_type;
    /**
     * 策略中命中的规则列表
     */
    @Transient
    private List<HitRulesBack> hit_rules;

    @OneToMany(targetEntity = HitRules.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "policy_set_id", updatable = true)
    private List<HitRules> hit_rules_convert;

    @Column(name = "tongdun_fraud_id")
    private Integer tongdun_fraud_id;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPolicy_uuid() {
        return this.policy_uuid;
    }

    public void setPolicy_uuid(String policy_uuid) {
        this.policy_uuid = policy_uuid;
    }

    public String getPolicy_decision() {
        return this.policy_decision;
    }

    public void setPolicy_decision(String policy_decision) {
        this.policy_decision = policy_decision;
    }

    public String getPolicy_mode() {
        return this.policy_mode;
    }

    public void setPolicy_mode(String policy_mode) {
        this.policy_mode = policy_mode;
    }

    public long getPolicy_score() {
        return this.policy_score;
    }

    public void setPolicy_score(long policy_score) {
        this.policy_score = policy_score;
    }

    public String getPolicy_name() {
        return this.policy_name;
    }

    public void setPolicy_name(String policy_name) {
        this.policy_name = policy_name;
    }

    public String getRisk_type() {
        return this.risk_type;
    }

    public void setRisk_type(String risk_type) {
        this.risk_type = risk_type;
    }

    public List<HitRulesBack> getHit_rules() {
        return this.hit_rules;
    }

    public void setHit_rules(List<HitRulesBack> hit_rules) {
        this.hit_rules = hit_rules;
    }

    public Integer getTongdun_fraud_id() {
        return this.tongdun_fraud_id;
    }

    public void setTongdun_fraud_id(Integer tongdun_fraud_id) {
        this.tongdun_fraud_id = tongdun_fraud_id;
    }

    public List<HitRules> getHit_rules_convert() {
        return this.hit_rules_convert;
    }

    public void setHit_rules_convert(List<HitRules> hit_rules_convert) {
        this.hit_rules_convert = hit_rules_convert;
    }

}
