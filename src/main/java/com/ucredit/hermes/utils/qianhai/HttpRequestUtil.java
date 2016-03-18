package com.ucredit.hermes.utils.qianhai;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;

/**
 * HTTP请求
 *
 * @author TANGYINGQUAN360
 */
public class HttpRequestUtil {
    public static String sendJsonWithHttp(String surl, String json)
            throws Exception {
        URL url = new URL(surl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type",
            "application/json;charset=utf-8");
        conn.setRequestMethod("POST");// 提交模式
        conn.setRequestProperty("Content-Length", json.getBytes().length + "");
        conn.setConnectTimeout(100000);// 连接超时单位毫秒 //
        conn.setReadTimeout(200000);// 读取超时 单位毫秒
        conn.setDoOutput(true);// 是否输入参数
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.connect();
        StringBuffer sb = new StringBuffer();
        try (DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {

            out.write(json.getBytes());
            out.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        conn.disconnect();
        return sb.toString();
    }

    final private static String PROTOCOL_NAME = "https";

    public static String sendJsonWithHttps(String surl, String json)
            throws Exception {
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset("UTF-8");
        Protocol httpProtocol = new Protocol(HttpRequestUtil.PROTOCOL_NAME,
            new SSLProtocolSocketFactoryUtil(false), 443);
        Protocol.registerProtocol(HttpRequestUtil.PROTOCOL_NAME, httpProtocol);
        PostMethod post = new PostMethod(surl);
        post.setRequestHeader("Content-Type", "application/json");
        RequestEntity requestEntity = new ByteArrayRequestEntity(
            json.getBytes("utf-8"));
        post.setRequestEntity(requestEntity);
        client.executeMethod(post);
        byte[] buf = new byte[512];
        String baosStr = "";
        try (InputStream in = post.getResponseBodyAsStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            do {
                int n = in.read(buf);
                if (n > 0) {
                    baos.write(buf, 0, n);
                } else if (n <= 0) {
                    break;
                }
            } while (true);
            baosStr = baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baosStr;
    }
}
