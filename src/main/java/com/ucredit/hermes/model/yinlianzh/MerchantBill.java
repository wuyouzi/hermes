package com.ucredit.hermes.model.yinlianzh;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("merChantBill")
public class MerchantBill {
    private String resCode;
    private String resMsg;
    private String sign;
    private String mName;
    private Integer posCount;
    private List<String> posIds;
    private String mcc;
    private String mid;
    private String city;
    private String address;
    private Long borrow;
    private String uuid;
    private Long lend;
    private Integer transCount;
    private List<String> data;
    private String firstTransDate;
    private String lastTransDate;

    public String getResCode() {
        return this.resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return this.resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getmName() {
        return this.mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
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

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
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

    public Long getBorrow() {
        return this.borrow;
    }

    public void setBorrow(Long borrow) {
        this.borrow = borrow;
    }

    public Long getLend() {
        return this.lend;
    }

    public void setLend(Long lend) {
        this.lend = lend;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getTransCount() {
        return this.transCount;
    }

    public void setTransCount(Integer transCount) {
        this.transCount = transCount;
    }

    public List<String> getData() {
        return this.data;
    }

    public void setData(List<String> data) {
        this.data = data;
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

}
