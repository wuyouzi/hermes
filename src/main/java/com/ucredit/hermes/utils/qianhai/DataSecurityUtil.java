package com.ucredit.hermes.utils.qianhai;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.ucredit.hermes.exception.PafaAppException;
import com.ucredit.hermes.exception.ServicesException;

public class DataSecurityUtil {
    public static String digest(byte[] oriByte) throws PafaAppException {
        MessageDigest md = null;
        String strDes = null;
        try {
            md = MessageDigest.getInstance("SHA1");
            md.update(oriByte);
            strDes = CommonUtil.bytes2Hex(md.digest());
        } catch (Exception e) {
            throw new PafaAppException("E000016", e);
        }
        return strDes;
    }

    public static String signData(String data, String keyPath,
            String storeAlias, String storePassword) throws PafaAppException {
        try {
            PrivateKey key = DataSecurityUtil.getPrivateKey(keyPath,
                storeAlias, storePassword);
            Signature sig = Signature.getInstance("SHA1WithRSA");
            sig.initSign(key);
            sig.update(data.getBytes("utf-8"));
            byte[] sigBytes = sig.sign();
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encodeBuffer(sigBytes);
        } catch (Exception e) {
            throw new PafaAppException("E000012", e);
        }
    }

    public static void verifyData(String data, String signValue, String cerPath)
            throws PafaAppException {
        try {
            PublicKey key = DataSecurityUtil.getPublicKey(cerPath);
            Signature sig = Signature.getInstance("SHA1WithRSA");
            sig.initVerify(key);
            sig.update(data.getBytes("utf-8"));
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] signValueByte = decoder.decodeBuffer(signValue);
            if (!sig.verify(signValueByte)) {
                throw new PafaAppException("E000013");
            }
            System.out.println("验签OK！");
        } catch (Exception e) {
            throw new PafaAppException("E000014", e);
        }
    }

    private static PublicKey getPublicKey(String cerPath)
            throws PafaAppException {
        @SuppressWarnings("resource")
        InputStream is = null;
        try {
            is = new FileInputStream(cerPath);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
            return cert.getPublicKey();
        } catch (FileNotFoundException e) {
            throw new PafaAppException("E000029", e);
        } catch (CertificateException e) {
            throw new PafaAppException("E000030", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static PrivateKey getPrivateKey(String keyPath, String storeAlias,
            String storePassword) throws PafaAppException {
        char[] storePwdArr;
        int i;
        @SuppressWarnings("resource")
        BufferedInputStream bis = null;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            @SuppressWarnings("resource")
            FileInputStream fis = new FileInputStream(keyPath);
            bis = new BufferedInputStream(fis);
            storePwdArr = new char[storePassword.length()];// store password
            for (i = 0; i < storePassword.length(); i++) {
                storePwdArr[i] = storePassword.charAt(i);
            }
            ks.load(bis, storePwdArr);
            return (PrivateKey) ks.getKey(storeAlias, storePwdArr);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new PafaAppException("E000033", e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new PafaAppException("E000031", e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new PafaAppException("E000032", e);
        } catch (CertificateException e) {
            e.printStackTrace();
            throw new PafaAppException("E000030", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new PafaAppException("E000033", e);
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            throw new PafaAppException("E000033", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String decrypt(String sealTxt, String keyStr)
            throws ServicesException {
        try {
            Cipher cipher = null;
            byte[] byteFina = null;
            SecretKey key = DataSecurityUtil.getKey(keyStr);
            try {
                cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key);
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] sealByte = decoder.decodeBuffer(sealTxt);
                byteFina = cipher.doFinal(sealByte);
                return new String(byteFina, "utf-8");
            } catch (Exception e) {
                throw new PafaAppException("E000034", e);
            } finally {
                cipher = null;
            }
        } catch (PafaAppException ee) {
            throw new ServicesException(ee);
        }
    }

    public static String encrypt(byte[] oriByte, String keyStr)
            throws ServicesException {
        try {
            byte[] sealTxt = null;
            SecretKey key = DataSecurityUtil.getKey(keyStr);
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                sealTxt = cipher.doFinal(oriByte);
                BASE64Encoder encoder = new BASE64Encoder();
                return encoder.encode(sealTxt);
            } catch (Exception e) {
                throw new PafaAppException("E000035", e);
            } finally {
                cipher = null;
            }
        } catch (PafaAppException ee) {
            throw new ServicesException(ee);
        }
    }

    private static SecretKey getKey(String key) throws PafaAppException {
        try {
            // 实例化DESede密钥
            DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("utf-8"));
            // 实例化密钥工厂
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance("DESede");
            // 生成密钥
            return keyFactory.generateSecret(dks);
        } catch (Exception e) {
            throw new PafaAppException("E000036", e);
        }
    }

}
