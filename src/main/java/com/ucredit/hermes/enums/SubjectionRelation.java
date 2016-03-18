package com.ucredit.hermes.enums;

/**
 * 隶属关系
 * 
 * @author caoming
 */
public enum SubjectionRelation {
    CENTRE("1", "中央"),
    PROVINCE("2", "省"),
    SEPARATELY_LISTED_CITY("3", "计划单列市"),
    CITY("4", "市"),
    COUNTY("5", "县"),
    TOWN("6", "乡镇"),
    TROOPS("7", "部队"),
    OTHER("9", "其它");

    private final String code;
    private final String string;

    private SubjectionRelation(String code, String string) {
        this.code = code;
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

    public String getCode() {
        return this.code;
    }
}
