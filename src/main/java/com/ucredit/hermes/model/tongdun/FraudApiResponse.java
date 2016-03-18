package com.ucredit.hermes.model.tongdun;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 同盾返回信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "tongdun_fraud_api_response")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FraudApiResponse extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 6005888662389884331L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 本次调用是否成功，true表示成功调用
     */
    private boolean success;
    /**
     * 如果success为false，此处显示错误码
     */
    private String reason_code;
    /**
     * 本次调用的请求id，用于事后反查事件
     */
    private String seq_id;
    /**
     * 最终的风险评估结果，取所有策略中分数最高的结果，结果有三种：Accept无风险，通过；Review低风险，审查；Reject高风险，拒绝
     */
    private String final_decision;
    /**
     * 最终的风险系数。权重模式下是策略中所有命中规则分数相加，首次匹配和最坏匹配时则由其中一条，
     * 规则决定，最后取策略集下所有策略中分数最高的作为最终风险系数，该系数越高说明风险越大
     */
    private long final_score;
    /**
     * 所有策略命中的规则列表的集合，位于FraudApiResponse对象中
     */
    @Transient
    private List<HitRulesBack> hit_rules;

    @OneToMany(targetEntity = HitRules.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "tongdun_fraud_id", updatable = true)
    private List<HitRules> hit_rules_convert;
    /**
     * 命中的风险类型的标识及风险结果的集合，只显示结果为Review和Reject的，格式为风险类型_评估结果，此处表示账户盗用低风险
     */
    private String risk_type;
    /**
     * 设备指纹信息，若用户传入resp_detail_type字段包含device，则返回结果包含该信息，否则无
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "device_info_id")
    private DeviceInfo device_info;
    /**
     * 用户传入的地理位置信息，若用户传入resp_detail_type字段包含geoip，则返回结果包含该信息，否则无
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "geoip_info_id")
    private GeoipInfo geoip_info;
    /**
     * 用户传入的身份证和手机号，若用户传入resp_detail_type字段包含attribution，则返回结果包含该信息，否则无
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "attribution_id")
    private Attribution attribution;
    /**
     * 触发的策略集名称
     */
    private String policy_set_name;
    /**
     * 展示策略集的内容，为JSON Array格式
     */
    @OneToMany(targetEntity = PolicySet.class, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "tongdun_fraud_id", updatable = true)
    private List<PolicySet> policy_set;

    @Temporal(TemporalType.TIMESTAMP)
    private Date queryTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date backTime;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason_code() {
        return this.reason_code;
    }

    public void setReason_code(String reason_code) {
        this.reason_code = reason_code;
    }

    public String getSeq_id() {
        return this.seq_id;
    }

    public void setSeq_id(String seq_id) {
        this.seq_id = seq_id;
    }

    public String getFinal_decision() {
        return this.final_decision;
    }

    public void setFinal_decision(String final_decision) {
        this.final_decision = final_decision;
    }

    public long getFinal_score() {
        return this.final_score;
    }

    public void setFinal_score(long final_score) {
        this.final_score = final_score;
    }

    public List<HitRulesBack> getHit_rules() {
        return this.hit_rules;
    }

    public void setHit_rules(List<HitRulesBack> hit_rules) {
        this.hit_rules = hit_rules;
    }

    public String getRisk_type() {
        return this.risk_type;
    }

    public void setRisk_type(String risk_type) {
        this.risk_type = risk_type;
    }

    public DeviceInfo getDevice_info() {
        return this.device_info;
    }

    public void setDevice_info(DeviceInfo device_info) {
        this.device_info = device_info;
    }

    public GeoipInfo getGeoip_info() {
        return this.geoip_info;
    }

    public void setGeoip_info(GeoipInfo geoip_info) {
        this.geoip_info = geoip_info;
    }

    public Attribution getAttribution() {
        return this.attribution;
    }

    public void setAttribution(Attribution attribution) {
        this.attribution = attribution;
    }

    public String getPolicy_set_name() {
        return this.policy_set_name;
    }

    public void setPolicy_set_name(String policy_set_name) {
        this.policy_set_name = policy_set_name;
    }

    public List<PolicySet> getPolicy_set() {
        return this.policy_set;
    }

    public void setPolicy_set(List<PolicySet> policy_set) {
        this.policy_set = policy_set;
    }

    public List<HitRules> getHit_rules_convert() {
        return this.hit_rules_convert;
    }

    public void setHit_rules_convert(List<HitRules> hit_rules_convert) {
        this.hit_rules_convert = hit_rules_convert;
    }

    public Date getQueryTime() {
        return this.queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public Date getBackTime() {
        return this.backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

}
