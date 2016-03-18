package com.ucredit.hermes.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 发送HTTP 请求 聚信立服务方法
 *
 * @author jacobdong@juxinli.com
 *         2015年1月22日
 */
public class HttpUtils {

    static private Logger logger = Logger.getLogger(HttpUtils.class);
    static private JsonParser jsonParser = new JsonParser();

    /**
     * 发送 get 请求
     *
     * @param url
     *        地址
     * @return {@link JsonObject}
     */
    public static JsonObject getJsonResponse(String url) {
        HttpUtils.logger.info("# GET 请求URL 为" + url);
        JsonObject jsonObject = new JsonObject();
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpGet get = new HttpGet(url);
        try (CloseableHttpClient closeableHttpClient = httpClientBuilder
                .build()) {
            jsonObject = HttpUtils.convertResponseBytes2JsonObj(closeableHttpClient.execute(get));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JsonObject postJsonData(String url, String jsonStrData) {
        HttpUtils.logger.info("# POST JSON 请求URL 为" + url);
        HttpUtils.logger.info("# POST JSON 数据为" + jsonStrData);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpPost post = new HttpPost(url);
        JsonObject jsonObject = null;
        try (CloseableHttpClient closeableHttpClient = httpClientBuilder
                .build()) {
            //HttpEntity entity = new StringEntity(jsonStrData);
            //修复 POST json 导致中文乱码
            HttpEntity entity = new StringEntity(jsonStrData, "UTF-8");
            post.setEntity(entity);
            post.setHeader("Content-type", "application/json");
            jsonObject = HttpUtils.convertResponseBytes2JsonObj(closeableHttpClient.execute(post));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Header 为 application/json POST 请求数据
     *
     * @param resp
     * @return {@link JsonObject}
     */

    private static JsonObject convertResponseBytes2JsonObj(HttpResponse resp) {
        JsonObject jsonObject = null;

        try (InputStream respIs = resp.getEntity().getContent()) {
            byte[] respBytes = IOUtils.toByteArray(respIs);
            String result = new String(respBytes, Charset.forName("UTF-8"));

            if (null == result || result.length() == 0) {
                // TODO
                System.err.println("无响应");
            } else {
                System.out.println(result);
                if ("{".equals(result.charAt(0)) && result.endsWith("}")) {
                    jsonObject = (JsonObject) HttpUtils.jsonParser
                            .parse(result);
                } else {
                    // TODO
                    System.err.println("不能转成JSON对象");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
