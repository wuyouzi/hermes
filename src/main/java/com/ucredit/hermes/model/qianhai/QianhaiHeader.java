package com.ucredit.hermes.model.qianhai;

/**
 * header信息
 *
 * @author zhouwuyuan
 */
public class QianhaiHeader {
    /**
     * 交易时间
     */
    private String transDate;
    /**
     * 授权代码
     */
    private String authCode;
    /**
     * 错误消息
     */
    private String rtMsg;
    /**
     * 机构代码
     */
    private String orgCode;
    /**
     * 渠道、系统ID
     */
    private String chnlId;
    /**
     * 交易流水号
     */
    private String transNo;
    /**
     * 错误代码
     */
    private String rtCode;
    /**
     * 授权时间
     */
    private String authDate;

    public String getTransDate() {
        return this.transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getRtMsg() {
        return this.rtMsg;
    }

    public void setRtMsg(String rtMsg) {
        this.rtMsg = rtMsg;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getChnlId() {
        return this.chnlId;
    }

    public void setChnlId(String chnlId) {
        this.chnlId = chnlId;
    }

    public String getTransNo() {
        return this.transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getRtCode() {
        return this.rtCode;
    }

    public void setRtCode(String rtCode) {
        this.rtCode = rtCode;
    }

    public String getAuthDate() {
        return this.authDate;
    }

    public void setAuthDate(String authDate) {
        this.authDate = authDate;
    }

}
