/**
 * Copyright(c) 2011-2011 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.enums;

/**
 * 企业注册或经营
 *
 * @author liuqianqian
 */
public enum CompanyOperateType {
    /**
     * 注册
     */
    REGISTER("注册"),
    /**
     * 经营
     */
    OPERATE("经营");

    private final String string;

    public static CompanyOperateType getCompanyOperateType(String str) {
        if ("REGISTER".equals(str)) {
            return CompanyOperateType.REGISTER;
        } else {
            return CompanyOperateType.OPERATE;
        }
    }

    private CompanyOperateType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
