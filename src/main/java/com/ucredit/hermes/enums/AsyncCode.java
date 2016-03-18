package com.ucredit.hermes.enums;

public enum AsyncCode {
    SUCCESS("00"),

    /**
     * 一般故障
     */
    FAILURE("01"),

    /**
     * 空数据
     */
    FAILURE_EMPTYDATA("02"),
    /**
     * 脏数据
     */
    FAILURE_DIRTYDATA("03"),
    /**
     * 无法连接到第三方
     */
    FAILURE_CONNECTION_REFUSED("04"),
    /**
     * 第三方故障
     */
    FAILURE_3RD_ERROR("05"),
    /**
     * 第三方返回异常
     */
    FAILURE_3RD_RETURN_ERROR("06"),
    /**
     * hermes故障
     */
    FAILURE_HERMES_ERROR("07"),
    /**
     * hermes解析故障
     */
    FAILURE_HERMES_ERROR_PARSE("08"),
    /**
     * MQ故障
     */
    FAILURE_MQ_ERROR("09"),
    /**
     * 未知故障
     */
    FAILURE_UNEXCEPTED("0A"),
    /**
     * TOKEN故障
     */
    FAILURE_TOKEN_ERROR("0B"),

    /**
     * 查询参数不匹配
     */
    RESPONSE_NOT_MATHCING("11"),
    /**
     * 无此数据
     */
    RESPONSE_NO_DETAILS("12"),

    UN_GRANT_AUTHORIZATION("13"),

    SING_ERROR("14");

    private final String string;

    private AsyncCode(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

    public String getString() {
        return this.string;
    }
}
