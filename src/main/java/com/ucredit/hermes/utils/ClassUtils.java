/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import java.lang.reflect.ParameterizedType;

/**
 * @author jay
 */
public class ClassUtils {
    public static Class<?> getActualGenericType(Class<?> genericType,
            int typeIndex) {
        return (Class<?>) ((ParameterizedType) genericType
            .getGenericSuperclass()).getActualTypeArguments()[typeIndex];
    }
}
