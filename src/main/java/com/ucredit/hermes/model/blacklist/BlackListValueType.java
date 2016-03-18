/**
 * Copyright(c) 2011-2015 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model.blacklist;

/**
 * @author ijay
 */
public enum BlackListValueType {
    NAME("姓名", "个人"),
    PHONE("手机", "电话"),
    ID_NO("身份证", "个人"),
    HOME_TELEPHONE("家庭电话", "电话"),
    WORK_TELEPHONE("工作电话", "电话"),
    COMPANY_NAME("名称", "企业");

    private final String purpose;

    private final String category;

    private BlackListValueType(String purpose, String category) {
        this.purpose = purpose;
        this.category = category;
    }

    /**
     * @return the purpose
     */
    public String getPurpose() {
        return this.purpose;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return this.category;
    }

    @Override
    public String toString() {
        return this.purpose;
    }

    /**
     * value 转化为 枚举值
     *
     * @param value1
     * @param value2
     * @return
     */
    public static BlackListValueType valueConverEnumName(String value1,
            String value2) {
        if ("个人".equals(value1)) {
            if ("姓名".equals(value2)) {
                return BlackListValueType.NAME;
            } else if ("身份证".equals(value2)) {
                return BlackListValueType.ID_NO;
            }
        } else if ("电话".equals(value1)) {
            if ("手机".equals(value2)) {
                return BlackListValueType.PHONE;
            } else if ("家庭".equals(value2)) {
                return BlackListValueType.HOME_TELEPHONE;
            } else if ("单位".equals(value2)) {
                return BlackListValueType.WORK_TELEPHONE;
            }
        } else if ("企业".equals(value1) && "名称".equals(value2)) {
            return BlackListValueType.COMPANY_NAME;
        }
        return null;
    }
}
