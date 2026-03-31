package com.git.util;

import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * 签名工具类
 */
public class SignUtil {

    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

    private static final int TIMEOUT = 30000;

    private static final String MAC_NAME = "HmacSHA1";

    private static final String ENCODING = "utf-8";

    /**
     * 生成 API 签名
     * 签名算法：HMAC-SHA1(appid + type + postData, secretKey) -> Base64 -> 大写 -> URL 编码
     */
    public static String appSign(String id, String secretKey, String type, String postData) throws Exception {
        String plainText = id + type + postData;
        byte[] hmacDigest = hmacsha1(plainText, secretKey);
        return URLEncoder.encode(Base64Encode(hmacDigest).toUpperCase(), "utf-8");
    }

    /**
     * Base64 编码
     */
    public static String Base64Encode(byte[] binaryData) {
        return Base64.getEncoder().encodeToString(binaryData);
    }

    /**
     * HmacSHA1 加密
     */
    public static String HmacSha1(byte[] binaryData, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("utf-8"), "HmacSHA1");
        mac.init(secretKey);
        byte[] digest = mac.doFinal(binaryData);
        return bytesToHex(digest);
    }

    /**
     * HmacSHA1 加密（字符串）
     */
    public static byte[] hmacsha1(String plainText, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("utf-8"), "HmacSHA1");
        mac.init(secretKey);
        return mac.doFinal(plainText.getBytes("utf-8"));
    }

    /**
     * HTTP POST 请求
     */
    public static JSONObject httpPost(String url, String strParam) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);

        try {
            if (null != strParam) {
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");  // 修改为 JSON 格式
                httpPost.setEntity(entity);
                
                // 添加 Accept 头
                httpPost.setHeader("Accept", "application/json");
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            if (result.getStatusLine().getStatusCode() == 200) {
                try {
                    String str = EntityUtils.toString(result.getEntity(), "utf-8");
                    if (str != null) {
                        jsonResult = JSONObject.parseObject(str);
                    }
                } catch (Exception e) {
                    logger.error("POST 内容解析失败：{}", url, e);
                }
            } else {
                logger.error("POST 请求失败，状态码：{}", result.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            logger.error("POST 请求提交失败：{}", url, e);
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
            httpPost.releaseConnection();
        }
        return jsonResult;
    }

    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
