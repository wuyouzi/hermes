/**
 *
 */
package com.ucredit.hermes.utils.qianhai;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 我们信任任何SERVER 证书
 *
 * @author 唐应泉(0755-22625539)
 */
public class TrustAnyTrustManagerUtil implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[] {};
    }

}
