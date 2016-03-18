package com.ucredit.hermes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 身份证号工具类
 */
public class IdentificationCodeUtil {
    private static final int[] WEIGHT = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
        10, 5, 8, 4, 2 };
    private static final char[] CHECKSUM = { '1', '0', 'X', '9', '8', '7', '6',
        '5', '4', '3', '2' };

    /**
     * @param century
     *        19xx 年用 19，20xx 年用 20
     * @param idCardNo15
     *        待转换的 15 位身份证号码
     * @return
     */
    public static String from15to18(int century, String idCardNo15) {

        String centuryStr = "" + century;
        if (century < 0 || centuryStr.length() != 2) {
            throw new IllegalArgumentException("世纪数无效！应该是两位的正整数。");
        }
        if (!(IdentificationCodeUtil.isIdCardNo(idCardNo15) && idCardNo15
            .length() == 15)) {
            throw new IllegalArgumentException("旧的身份证号格式不正确！");
        }

        int[] weight = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
            8, 4, 2, 1 };

        // 通过加入世纪码, 变成 17 为的新号码本体.
        String newNoBody = idCardNo15.substring(0, 6) + centuryStr
            + idCardNo15.substring(6);

        //下面算最后一位校验码

        int checkSum = 0;
        for (int i = 0; i < 17; i++) {
            int ai = Integer.parseInt("" + newNoBody.charAt(i)); // 位于 i 位置的数值
            checkSum = checkSum + ai * weight[i];
        }

        int checkNum = checkSum % 11;
        String checkChar = null;

        switch (checkNum) {
            case 0:
                checkChar = "1";
                break;
            case 1:
                checkChar = "0";
                break;
            case 2:
                checkChar = "X";
                break;
            default:
                checkChar = "" + (12 - checkNum);
        }

        return newNoBody + checkChar;

    }

    public static String from18to15(String idCardNo18) {

        if (!(IdentificationCodeUtil.isIdCardNo(idCardNo18) && idCardNo18
            .length() == 18)) {
            throw new IllegalArgumentException("身份证号参数格式不正确！");
        }

        return idCardNo18.substring(0, 6) + idCardNo18.substring(8, 17);
    }

    /**
     * 判断给定的字符串是不是符合身份证号的要求
     *
     * @param str
     * @return
     */
    public static boolean isIdCardNo(String str) {

        if (str == null) {
            return false;
        }

        int len = str.length();
        if (len != 15 && len != 18) {
            return false;
        }

        for (int i = 0; i < len-1; i++) {
            try {
                Integer.parseInt("" + str.charAt(i));
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    /**
     * 从THREAD里拿来的验证方法
     * 
     * @param id
     * @return
     */
    public static boolean verifyIDNumber(String id) {
        id = StringUtils.trim(id);
        if (StringUtils.isBlank(id)) {
            return false;
        }

        // 只校验18位
        if (id.length() == 18) {
            String date = id.substring(6, 14);
            boolean flag = true;
            try {
                //判断日期
                Date birthDay = DateUtils.parseDate(date, "yyyyMMdd");
                if (birthDay.after(new Date())) {
                    flag = false;
                }
            } catch (ParseException e) {
                flag = false;
            }
            if (!flag) {
                return false;
            }

            char checksum = IdentificationCodeUtil.generateChecksum(id);
            return Character.toUpperCase(id.charAt(17)) == checksum;
        } else {
            return id.length() == 15;
        }
    }

    public static char generateChecksum(String idNo) {
        int sum = 0;
        char[] ids = idNo.toCharArray();

        for (int i = 0; i < 17; i++) {
            int n = ids[i] - '0';
            sum += n * IdentificationCodeUtil.WEIGHT[i];
        }

        return IdentificationCodeUtil.CHECKSUM[sum % 11];
    }

    public static Date getBirthday(String idNo) {
        idNo = StringUtils.trim(idNo);
        if (idNo.length() < 14) {
            return new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        if (idNo.length() == 15) {
            String dateStr = "19" + idNo.substring(6, 12);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        } else if (idNo.length() == 18) {
            String dateStr = idNo.substring(6, 14);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        } else {
            return null;
        }
    }
}
