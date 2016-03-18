/**
 * Copyright(c) 2011-2012 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ijay
 */
public class RESTTemplate {
    private static Logger logger = LoggerFactory.getLogger(RESTTemplate.class);

//    @Autowired
//    private Variables variables;

    private static CloseableHttpClient buildHttpClient() {
        try {
            HttpClientBuilder cb = HttpClientBuilder.create()
                    .disableAutomaticRetries();
            cb.setHostnameVerifier(new AllowAllHostnameVerifier());

            // set ssl context
            SSLContext ctx = SSLContext.getInstance("TLS");

            ctx.init(null, new TrustManager[] { new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0,
                        String arg1) {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0,
                        String arg1) {
                }
            } }, null);
            cb.setSslcontext(ctx);

//            if (this.getUsername() != null && this.getPassword() != null) {
//                CredentialsProvider cp = new BasicCredentialsProvider();
//                cp.setCredentials(
//                    AuthScope.ANY,
//                    new UsernamePasswordCredentials(this.getUsername(), this
//                        .getPassword()));
//                cb.setDefaultCredentialsProvider(cp);
//            }

            RequestConfig cfg = RequestConfig.custom()
                    .setSocketTimeout(30 * 1000).setConnectTimeout(60 * 1000)
                    .build();
            cb.setDefaultRequestConfig(cfg);
            return cb.build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static CloseableHttpClient buildHttpClientSSL() {
        try {
            HttpClientBuilder cb = HttpClientBuilder.create()
                    .disableAutomaticRetries();
            cb.setHostnameVerifier(new AllowAllHostnameVerifier());

            // set ssl context
            SSLContext ctx = SSLContext.getInstance("SSLv3");

            ctx.init(null, new TrustManager[] { new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0,
                        String arg1) {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0,
                        String arg1) {
                }
            } }, null);
            cb.setSslcontext(ctx);

//            if (this.getUsername() != null && this.getPassword() != null) {
//                CredentialsProvider cp = new BasicCredentialsProvider();
//                cp.setCredentials(
//                    AuthScope.ANY,
//                    new UsernamePasswordCredentials(this.getUsername(), this
//                        .getPassword()));
//                cb.setDefaultCredentialsProvider(cp);
//            }

            RequestConfig cfg = RequestConfig.custom()
                    .setSocketTimeout(30 * 1000).setConnectTimeout(60 * 1000)
                    .build();
            cb.setDefaultRequestConfig(cfg);
            return cb.build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 调用controller中的方法，保存数据到数据库
     *
     * @param url
     * @param o
     * @param type
     * @return
     * @throws Exception
     */
    public <T> ResponseEntity<T> addEntity(String url, Object o, Class<T> type)
            throws Exception {
        try {
            try (CloseableHttpClient client = RESTTemplate.buildHttpClient();) {
                return RESTTemplate.buildTemplate(client).postForEntity(url, o,
                    type);
            } catch (HttpStatusCodeException ex) {
                this.handleServerException(url, ex);
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static RestTemplate buildTemplate(CloseableHttpClient client) {
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
            client);
        return new RestTemplate(requestFactory);
    }

    public <T> T getEntity(String url, Class<T> type) throws Exception {
        try {
            try (CloseableHttpClient client = RESTTemplate.buildHttpClient();) {
                return RESTTemplate.buildTemplate(client).getForObject(url,
                    type);
            } catch (HttpStatusCodeException ex) {
                this.handleServerException(url, ex);
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEntity(String url) throws Exception {
        StringBuilder data = new StringBuilder("");
        try {
            HttpGet httpget = new HttpGet(url);
            try (CloseableHttpClient httpclient = RESTTemplate
                    .buildHttpClient();
                    CloseableHttpResponse response = httpclient
                            .execute(httpget);
                    BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(
                            response.getEntity().getContent(), "UTF-8"));) {
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    data = data.append(line);
                }
                return data.toString();
            } catch (HttpStatusCodeException ex) {
                this.handleServerException(url, ex);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void putEntity(String url, Object request) throws Exception {
        try {
            try (CloseableHttpClient client = RESTTemplate.buildHttpClient();) {
                RESTTemplate.buildTemplate(client).put(url, request);
            } catch (HttpStatusCodeException ex) {
                this.handleServerException(url, ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> postEntity(String url, Object request)
            throws Exception {
        try {
            try (CloseableHttpClient client = RESTTemplate.buildHttpClient();) {
                return RESTTemplate.buildTemplate(client).postForEntity(url,
                    request, String.class);
            } catch (HttpStatusCodeException ex) {
                return this.handleServerException(url, ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected ResponseEntity<String> handleServerException(String url,
        HttpStatusCodeException ex) throws Exception {
        HttpStatus statusCode = ex.getStatusCode();
        String respString = ex.getResponseBodyAsString();

        switch (statusCode) {
            case CONFLICT:
                return new ResponseEntity<>(respString, statusCode);
            case NOT_FOUND:
                throw new RuntimeException("URL not found: " + url);
            default:
                try {
                    RESTTemplate.logger
                    .error("Response string:\n" + respString);

                    Map<String, String> map = new ObjectMapper().configure(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false).readValue(respString, Map.class);
                    String name = null; // from Renrendai

                    // Exceptions have stackTrace
                    if (map.containsKey("stackTrace")) {
                        Exception ori = new ObjectMapper().configure(
                            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                            false).readValue(respString, Exception.class);

                        throw new Exception("Exception thrown from server", ori);
                    } else if ((name = map.get("name")) != null) {
                        throw new RuntimeException(name);
                    } else {
                        throw new RuntimeException(respString);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(respString, e);
                }
        }
    }

    public String sendJsonWithHttps(String surl, String json) throws Exception {

        try (CloseableHttpClient client = RESTTemplate.buildHttpClientSSL()) {
            HttpPost post = new HttpPost(surl);
            StringEntity entity = new StringEntity(json,
                ContentType.APPLICATION_JSON);
            post.setEntity(entity);
            String baosStr = "";
            try (CloseableHttpResponse response = client.execute(post)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity e = response.getEntity();
                    baosStr = EntityUtils.toString(e, "UTF-8");
                }
                return baosStr;
            }
        }
    }
//    protected String getUsername() {
//        return this.variables.getAdminUsername();
//    }
//
//    protected String getPassword() {
//        return this.variables.getAdminPassword();
//    }
}
