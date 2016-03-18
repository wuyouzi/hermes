package com.ucredit.hermes.enums;

public enum BaiRongResultCode {

    BaiRong_00("操作成功"),

    BaiRong_100001("百融程序错误"),

    BaiRong_100002("匹配结果为空"),

    BaiRong_100003("缺少必选key值"),

    BaiRong_100004("商户不存在"),

    BaiRong_100005("登陆密码不正确"),

    BaiRong_100006("请求参数格式错误"),

    BaiRong_100007("Tokenid过期"),

    BaiRong_100008("客户端api调用码不能为空"),

    BaiRong_100009("Ip地址错误"),

    BaiRong_1000010("超出当天访问次数"),

    YouXin_1000001("网络问题"),

    YouXin_1000002("身份证号,手机,邮箱至少要有一个"),

    YouXin_1000003("JMS未启动"),

    YouXin_1000004("学历信息参数不正确"),

    YouXin_1000005("职位参数不正确"),

    YouXin_1000006("单位性质参数不正确"),

    YouXin_HOUSE_TYPE_ERROR("住房性质参数不正确"),
    // YouXin_APPLY_SOURCE_ERROR("申请渠道参数正确"),
    YouXin_Marriage_ERROR("婚姻状况参数不正确"),

    YouXin_Biz_INDUSTRY_ERROR("单位所属行业参数不正确"),

    YouXin_IDCARD_ERROR("身份证号错误"),

    YouXin_PHONE_ERROR("电话号码错误"),

    YouXin_MAIL_ERROR("邮箱格式错误"),

    YouXin_ERROR("待确定错误"),

    YouXin_GET_NULL_BY_ID_ERROR("根据ID没有查询到数据"),

    YouXin_DB_ERROR("hermesDB出错"),

    YouXin_Hermes_ERROR("hermes出错");

    private final String string;

    private BaiRongResultCode(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

    public String getString() {
        return this.string;
    }

//	public static void main(String[] args) {
//		BaiRongResultCode[] enums = BaiRongResultCode.values();
//		for (BaiRongResultCode item : enums) {
//			System.out.println(item.getString() + " name=" + item.name());
//		}
//	}

}
