/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author jay
 */
public class MessageEncryptUtilsTest {

    /**
     * Test method for
     * {@link com.ucredit.hermes.utils.MessageEncryptUtils#sortListToString(java.util.List)}
     * .
     */
    @Test
    public void testSortListToString() {
        List<String> l = Arrays.asList(null, "", "a", "c", "b", null, "");
        String s = MessageEncryptUtils.sortListToString(l);

        Assert.assertEquals("abc", s);
    }

}
