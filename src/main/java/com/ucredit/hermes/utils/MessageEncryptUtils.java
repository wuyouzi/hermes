/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * MD5算法
 *
 * @author liuqianqian
 */
public class MessageEncryptUtils {

    /**
     * List排序后转为String
     *
     * @param list
     *        :需要转换的List
     * @return String转换后的字符串
     */
    public static String sortListToString(List<String> list) {
        StringBuffer sb = new StringBuffer();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1 == null && o2 == null ? 0 : o1 == null ? -1
                    : o2 == null ? 1 : o1.compareTo(o2);
            }
        });

        for (String string : list) {
            if (StringUtils.isNotBlank(string)) {
                sb.append(string);
            }
        }

        return sb.toString();
    }
}
