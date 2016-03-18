package com.ucredit.hermes.enums;

public enum BaiRongConsumptionTypes {

    THREE_MONTH("过去3个月（自然月，不含当月）"),
    SIX_MONTH("过去6个月（自然月，不含当月）"),
    TWELVE_MONTH("过去12个月（自然月，不含当月）"),
    LEVEL(
            "当前类目下的消费级别（根据过去12月的总消费金额对当前类目下所有用户（有浏览记录的）从小到大排名，该值=排名/参与排名用户数量，小数点后保留4位数字，存在并列情况）");
    private final String string;

    private BaiRongConsumptionTypes(String string) {
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
