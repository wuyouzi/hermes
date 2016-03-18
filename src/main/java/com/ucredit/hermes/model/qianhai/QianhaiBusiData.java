package com.ucredit.hermes.model.qianhai;

import java.util.List;

/**
 * busiData信息
 * 
 * @author zhouwuyuan
 */
public class QianhaiBusiData {
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 具体记录
     */
    private List<QianhaiRecords> records;

    public String getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public List<QianhaiRecords> getRecords() {
        return this.records;
    }

    public void setRecords(List<QianhaiRecords> records) {
        this.records = records;
    }

}
