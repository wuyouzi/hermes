package com.ucredit.hermes.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUtils {

    public static boolean isIdcard(String idcard) {
        return idcard == null || "".equals(idcard) ? false : Pattern.matches(
            "(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)", idcard);
    }

    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static boolean isEmail(String email) {
        String str = "^([a-z0-9A-Z]+[-_\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-_[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
