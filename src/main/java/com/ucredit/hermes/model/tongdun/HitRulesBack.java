package com.ucredit.hermes.model.tongdun;

/**
 * 所有策略命中的规则列表的集合，位于FraudApiResponse对象中，为JSON Array格式
 *
 * @author zhouwuyuan
 */
public class HitRulesBack {
    /**
     *
     */
    private String uuid;
    /**
     * 规则编号，编号只读
     */
    private String id;
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

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

}
