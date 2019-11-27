package com.example.demo.util;

import com.example.demo.util.constant.BizConstants;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Package com.inspur.icity.microsrv.operation.green.plant.logic.util
 * @ClassName WeChatUtil
 * @Description 微信工具类
 * @Author W.Z.King
 * @Date 2019/5/6 11:11
 */
public class WeChatUtil {

    public static Map<String, Object> getWeChatUserInfo(String code) throws Exception {
        Map<String, String> getTokenParams = new HashMap<>();
        getTokenParams.put("appid", BizConstants.WX_APP_ID);
        getTokenParams.put("secret", BizConstants.WX_SECRET);
        getTokenParams.put("code", code);
        getTokenParams.put("grant_type", BizConstants.WX_GRANT_TYPE);
        String result = HttpUtil.get(BizConstants.WX_ACCESS_TOKEN_URL, getTokenParams);
        JSONObject ret = JSONObject.fromObject(result);
        String accessToken = ret.getString("access_token");
        String openid = ret.getString("openid");

        Map<String, String> getUserParams = new HashMap<>();
        getUserParams.put("access_token", accessToken);
        getUserParams.put("openid", openid);
        getUserParams.put("lang", "zh_CN");
        String userInfo = HttpUtil.get(BizConstants.WX_ACCESS_USER_INFO, getUserParams);
//        JSONObject userJO = JSONObject.fromObject(userInfo);
//        String nickName = userJO.getString("nickname");
//        String headImgUrl = userJO.getString("headimgurl");
//        int sex = userJO.getInt("sex");

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("userInfo", userInfo);
//        retMap.put("nickName", nickName);
//        retMap.put("headImgUrl", headImgUrl);
//        retMap.put("sex", sex);

        return retMap;
    }

    public static void main(String[] args) {
        try {
            getWeChatUserInfo("071kTFX40FVQxC1yf1X405gVX40kTFXE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class HttpUtil{

    private static String get(String url) throws Exception {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000).setSocketTimeout(10000).build();

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, "utf-8");
            return writer.toString();
        } catch (Exception var6) {
            throw var6;
        }
    }


    public static String get(String url, Map<String, String> param) throws Exception {
        if (param != null && !param.isEmpty()) {
            url = url + "?" + mapToUrl(param);
        }

        return get(url);
    }

    public static String mapToUrl(Map<String, String> map) {
        if (map == null) {
            return "";
        } else {
            StringBuffer strb = new StringBuffer();
            Iterator var2 = map.entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var2.next();
                strb.append((String)entry.getKey() + "=" + (String)entry.getValue());
                strb.append("&");
            }

            String url = strb.toString();
            if (url.endsWith("&")) {
                url = StringUtils.substringBeforeLast(url, "&");
            }

            return url;
        }
    }
}
