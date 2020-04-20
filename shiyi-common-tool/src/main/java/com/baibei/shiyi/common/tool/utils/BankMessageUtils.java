package com.baibei.shiyi.common.tool.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: hyc 阿里云四要素工具类
 * @date: 2019/11/27 16:35
 * @description:
 */
public class BankMessageUtils {
        public static void main(String[] args) throws IOException {
            /*String url = "https://bankcard4c.shumaidata.com/bankcard4c";
            String appCode = "2afc8e8dd96a4c148859d80f22aa504d";

            Map<String, String> params = new HashMap<>();
            params.put("idcard", "440582199502183412");
            params.put("name", "江泽龙");
            params.put("bankcard", "622439880024762327");
            params.put("mobile","13760648084");
            String result = get(appCode, url, params);
            System.out.println(result);*/

            /*String url2="https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardBinCheck=true&cardNo=6214831200670245";
            OkHttpClient client = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().url(url2).build();
            Response response = client.newCall(request).execute();
            String rs = response.body().string();
            System.out.println(rs);*/

//            StringBuilder url = new StringBuilder("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardBinCheck=true&");
//            url.append("cardNo").append("=").append("6214831200670245");
//            String substring = url.toString().substring(0, url.length() - 1);
//            System.out.println(substring);
//            OkHttpClient client = new OkHttpClient.Builder().build();
//            Request request = new Request.Builder().url(substring).build();
//            Response response = client.newCall(request).execute();
//            String rs = response.body().string();
//            System.out.println(rs);

            String host = "https://cnaps.market.alicloudapi.com";
            String path = "/lianzhuo/querybankaps";
            String method = "GET";
            String appcode = "2afc8e8dd96a4c148859d80f22aa504d";
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);
            Map<String, String> querys = new HashMap<String, String>();
//            querys.put("bank", "bank");
            querys.put("card", "6225881207235218");
            querys.put("city", "广州");
//            querys.put("key", "");
//            querys.put("page", "");
//            querys.put("province", "province");


            try {
                /**
                 * 重要提示如下:
                 * HttpUtils请从
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
                 * 下载
                 *
                 * 相应的依赖请参照
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
                 */
                HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
//                System.out.println(response.toString());
                //获取response的body
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 用到的HTTP工具包：okhttp 3.13.1
         * <dependency>
         * <groupId>com.squareup.okhttp3</groupId>
         * <artifactId>okhttp</artifactId>
         * <version>3.13.1</version>
         * </dependency>
         */
        public static String get(String appCode, String url, Map<String, String> params) throws IOException {
            url = url + buildRequestUrl(params);
            OkHttpClient client = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().url(url).addHeader("Authorization", "APPCODE " + appCode).build();
            Response response = client.newCall(request).execute();
            System.out.println("返回状态码" + response.code() + ",message:" + response.message());
            String result = response.body().string();
            return result;
        }

    /**
     * 根据银行信息查询支行信息
     * @param host
     * @param path
     * @param method
     * @param appcode
     * @param querys
     * @return
     */
        public static String getBankCode(String host,String path,String method,String appcode,Map<String,String> querys){
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);

//            querys.put("key", "key");
//            querys.put("page", "");
//            querys.put("province", "province");


            try {
                /**
                 * 重要提示如下:
                 * HttpUtils请从
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
                 * 下载
                 *
                 * 相应的依赖请参照
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
                 */
                HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
//                System.out.println(response.toString());
                //获取response的body
                return EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public static String buildRequestUrl(Map<String, String> params) {
            StringBuilder url = new StringBuilder("?");
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                url.append(key).append("=").append(params.get(key)).append("&");
            }
            return url.toString().substring(0, url.length() - 1);
        }

}
