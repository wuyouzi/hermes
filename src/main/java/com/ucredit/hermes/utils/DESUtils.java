package com.ucredit.hermes.utils;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import com.ucredit.hermes.common.HermesConsts;

/**
 * 国政通提供且建议 DES 加密解密方式
 *
 * @author caoming
 */
public class DESUtils {

    /**
     * 加密算法
     */
    private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    public static String encode(String key, byte[] data) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // key的长度不能够小于8位字节
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DESUtils.ALGORITHM_DES);
        IvParameterSpec iv = new IvParameterSpec(
            HermesConsts.GZT_IP_KEY.getBytes()); // 向量
        AlgorithmParameterSpec paramSpec = iv;
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
        byte[] bytes = cipher.doFinal(data);
        return new String(Base64.encodeBase64(bytes), "GB18030");
    }

    public static String decode(String key, byte[] data) throws Exception {
        data = Base64.decodeBase64(data);
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // key 的长度不能够小于 8 位字节
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DESUtils.ALGORITHM_DES);
        IvParameterSpec iv = new IvParameterSpec(
            HermesConsts.GZT_IP_KEY.getBytes());
        AlgorithmParameterSpec paramSpec = iv;
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
        return new String(cipher.doFinal(data), "GB18030");
    }
}
