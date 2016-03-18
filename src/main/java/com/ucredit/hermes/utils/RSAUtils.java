/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author caoming
 */
public class RSAUtils {
    private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

//    /**
//     * 生成公钥和私钥
//     *
//     * @param password
//     * @return
//     * @throws NoSuchAlgorithmException
//     */
//    private static List<String> createKey() throws NoSuchAlgorithmException {
//        List<String> list = new ArrayList<>();
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator
//            .getInstance("RSA-1");
//        SecureRandom secureRandom = new SecureRandom();
//        secureRandom.setSeed("UCREDIT_SIGNATURE_INFO".getBytes());
//        keyPairGenerator.initialize(512, secureRandom);
//        KeyPair keyPair = keyPairGenerator.genKeyPair();
//        PrivateKey privateKey = keyPair.getPrivate();
//        PublicKey publicKey = keyPair.getPublic();
//        String privateKeyString = RSAUtils.bytesToHexStr(privateKey
//            .getEncoded());
//        String publicKeyString = RSAUtils.bytesToHexStr(publicKey.getEncoded());
//        list.add(privateKeyString);
//        list.add(publicKeyString);
//        return list;
//    }

    /**
     * 用私钥给信息加密，生成证书
     *
     * @param privateKey
     *        私钥
     * @param password
     *        需要加密的密码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    public static String signatureData(String privateKey, String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException,
            UnsupportedEncodingException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
            RSAUtils.hexStrToBytes(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA-1");
        PrivateKey myPrivateKey = keyFactory
            .generatePrivate(pkcs8EncodedKeySpec);

        // 用私钥对信息生成数字签名
        Signature signet = Signature.getInstance("MD5withRSA");
        signet.initSign(myPrivateKey);
        signet.update(password.getBytes("ISO-8859-1"));
        byte[] signed = signet.sign();
        return RSAUtils.bytesToHexStr(signed);
    }

    /**
     * 用公钥验证是否正确
     *
     * @param info
     *        原始数据
     * @param signatureString
     *        用证书加密后的数据
     * @param publickey
     *        公钥
     * @return
     * @throws Exception
     */
    public static boolean verifySignature(String info, String signatureString,
            String publickey) throws Exception {
        //这是GenerateKeyPair输出的公钥编码
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
            RSAUtils.hexStrToBytes(publickey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
        //这是SignatureData输出的数字签名
        byte[] signed = RSAUtils.hexStrToBytes(signatureString);

        Signature signetcheck = Signature.getInstance("MD5withRSA");
        signetcheck.initVerify(pubKey);
        signetcheck.update(info.getBytes());
        return signetcheck.verify(signed);
    }

    private static final byte[] hexStrToBytes(String s) {
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
                16);
        }
        return bytes;
    }

    private static String bytesToHexStr(byte[] bytes) {
        StringBuilder s = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            s.append(RSAUtils.bcdLookup[b >>> 4 & 0x0f]);
            s.append(RSAUtils.bcdLookup[b & 0x0f]);
        }
        return s.toString();
    }

//    public static void main(String[] args) throws Exception {
//        List<String> createKey = RSAUtils.createKey();
//        String privateKey = createKey.get(0);
//        String publicKey = createKey.get(1);
//        System.out.println("私钥..." + privateKey);
//        System.out.println("公钥..." + publicKey);
//        String signatureData = RSAUtils.signatureData(privateKey,
//            "youxin#123youxin#123");
//        System.out.println("signatureData......" + signatureData);
//        boolean signature = RSAUtils.verifySignature("youxin#123",
//            signatureData, publicKey);
//        System.out.println(signature);
//    }
}
