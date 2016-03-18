package com.ucredit.hermes.utils;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ucredit.hermes.model.tongdun.Condition;
import com.ucredit.hermes.model.tongdun.TongdunFraudRecord;

public class TongDunUtils {
    private static Logger logger = LoggerFactory.getLogger(TongDunUtils.class);

    /**
     * 对合作方和应用密钥进行加密产生token<br>
     * 使用的时候服务器时间必须跟互联网时间是同步的且时区是中国(+8)
     *
     * @param partnerCode
     *        合作方
     * @param secretKey
     *        应用密钥
     * @return 对合作方和应用密钥加密产生的token
     * @throws NoSuchAlgorithmException
     * @throws IllegalArgumentException
     *         合作方或者应用密钥为blank
     */
    public static String generateEncodedToken(String partnerCode,
            String appName, String appSecretKey, long timestamp)
                    throws NoSuchAlgorithmException {
        if (StringUtils.isBlank(partnerCode) || StringUtils.isBlank(appName)
            || StringUtils.isBlank(appSecretKey)) {
            throw new IllegalArgumentException("合作方,应用名和应用密钥必填");
        }

        TongDunUtils.logger.info("timestamp is :{} ", timestamp);

        StringBuilder basestr = new StringBuilder();
        basestr.append("partner_code=").append(partnerCode).append("|appName=")
            .append(appName).append("|timestamp=").append(timestamp)
            .append('|').append(appSecretKey);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(basestr.toString().getBytes());
        byte b[] = md.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (byte element : b) {
            i = element;
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append('0');
            }
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    /**
     * 将对象转换成参数
     *
     * @param condition
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static String convertToParams(Condition condition)
            throws IllegalArgumentException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        Field[] fields = condition.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            Object value = f.get(condition);
            if (value != null) {
                String name = f.getName();
                sb.append(name + "=" + value + "&");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static TongdunFraudRecord getTongdunFraudRecord(Condition condition)
            throws IllegalArgumentException, IllegalAccessException,
            NoSuchFieldException, SecurityException {
        TongdunFraudRecord tf = new TongdunFraudRecord();
        Field[] conFields = condition.getClass().getDeclaredFields();
        for (Field f : conFields) {
            f.setAccessible(true);
            Object value = f.get(condition);
            if (value != null) {
                String name = f.getName();
                Field fieldData = tf.getClass().getDeclaredField(name);
                fieldData.setAccessible(true);
                fieldData.set(tf, value);
            }
        }
        return tf;
    }
}
