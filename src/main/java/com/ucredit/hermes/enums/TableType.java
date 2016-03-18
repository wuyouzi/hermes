/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * @author caoming
 */
public enum TableType {
    /**
     * 企业基本信息
     */
    COMPANY_INFOS("company_infos"),

    /**
     * 企业股东信息
     */
    COMPANY_SHAREHOLDER_INFOS("company_shareholder_infos"),

    /**
     * 企业高管信息
     */
    COMPANY_MANAGER_INFOS("company_manager_infos"),

    /**
     * 企业对外投资信息
     */
    COMPANY_OTHER_SHAREHOLDER_INFOS("company_other_shareholder_infos"),

    /**
     * 法定代表人在其他机构任职信息
     */
    LEGAL_OTHERMANAGER_INFOS("legal_otherManager_infos"),

    /**
     * 法定代表人股权投资信息
     */
    LEGAL_OTHER_SHAREHOLDER_INFOS("legal_other_shareholder_infos"),

    /**
     * 企业注册和经营地址及电话信息
     */
    COMPANY_CONTACT_OPERATE_INFOS("company_contact_operate_infos"),

    /**
     * 企业法院被执行信息
     */
    COMPANY_COURT_INFOS("company_court_infos"),

    /**
     * 个人户籍信息
     */
    POLICE_RESIDENCE_INFOS("police_residence_infoa"),

    /**
     * 个人学历信息
     */
    EDUCATION_INFOS("educational_infos"),

    /**
     * 个人学籍信息
     */
    EDUCATION_IN_SCHOOL_INFOS("education_in_school_infos"),

    /**
     * 个人社保信息
     */
    GD_SI_PERSON_INFOS("gd_si_person_infos");

    private final String string;

    private TableType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
