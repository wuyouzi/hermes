package com.ucredit.hermes.model.yinlianzh;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("merchantTransaction")
public class MerchantTransaction {
    private String mid;
    private String sMid;//加密后的mid，多个查询时返回
    private String name;
    private Integer posCount;
    private List<String> posIds;
    private String mcc;
    private String city;
    private String address;
    private Long receipt;
    private Long expense;
    private Integer transCount;
    private String firstTransDate;
    private String lastTransDate;
    private String fileUuid;
    private List<String> transactions;

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getsMid() {
        return this.sMid;
    }

    public void setsMid(String sMid) {
        this.sMid = sMid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosCount() {
        return this.posCount;
    }

    public void setPosCount(Integer posCount) {
        this.posCount = posCount;
    }

    public List<String> getPosIds() {
        return this.posIds;
    }

    public void setPosIds(List<String> posIds) {
        this.posIds = posIds;
    }

    public String getMcc() {
        return this.mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getReceipt() {
        return this.receipt;
    }

    public void setReceipt(Long receipt) {
        this.receipt = receipt;
    }

    public Long getExpense() {
        return this.expense;
    }

    public void setExpense(Long expense) {
        this.expense = expense;
    }

    public Integer getTransCount() {
        return this.transCount;
    }

    public void setTransCount(Integer transCount) {
        this.transCount = transCount;
    }

    public String getFirstTransDate() {
        return this.firstTransDate;
    }

    public void setFirstTransDate(String firstTransDate) {
        this.firstTransDate = firstTransDate;
    }

    public String getLastTransDate() {
        return this.lastTransDate;
    }

    public void setLastTransDate(String lastTransDate) {
        this.lastTransDate = lastTransDate;
    }

    public String getFileUuid() {
        return this.fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public List<String> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

}
