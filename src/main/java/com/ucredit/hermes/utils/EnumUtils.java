/**
 * Copyright(c) 2011-2012 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ijay
 */
public class EnumUtils {

    /**
     * 根据 枚举值 获取枚举对象
     * 
     * @param <T>
     * @param type
     * @param code
     * @return
     */
    public static <T extends Enum<T>> T getEnumByCode(Class<T> type, String code) {
        T[] values = type.getEnumConstants();
        T t = null;
        for (T value : values) {
            if (value.toString().equals(code)) {
                t = value;
                break;
            }
        }
        return t;
    }

    /**
     * 字符串改为枚举
     * 
     * @param <T>
     * @param type
     * @param strings
     * @return
     */
    public static <T extends Enum<T>> List<T> getEnumsByNames(Class<T> type,
            List<String> strings) {
        if (strings == null) {
            return Collections.EMPTY_LIST;
        }

        List<T> list = new ArrayList<>(strings.size());

        for (String s : strings) {
            list.add(Enum.valueOf(type, s));
        }

        return list;
    }

    public static <T extends Enum<T>> T getEnumByOrdinal(Class<T> type,
            int ordinal) {
        T[] values = type.getEnumConstants();

        if (ordinal < 0 || ordinal >= values.length) {
            throw new IndexOutOfBoundsException("Invalid ordinal <" + ordinal
                + "> for " + type);
        }

        return values[ordinal];
    }

    public static <T extends Enum<T>> T getEnumByOrdinal(String className,
            int ordinal) throws ClassNotFoundException {
        return EnumUtils.getEnumByOrdinal((Class<T>) Class.forName(className),
            ordinal);
    }
}
