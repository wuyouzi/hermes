package test;

import java.util.Date;

public class HdlBlacklistIncDTO {
    private static final long serialVersionUID = 1L;
    private String idHdlBlacklistInc;
    private String handleSn;
    private String submitSn;
    private String idNo;
    private String idType;
    private String userName;
    private String dataBuildTime;
    private String money;
    private String cardNo;
    private String qqNo;
    private String phoneNo;
    private String currency;
    private String grade;
    private String sourceId;
    private Date dateHandle;
    // 客户请求SEQ_NO,非数据库字段
    private String clientSeqNo;
    // GBD对应的STATE,非数据库字段
    private String state;

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getClientSeqNo() {
        return this.clientSeqNo;
    }

    public void setClientSeqNo(String clientSeqNo) {
        this.clientSeqNo = clientSeqNo;
    }

    public String getIdHdlBlacklistInc() {
        return this.idHdlBlacklistInc;
    }

    public void setIdHdlBlacklistInc(String idHdlBlacklistInc) {
        this.idHdlBlacklistInc = idHdlBlacklistInc;
    }

    public String getHandleSn() {
        return this.handleSn;
    }

    public void setHandleSn(String handleSn) {
        this.handleSn = handleSn;
    }

    public String getSubmitSn() {
        return this.submitSn;
    }

    public void setSubmitSn(String submitSn) {
        this.submitSn = submitSn;
    }

    public String getIdNo() {
        return this.idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdType() {
        return this.idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDataBuildTime() {
        return this.dataBuildTime;
    }

    public void setDataBuildTime(String dataBuildTime) {
        this.dataBuildTime = dataBuildTime;
    }

    public String getMoney() {
        return this.money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCardNo() {
        return this.cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getQqNo() {
        return this.qqNo;
    }

    public void setQqNo(String qqNo) {
        this.qqNo = qqNo;
    }

    public String getPhoneNo() {
        return this.phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Date getDateHandle() {
        return this.dateHandle;
    }

    public void setDateHandle(Date dateHandle) {
        this.dateHandle = dateHandle;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("equals..");
        if (obj == null) {
            return super.equals(obj);
        }
        HdlBlacklistIncDTO tmp = (HdlBlacklistIncDTO) obj;
        System.out.println(tmp.getUserName());
        boolean result = tmp.getUserName().equals(this.userName);
        if (result) {// 成功返回数据对象
            this.clientSeqNo = tmp.getClientSeqNo();
        }
        return result;
    }

    @Override
    public int hashCode() {
        System.out.println("hashCode..");
        return 888;
    }
}
