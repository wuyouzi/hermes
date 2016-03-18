package com.ucredit.hermes.utils.qianhai;

public class MessageUtil {
    public static String getMHeader() {
        StringBuffer sb = new StringBuffer(
                "{\"orgCode\":\"3\",\"chnlId\":\"qhcs-dcs\",\"transNo\":\"Tran001\",\"transDate\":\"2015-02-02 14:12:14\",\"authCode\":\"P2P001A2_123\",\"authDate\":\"2015-12-02 14:12:14\"}");
        return sb.toString();
    }

    /**
     * @param orgCode
     *        机构代码
     * @param chnlId
     *        渠道、系统ID
     * @param transNo
     *        交易流水号 该流水号只能使用一次
     * @param transDate
     *        交易时间 yyyy-MM-dd HH:mm:ss
     * @param authCode
     *        授权代码
     * @param authDate
     *        授权时间
     * @return
     */
    public static String getMHeader_DMZ(String orgCode, String chnlId,
            String transNo, String transDate, String authCode, String authDate) {
        StringBuffer sb = new StringBuffer("{\"orgCode\":\"" + orgCode
            + "\",\"chnlId\":\"" + chnlId + "\",\"transNo\":\"" + transNo
            + "\",\"transDate\":\"" + transDate + "\",\"authCode\":\""
            + authCode + "\",\"authDate\":\"" + authDate + "\"}");
        return sb.toString();
    }

    /**
     * @param batchNo
     *        批次号 由数字+字母组成
     * @param reasonCode
     *        查询原因
     * @param idNo
     *        证件号码
     * @param idType
     *        证件类型
     * @param name
     *        主体名称
     * @param seqNo
     *        序列号 子批次号，本批次内唯一
     * @return
     */
    public static String getBusiData(String batchNo, String reasonCode,
            String idNo, String idType, String name, String seqNo) {
        StringBuffer sb = new StringBuffer("{\"batchNo\":\"" + batchNo
            + "\",\"records\":[{\"reasonCode\":\"" + reasonCode
            + "\",\"idNo\":\"" + idNo + "\",\"idType\":\"" + idType
            + "\",\"name\":\"" + name + "\",\"seqNo\":\"" + seqNo + "\"}]}");
        return sb.toString();
    }

    /**
     * @param batchNo
     *        批次号 由数字+字母组成
     * @param reasonCode
     *        查询原因
     * @param idNo
     *        证件号码
     * @param idType
     *        证件类型
     * @param name
     *        主体名称
     * @param seqNo
     *        序列号 子批次号，本批次内唯一
     * @return
     */
    public static String getBusiData_MSC8004(String batchNo, String reasonCode,
            String idNo, String idType, String name, String seqNo) {
        StringBuffer sb = new StringBuffer("{\"batchNo\":\"" + batchNo
            + "\",\"records\":[{\"reasonCode\":\"" + reasonCode
            + "\",\"idNo\":\"" + idNo + "\",\"idType\":\"" + idType
            + "\",\"name\":\"" + name + "\",\"seqNo\":\"" + seqNo + "\"}]}");
        return sb.toString();
    }

    /**
     * @param signatureValue
     *        签名
     * @param pwd
     *        密码
     * @return
     */
    public static String getSecurityInfo(String signatureValue, String pwd,
            String userName) {
        StringBuffer sb = new StringBuffer("{\"signatureValue\":\""
                + signatureValue + "\",\"userName\":\"" + userName
            + "\",\"userPassword\":\"" + pwd + "\"}");
        return sb.toString();
    }
}
