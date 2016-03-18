/**
 * Copyright(c) 2011-2011 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.ucredit.hermes.utils.SpELUtils;

/**
 * @author ijay
 * @param <T>
 */
public abstract class NonEqualsBaseModel<T> implements Cloneable, Serializable {
    private static final long serialVersionUID = -3613939348089186582L;

    @Override
    public T clone() {
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * 将对象以map形式转换为VO
     *
     * @param escapeDot
     *        若为<code>true</code>，将键中的"."替换为"_"
     * @param propertyNames
     *        需转换的属性名数组，每组可含1或2个字符串，其中[0]固定为遵循SpEL的属性名。
     *        [1]为可选，当出现时将作为map中的键值，否则使用[0]作为键值。
     *        最终map中的键值会过滤以下基本的SpEL功能字符：
     *        ?, (, )
     *        若出现propertyNames，返回的map将按propertyNames有序
     * @return
     */
    public Map<String, Object> wrapVoMap(boolean escapeDot,
            String[]... propertyNames) {
        Map<String, Object> map = new LinkedHashMap<>();

        for (String[] name : propertyNames) {
            // remove SpEL notations
            String key = (name.length == 1 ? name[0] : name[1]).replaceAll(
                "[\\?\\(\\)]", "");
            if (escapeDot) {
                key = key.replace('.', '_');
            }
            map.put(key, SpELUtils.extractProperty(this, name[0]));
        }

        return map;
    }

    /**
     * {@link #wrapVoMap(boolean, String[][])} with escapeDot = false
     *
     * @param propertyNames
     * @return
     */
    public Map<String, Object> wrapVoMap(String[]... propertyNames) {
        return this.wrapVoMap(false, propertyNames);
    }

    /**
     * @param propertyNames
     * @return
     */
    public Map<String, Object> wrapVoMap(String... propertyNames) {
        List<String[]> strings = new LinkedList<>();
        for (String s : propertyNames) {
            strings.add(new String[] { s });
        }

        return this.wrapVoMap(false,
            strings.toArray(new String[strings.size()]));
    }
}
