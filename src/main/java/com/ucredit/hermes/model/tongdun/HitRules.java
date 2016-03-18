package com.ucredit.hermes.model.tongdun;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.enums.TongdunType;
import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 所有策略命中的规则列表的集合，位于FraudApiResponse对象中，为JSON Array格式
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "tongdun_hit_rules")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HitRules extends BaseModel<Integer> {
    private static final long serialVersionUID = 6867280521585185721L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     *
     */
    private String uuid;
    /**
     * 规则编号，编号只读
     */
    private String back_id;
    /**
     * 规则名称，名称可以被修改
     */
    private String name;
    /**
     * 规则决策结果，Accept/Review/Reject，只有在首次匹配和最坏匹配模式下有效
     */
    private String decision;
    /**
     * 规则风险权重，只有在权重模式下有效
     */
    private long score;

    @Column(name = "policy_set_id")
    private Integer policy_set_id;

    @Column(name = "tongdun_fraud_id")
    private Integer tongdun_fraud_id;

    @Enumerated(EnumType.STRING)
    private TongdunType type;

    public TongdunType getType() {
        return this.type;
    }

    public void setType(TongdunType type) {
        this.type = type;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getBack_id() {
        return this.back_id;
    }

    public void setBack_id(String back_id) {
        this.back_id = back_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecision() {
        return this.decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public long getScore() {
        return this.score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public Integer getPolicy_set_id() {
        return this.policy_set_id;
    }

    public void setPolicy_set_id(Integer policy_set_id) {
        this.policy_set_id = policy_set_id;
    }

    public Integer getTongdun_fraud_id() {
        return this.tongdun_fraud_id;
    }

    public void setTongdun_fraud_id(Integer tongdun_fraud_id) {
        this.tongdun_fraud_id = tongdun_fraud_id;
    }

}
