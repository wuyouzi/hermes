/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.model.yinlianzh.YinlianzhMerchantBillBaseInfo;

/**
 * @author caoming
 */
public class ThirdQueueTaskResult implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2974701682167354168L;

    private String name;
    private String number;
    private String code;
    private String levelNo;
    private int graduateYear;
    private String college;
    private JMSType type;
    private String fileUuid;
    private String params;
    private String searchType;
    private String systemId;
    private String lendRequestId;
    private List<String> fileUuids;
    private YinlianzhMerchantBillBaseInfo merchantBillBaseInfo;
    private String searchMids;

    private String unitName;
    private String queryUserID;
    private Date newReceiveTime;
    private String idcard;
    private String phone;
    private String userName;
    private String ip;
    private Integer fkId;
    private Integer reportNum;

    private String home_tel;
    private String home_addr;
    private String work_tel;
    private String couple_phone_num;
    private String contact_list;
    private String keyid;
    /**
     * 记录发送请求时的refID
     */
    private String refId;

    public ThirdQueueTaskResult() {
    }

    public ThirdQueueTaskResult(String fileUuid, JMSType type, String systemId) {
        this.fileUuid = fileUuid;
        this.type = type;
        this.systemId = systemId;
    }

    public ThirdQueueTaskResult(
            YinlianzhMerchantBillBaseInfo merchantBillBaseInfo, JMSType type,
            String systemId) {
        this.merchantBillBaseInfo = merchantBillBaseInfo;
        this.type = type;
        this.systemId = systemId;
    }

    public ThirdQueueTaskResult(String fileUuid, String lendRequestId,
            String searchType) {
        this.fileUuid = fileUuid;
        this.searchType = searchType;
        this.lendRequestId = lendRequestId;
    }

    public ThirdQueueTaskResult(String params, String searchType) {
        this.params = params;
        this.searchType = searchType;
    }

    public ThirdQueueTaskResult(List<String> fileUuids, JMSType type,
            String systemId) {
        this.type = type;
        this.systemId = systemId;
        this.fileUuids = fileUuids;
    }

    public String getFileUuid() {
        return this.fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;

    }

    public ThirdQueueTaskResult(String companyName, String orgCode,
            String registerNo, JMSType type) {
        this.name = companyName;
        this.number = orgCode;
        this.code = registerNo;
        this.type = type;
    }

    public ThirdQueueTaskResult(String name, String number, String unitName,
            String queryUserID, Date newReceiveTime, String levelNo,
            int graduateYear, String college, JMSType type) {
        this.name = name;
        this.number = number;
        this.unitName = unitName;
        this.queryUserID = queryUserID;
        this.newReceiveTime = newReceiveTime;
        this.levelNo = levelNo;
        this.graduateYear = graduateYear;
        this.college = college;
        this.type = type;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getSearchType() {
        return this.searchType;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public JMSType getType() {
        return this.type;
    }

    public void setType(JMSType type) {
        this.type = type;
    }

    public String getLevelNo() {
        return this.levelNo;
    }

    public void setLevelNo(String levelNo) {
        this.levelNo = levelNo;
    }

    public int getGraduateYear() {
        return this.graduateYear;
    }

    public void setGraduateYear(int graduateYear) {
        this.graduateYear = graduateYear;
    }

    public String getCollege() {
        return this.college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRefId() {
        return this.refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getQueryUserID() {
        return this.queryUserID;
    }

    public void setQueryUserID(String queryUserID) {
        this.queryUserID = queryUserID;
    }

    public Date getNewReceiveTime() {
        return this.newReceiveTime;
    }

    public void setNewReceiveTime(Date newReceiveTime) {
        this.newReceiveTime = newReceiveTime;
    }

    public String getIdcard() {
        return this.idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getFkId() {
        return this.fkId;
    }

    public void setFkId(Integer fkId) {
        this.fkId = fkId;
    }

    public Integer getReportNum() {
        return this.reportNum;
    }

    public void setReportNum(Integer reportNum) {
        this.reportNum = reportNum;
    }

    public String getHome_tel() {
        return this.home_tel;
    }

    public void setHome_tel(String home_tel) {
        this.home_tel = home_tel;
    }

    public String getHome_addr() {
        return this.home_addr;
    }

    public void setHome_addr(String home_addr) {
        this.home_addr = home_addr;
    }

    public String getWork_tel() {
        return this.work_tel;
    }

    public void setWork_tel(String work_tel) {
        this.work_tel = work_tel;
    }

    public String getCouple_phone_num() {
        return this.couple_phone_num;
    }

    public void setCouple_phone_num(String couple_phone_num) {
        this.couple_phone_num = couple_phone_num;
    }

    public String getContact_list() {
        return this.contact_list;
    }

    public void setContact_list(String contact_list) {
        this.contact_list = contact_list;
    }

    public String getKeyid() {
        return this.keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getLendRequestId() {
        return this.lendRequestId;
    }

    public void setLendRequestId(String lendRequestId) {
        this.lendRequestId = lendRequestId;
    }

    public List<String> getFileUuids() {
        return this.fileUuids;
    }

    public void setFileUuids(List<String> fileUuids) {
        this.fileUuids = fileUuids;
    }

    public YinlianzhMerchantBillBaseInfo getMerchantBillBaseInfo() {
        return this.merchantBillBaseInfo;
    }

    public void setMerchantBillBaseInfo(
            YinlianzhMerchantBillBaseInfo merchantBillBaseInfo) {
        this.merchantBillBaseInfo = merchantBillBaseInfo;
    }

    public String getSearchMids() {
        return this.searchMids;
    }

    public void setSearchMids(String searchMids) {
        this.searchMids = searchMids;

    }

}
