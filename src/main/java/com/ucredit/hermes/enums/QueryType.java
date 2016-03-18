/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * @author caoming
 */
public enum QueryType {
    PERSON("个人基本信息"),
    PERSON_EDUCATION("个人学历信息"),
    CHECK_EDUCATION("核查学历信息"),
    CHECK_ROLL("核查学籍信息"),
    CHECK_COMPANY("企业信息"),
    CHECK_POLICE("个人户籍信息"),
    GDSI_PERSION("广东社保信息"),
    PERSON_EDUCATION_INSCHOOL("个人学籍信息");

    private final String name;

    private QueryType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
