package com.ucredit.hermes.model.qianhai;

import javax.persistence.Lob;

/**
 * 调用前海返回的信息
 *
 * @author zhouwuyuan
 */
public class QianhaiResponseInfo {
    /**
     * 签名信息
     */
    private QianhaiSecurityInfo securityInfo;
    /**
     *
     */
    private String oriMessage;
    /**
     * 返回主要信息
     */
    @Lob
    private String busiData;
    /**
     * 返回头信息
     */
    private QianhaiHeader header;

    public QianhaiSecurityInfo getSecurityInfo() {
        return this.securityInfo;
    }

    public void setSecurityInfo(QianhaiSecurityInfo securityInfo) {
        this.securityInfo = securityInfo;
    }

    public String getOriMessage() {
        return this.oriMessage;
    }

    public void setOriMessage(String oriMessage) {
        this.oriMessage = oriMessage;
    }

    public String getBusiData() {
        return this.busiData;
    }

    public void setBusiData(String busiData) {
        this.busiData = busiData;
    }

    public QianhaiHeader getHeader() {
        return this.header;
    }

    public void setHeader(QianhaiHeader header) {
        this.header = header;
    }

}
