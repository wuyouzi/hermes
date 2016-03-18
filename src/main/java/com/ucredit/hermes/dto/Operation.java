package com.ucredit.hermes.dto;

public class Operation {
    private String fromSysId;
    private String fromSysName;
    private String toSysId;
    private String toSysName;
    private String authToken;
    private String fromUserId;
    private String fromUserName;
    private String requestId;
    private String requestType;
    private String requestTime;
    private String responseTime;
    private String responseStatus;
    private String requestTimeOut;
    private String contentEncoding; // default

    public String getResponseStatus() {
        return this.responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getToSysId() {
        return this.toSysId;
    }

    public void setToSysId(String toSysId) {
        this.toSysId = toSysId;
    }

    public String getToSysName() {
        return this.toSysName;
    }

    public void setToSysName(String toSysName) {
        this.toSysName = toSysName;
    }

    public String getResponseTime() {
        return this.responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getFromSysId() {
        return this.fromSysId;
    }

    public void setFromSysId(String fromSysId) {
        this.fromSysId = fromSysId;
    }

    public String getFromSysName() {
        return this.fromSysName;
    }

    public void setFromSysName(String fromSysName) {
        this.fromSysName = fromSysName;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getFromUserId() {
        return this.fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return this.fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestTime() {
        return this.requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestTimeOut() {
        return this.requestTimeOut;
    }

    public void setRequestTimeOut(String requestTimeOut) {
        this.requestTimeOut = requestTimeOut;
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

}
