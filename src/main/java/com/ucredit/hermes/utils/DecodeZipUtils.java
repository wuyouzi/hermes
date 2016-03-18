/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * @author caoming
 */
public class DecodeZipUtils {

    /**
     * 专线返回base64压缩的资源，进行解压缩
     *
     * @param sourceString
     * @return
     */
    public static String decodeZip(String sourceString) {

//        byte[] decodeBase64 = Base64.decodeBase64(sourceString);
        try (InputStream in = new ByteArrayInputStream(
            Base64.decodeBase64(sourceString.getBytes()));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ZipInputStream zin = new ZipInputStream(in);) {

            zin.getNextEntry();
            IOUtils.copy(zin, out);
            zin.closeEntry();
            zin.close();
            return new String(out.toByteArray(), "GBK");
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
