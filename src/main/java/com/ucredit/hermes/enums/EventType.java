package com.ucredit.hermes.enums;

/**
 * 同盾 事件类型
 *
 * @author zhouwuyuan
 */
public enum EventType {
    //用户登录场景
    Login("Login"),
    //用户注册场景
    Register("Register"),
    //交易场景
    Trade("Trade"),
    //支付场景
    Payment("Payment"),
    //退款场景
    Refund("Refund"),
    //借款场景
    Loan("Loan"),
    //转账场景
    Transfer("Transfer"),
    //提现场景
    Withdraw("Withdraw"),
    //修改重要资料或敏感信息等场景
    Modify("Modify"),
    //点击欺诈场景中的点击事件
    Click("Click"),
    //点击欺诈场景中的激活事件
    Activate("Activate"),
    //在以上事件类型无法满足的情况下,通过建立事件标示代替
    Other("Other");
    private final String string;

    private EventType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

}
