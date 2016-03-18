package com.ucredit.hermes.model.juxinli;

/**
 * 数据检查点
 *
 * @author zhouwuyuan
 */
public class BehaviorCheck {
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
    private String evidence;

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

}
