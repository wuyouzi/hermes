/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.common;

import java.io.Serializable;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author caoming
 */
@ManagedResource
public class Variables implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4809766459223411088L;

    private String pengYuanURL;
    private String pengYuanUsername;
    private String pengYuanPassword;
    private String guoZhengTongUsername;
    private String guoZhengTongPassword;

    private String baiRongUserName;
    private String baiRongPassword;

    private String anShengURL;

    private String juxinliUrl;
    private String juxinliClientSecret;
    private String juxinliAccessToken;
    private String juxinliVerifyUrl;

    private String yinlianzhihuiBillUrl;
    private String yinlianzhihuiBillsUrl;
    private String yinlianzhihuiDownloadUrl;
    private String yinlianzhAccount;
    private String yinlianzhPrivateKey;

    private String miguanClientSecret;
    private String miguanAccessToken;
    private String miguanUrl;

    private String tongdunUrl;
    private String tongdunPartnerCode;
    private String tongdunAndroidSecretKey;
    private String tongdunIosSecretKey;
    private String tongdunSecretKey;

    /**
     * 前海征信
     */
    private String qianhaiUrl;
    private String qianhaiOrgCode;
    private String qianhaiChnlId;
    private String qianhaiAuthCode;
    private String qianhaiUserName;
    private String qianhaiUserPassword;
    private String qianhaiPathCer;
    private String qianhaiPathJks;
    private String qianhaiAuthDate;
    private String qianhaiCheckCode;
    private String qianhaiJksAlias;
    private String qianhaiJksPassword;

    private String crawlCompanyUrl;
    private String crawlXuexinUrl;

    @ManagedAttribute
    public String getGuoZhengTongUsername() {
        return this.guoZhengTongUsername;
    }

    @ManagedAttribute
    public void setGuoZhengTongUsername(String guoZhengTongUsername) {
        this.guoZhengTongUsername = guoZhengTongUsername;
    }

    @ManagedAttribute
    public String getGuoZhengTongPassword() {
        return this.guoZhengTongPassword;
    }

    @ManagedAttribute
    public void setGuoZhengTongPassword(String guoZhengTongPassword) {
        this.guoZhengTongPassword = guoZhengTongPassword;
    }

    @ManagedAttribute
    public String getPengYuanURL() {
        return this.pengYuanURL;
    }

    @ManagedAttribute
    public void setPengYuanURL(String pengYuanURL) {
        this.pengYuanURL = pengYuanURL;
    }

    @ManagedAttribute
    public String getPengYuanUsername() {
        return this.pengYuanUsername;
    }

    @ManagedAttribute
    public void setPengYuanUsername(String pengYuanUsername) {
        this.pengYuanUsername = pengYuanUsername;
    }

    @ManagedAttribute
    public String getPengYuanPassword() {
        return this.pengYuanPassword;
    }

    @ManagedAttribute
    public void setPengYuanPassword(String pengYuanPassword) {
        this.pengYuanPassword = pengYuanPassword;
    }

    @ManagedAttribute
    public String getBaiRongUserName() {
        return this.baiRongUserName;
    }

    @ManagedAttribute
    public void setBaiRongUserName(String baiRongUserName) {
        this.baiRongUserName = baiRongUserName;
    }

    @ManagedAttribute
    public String getBaiRongPassword() {
        return this.baiRongPassword;
    }

    @ManagedAttribute
    public void setBaiRongPassword(String baiRongPassword) {
        this.baiRongPassword = baiRongPassword;
    }

    @ManagedAttribute
    public String getAnShengURL() {
        return this.anShengURL;
    }

    @ManagedAttribute
    public void setAnShengURL(String anShengURL) {
        this.anShengURL = anShengURL;
    }

    @ManagedAttribute
    public String getJuxinliUrl() {
        return this.juxinliUrl;
    }

    @ManagedAttribute
    public void setJuxinliUrl(String juxinliUrl) {
        this.juxinliUrl = juxinliUrl;
    }

    @ManagedAttribute
    public String getJuxinliClientSecret() {
        return this.juxinliClientSecret;
    }

    @ManagedAttribute
    public void setJuxinliClientSecret(String juxinliClientSecret) {
        this.juxinliClientSecret = juxinliClientSecret;
    }

    @ManagedAttribute
    public String getJuxinliAccessToken() {
        return this.juxinliAccessToken;
    }

    @ManagedAttribute
    public void setJuxinliAccessToken(String juxinliAccessToken) {
        this.juxinliAccessToken = juxinliAccessToken;
    }

    @ManagedAttribute
    public String getJuxinliVerifyUrl() {
        return this.juxinliVerifyUrl;
    }

    @ManagedAttribute
    public void setJuxinliVerifyUrl(String juxinliVerifyUrl) {
        this.juxinliVerifyUrl = juxinliVerifyUrl;
    }

    public String getYinlianzhihuiBillUrl() {
        return this.yinlianzhihuiBillUrl;
    }

    @ManagedAttribute
    public void setYinlianzhihuiBillUrl(String yinlianzhihuiBillUrl) {
        this.yinlianzhihuiBillUrl = yinlianzhihuiBillUrl;
    }

    @ManagedAttribute
    public String getYinlianzhihuiBillsUrl() {
        return this.yinlianzhihuiBillsUrl;
    }

    @ManagedAttribute
    public String getYinlianzhihuiDownloadUrl() {
        return this.yinlianzhihuiDownloadUrl;
    }

    @ManagedAttribute
    public void setYinlianzhihuiDownloadUrl(String yinlianzhihuiDownloadUrl) {
        this.yinlianzhihuiDownloadUrl = yinlianzhihuiDownloadUrl;
    }

    @ManagedAttribute
    public void setYinlianzhihuiBillsUrl(String yinlianzhihuiBillsUrl) {
        this.yinlianzhihuiBillsUrl = yinlianzhihuiBillsUrl;
    }

    @ManagedAttribute
    public String getYinlianzhAccount() {
        return this.yinlianzhAccount;
    }

    @ManagedAttribute
    public void setYinlianzhAccount(String yinlianzhAccount) {
        this.yinlianzhAccount = yinlianzhAccount;
    }

    @ManagedAttribute
    public String getYinlianzhPrivateKey() {
        return this.yinlianzhPrivateKey;
    }

    @ManagedAttribute
    public void setYinlianzhPrivateKey(String yinlianzhPrivateKey) {
        this.yinlianzhPrivateKey = yinlianzhPrivateKey;

    }

    @ManagedAttribute
    public String getMiguanClientSecret() {
        return this.miguanClientSecret;
    }

    @ManagedAttribute
    public void setMiguanClientSecret(String miguanClientSecret) {
        this.miguanClientSecret = miguanClientSecret;
    }

    @ManagedAttribute
    public String getMiguanAccessToken() {
        return this.miguanAccessToken;
    }

    @ManagedAttribute
    public void setMiguanAccessToken(String miguanAccessToken) {
        this.miguanAccessToken = miguanAccessToken;
    }

    @ManagedAttribute
    public String getMiguanUrl() {
        return this.miguanUrl;
    }

    @ManagedAttribute
    public void setMiguanUrl(String miguanUrl) {
        this.miguanUrl = miguanUrl;
    }

    public String getTongdunUrl() {
        return this.tongdunUrl;
    }

    @ManagedAttribute
    public void setTongdunUrl(String tongdunUrl) {
        this.tongdunUrl = tongdunUrl;
    }

    @ManagedAttribute
    public String getTongdunPartnerCode() {
        return this.tongdunPartnerCode;
    }

    @ManagedAttribute
    public void setTongdunPartnerCode(String tongdunPartnerCode) {
        this.tongdunPartnerCode = tongdunPartnerCode;
    }

    @ManagedAttribute
    public String getTongdunAndroidSecretKey() {
        return this.tongdunAndroidSecretKey;
    }

    @ManagedAttribute
    public void setTongdunAndroidSecretKey(String tongdunAndroidSecretKey) {
        this.tongdunAndroidSecretKey = tongdunAndroidSecretKey;
    }

    @ManagedAttribute
    public String getTongdunIosSecretKey() {
        return this.tongdunIosSecretKey;
    }

    @ManagedAttribute
    public void setTongdunIosSecretKey(String tongdunIosSecretKey) {
        this.tongdunIosSecretKey = tongdunIosSecretKey;
    }

    @ManagedAttribute
    public String getTongdunSecretKey() {
        return this.tongdunSecretKey;
    }

    @ManagedAttribute
    public void setTongdunSecretKey(String tongdunSecretKey) {
        this.tongdunSecretKey = tongdunSecretKey;

    }

    @ManagedAttribute
    public String getQianhaiUrl() {
        return this.qianhaiUrl;
    }

    @ManagedAttribute
    public void setQianhaiUrl(String qianhaiUrl) {
        this.qianhaiUrl = qianhaiUrl;
    }

    @ManagedAttribute
    public String getQianhaiOrgCode() {
        return this.qianhaiOrgCode;
    }

    @ManagedAttribute
    public void setQianhaiOrgCode(String qianhaiOrgCode) {
        this.qianhaiOrgCode = qianhaiOrgCode;
    }

    @ManagedAttribute
    public String getQianhaiChnlId() {
        return this.qianhaiChnlId;
    }

    @ManagedAttribute
    public void setQianhaiChnlId(String qianhaiChnlId) {
        this.qianhaiChnlId = qianhaiChnlId;
    }

    @ManagedAttribute
    public String getQianhaiAuthCode() {
        return this.qianhaiAuthCode;
    }

    @ManagedAttribute
    public void setQianhaiAuthCode(String qianhaiAuthCode) {
        this.qianhaiAuthCode = qianhaiAuthCode;
    }

    @ManagedAttribute
    public String getQianhaiUserName() {
        return this.qianhaiUserName;
    }

    @ManagedAttribute
    public void setQianhaiUserName(String qianhaiUserName) {
        this.qianhaiUserName = qianhaiUserName;
    }

    @ManagedAttribute
    public String getQianhaiUserPassword() {
        return this.qianhaiUserPassword;
    }

    @ManagedAttribute
    public void setQianhaiUserPassword(String qianhaiUserPassword) {
        this.qianhaiUserPassword = qianhaiUserPassword;
    }

    @ManagedAttribute
    public String getQianhaiPathCer() {
        return this.qianhaiPathCer;
    }

    @ManagedAttribute
    public void setQianhaiPathCer(String qianhaiPathCer) {
        this.qianhaiPathCer = qianhaiPathCer;
    }

    @ManagedAttribute
    public String getQianhaiAuthDate() {
        return this.qianhaiAuthDate;
    }

    @ManagedAttribute
    public void setQianhaiAuthDate(String qianhaiAuthDate) {
        this.qianhaiAuthDate = qianhaiAuthDate;
    }

    @ManagedAttribute
    public String getQianhaiCheckCode() {
        return this.qianhaiCheckCode;
    }

    @ManagedAttribute
    public void setQianhaiCheckCode(String qianhaiCheckCode) {
        this.qianhaiCheckCode = qianhaiCheckCode;
    }

    @ManagedAttribute
    public String getQianhaiPathJks() {
        return this.qianhaiPathJks;
    }

    @ManagedAttribute
    public void setQianhaiPathJks(String qianhaiPathJks) {
        this.qianhaiPathJks = qianhaiPathJks;
    }

    @ManagedAttribute
    public String getQianhaiJksAlias() {
        return this.qianhaiJksAlias;
    }

    @ManagedAttribute
    public void setQianhaiJksAlias(String qianhaiJksAlias) {
        this.qianhaiJksAlias = qianhaiJksAlias;
    }

    @ManagedAttribute
    public String getQianhaiJksPassword() {
        return this.qianhaiJksPassword;
    }

    @ManagedAttribute
    public void setQianhaiJksPassword(String qianhaiJksPassword) {
        this.qianhaiJksPassword = qianhaiJksPassword;
    }

    @ManagedAttribute
    public String getCrawlCompanyUrl() {
        return this.crawlCompanyUrl;
    }

    @ManagedAttribute
    public void setCrawlCompanyUrl(String crawlCompanyUrl) {
        this.crawlCompanyUrl = crawlCompanyUrl;
    }

    public String getCrawlXuexinUrl() {
        return this.crawlXuexinUrl;
    }

    @ManagedAttribute
    public void setCrawlXuexinUrl(String crawlXuexinUrl) {
        this.crawlXuexinUrl = crawlXuexinUrl;
    }

}