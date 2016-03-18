/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.common;

/**
 * @author caoming
 */
public interface HermesConsts {
    /**
     * 国政通类型
     */
    //身份证类型
    String GZT_QUERY_TYPE = "1A020201";
    //国政通编码方式
    /**
     * 国政通的编码方式
     */
    String GZT_CHARSET = "GB18030";
    /**
     * 国政通的DES密钥
     */
    String GZT_DES_KEY = "12345678";
    /**
     * 国政通的IP 向量密钥
     */
    String GZT_IP_KEY = "12345678";

    /**
     * 鹏元查询类型
     */
    String PENG_YUAN_QUERY_TYPE = "95001,90035";

    String PENG_YUAN_95001 = "95001";
    /**
     * 全国企业基本信息
     */
    String PENG_YUAN_95001_21301 = "21301";
    /**
     * 全国企业注册地址及电话信息
     */
    String PENG_YUAN_95001_21611 = "21611";
    /**
     * 企业股东信息
     */
    String PENG_YUAN_95001_22101 = "22101";
    /**
     * 企业高管信息
     */
    String PENG_YUAN_95001_22102 = "22102";

    String PENG_YUAN_90035 = "90035";
    /**
     * 全国企业经营地址及电话信息
     */
    String PENG_YUAN_90035_21612 = "21612";

    /**
     * 101 贷款审批 102 贷款贷后管理 103 贷款催收
     */
    String QUERY_REASON_TYPE = "101";

    String COMPANY_SYSTEM_ERROR = "hasSystemError为true，查询无效";

    /**
     * 异常返回类型
     */
    String HERMES_RESPONSE_ERROR = "第三方系统返回错误，请联系管理员！";
    /**
     * 国政通异常返回类型
     */
    String HERMES_GZT_RESPONSE_ERROR = "查询失败，请联系管理员或稍候重试！";

    String HERMES_EDUCATION_NOT_FOUND = "未查到此用户的学历信息";

    String HERMES_COMPANY_NOT_FOUND = "未查到此企业的内容，请确认后再次查询！";

    String HERMES_GZT_DATABASENOTFOUND = "1";

    String HERMES_GZT_NOTMATCH = "2";

    String HERMES_GZG_NOTFOUND = "1001";

    String HERMES_GZT_SEARCHFAILD = "1002";

    String HERMES_GZT_ERROR = "1003";

    String HERMES_CONNECT_ERROR = "2001";

    String HERMES_IDCARD_ERROR = "2002";
    String HERMES_NAME_ERROR = "2003";

    /**
     * 默认机构为友信
     */
    String DEFAULT_GROUP_NAME = "友信";

    /**
     * 专线所有参数
     */
    /**
     * 返回格式
     */
    String SPECIAL_FORMAT = ".xml";

    /**
     * 银联智惠下载商户交易流水失败信息
     */
    String YINLIANZH_FILE_NOT_FOUND = "文件下载失败，文件未找到或不存在";
    /**
     * 银联智惠下载商户交易流水文件正在生成信息
     */
    String YINLIANZH_FILE_DOWNLOADING = "文件正在生成，请稍后下载";

    String YINLIANZH_FILE_DOWNLOADSUCCESS = "文件下载成功";

    String MSG = "msg";
}
