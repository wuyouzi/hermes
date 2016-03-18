package com.ucredit.hermes.model.yinlianzh;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("merChantBills")
public class MerchantBills {
    private String resCode;
    private String resMsg;
    private Long totalReceipt;
    private Long totalExpense;
    private Integer totalCount;
    private String fileUuid;
    private List<MerchantTransaction> transactionsInfo;
    private String sign;

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

    public Long getTotalReceipt() {
        return this.totalReceipt;
    }

    public void setTotalReceipt(Long totalReceipt) {
        this.totalReceipt = totalReceipt;
    }

    public Long getTotalExpense() {
        return this.totalExpense;
    }

    public void setTotalExpense(Long totalExpense) {
        this.totalExpense = totalExpense;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getFileUuid() {
        return this.fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public List<MerchantTransaction> getTransactionsInfo() {
        return this.transactionsInfo;
    }

    public void setTransactionsInfo(List<MerchantTransaction> transactionsInfo) {
        this.transactionsInfo = transactionsInfo;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
