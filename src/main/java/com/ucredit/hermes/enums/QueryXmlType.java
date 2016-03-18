/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * @author caoming
 */
public enum QueryXmlType {
    CORP_NAME("corpName", "企业名称"),
    ORG_CODE("orgCode", "企业组织机构代码"),
    REGISTER_NO("registerNo", "企业工商注册号"),
    PERSON_NAME("name", "查询人姓名"),
    DOCUMENT_NO("documentNo", "查询人证件号码"),
    //学历信息查询
    LEVEL_NO("levelNo", "学历证编号"),
    GRADUATE_YEAR("graduateYear", "毕业年份"),
    COLLEGE("college", "毕业院校"),
    //学历核查
    GRADUATE_TIME("graduateTime", "毕业年份"),
    COLLEGE_LEVEL("collegeLevel", "学历层次"),
    //学籍核查
    START_TIME("startTime", "入学年份"),

    ;

    private final String name;
    private final String value;

    public String getValue() {
        return this.value;
    }

    private QueryXmlType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
