package com.baibei.shiyi.common.tool.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/14 18:23
 * @description:
 */
@Slf4j
public class SkuPropertyUtil {

    /**
     * 根据sku获取sku的值
     * eg：输入：{"颜色":"红色","尺寸":"30"}，输出：红色;30
     *
     * @param skuProperty
     * @return
     */
    public static String getSkuPropertyValue(String skuProperty) {
        if (StringUtils.isEmpty(skuProperty)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        try {
            JSONObject jsonObject = JSONObject.parseObject(skuProperty);
            Set<String> set = jsonObject.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                sb.append(jsonObject.getString(iterator.next()));
                sb.append(";");
            }
            String result = sb.toString();
            return result.substring(0, result.length() - 1);
        } catch (Exception e) {
            log.error("获取sku异常", e);
        }
        return "";
    }

    /**
     * 获取属性规格描述
     * eg:输入[{"key":"颜色","value":"红色"},{"key":"尺寸","value":"37"}]，输出：{"颜色":"红色","尺寸":"30"}
     * @return
     */
    public static String getSkuProperty(String jsonStr){
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        JSONObject jsonProperty = new JSONObject();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            String key = jsonObject.getString("key");
            String value = jsonObject.getString("value");
            jsonProperty.put(key,value);
        }
        return jsonProperty.toJSONString();
    }

    /**
     * 获取商品参数描述
     * eg:输入[{"key":"颜色","value":"红色"},{"key":"尺寸","value":"37"}]，输出：{"颜色":"红色","尺寸":"30"}
     * @return
     */
    public static String getParams(String jsonStr){
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        JSONObject jsonProperty = new JSONObject();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            String key = jsonObject.getString("key");
            String value = jsonObject.getString("value");
            jsonProperty.put(key,value);
        }
        return jsonProperty.toJSONString();
    }

    /**
     * 属性规格转换
     * eg:输入{"颜色":"红色","尺寸":"30"}，输出：[{"key":"颜色","value":"红色"},{"key":"尺寸","value":"37"}]
     * @param jsonStr
     * @return
     */
    public static String transformProperty(String jsonStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONArray jsonArray = new JSONArray();
        Set<String> keys = jsonObject.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            Object value = jsonObject.get(key);
            JSONObject jb = new JSONObject();
            jb.put("key",key);
            jb.put("value",value);
            jsonArray.add(jb);
        }
        return jsonArray.toJSONString();
    }

    public static void main(String[] args) {
        String jsonStr = "{\"颜色\":\"红色\",\"尺寸\":30}";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONArray jsonArray = new JSONArray();
        Set<String> keys = jsonObject.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            Object value = jsonObject.get(key);
            JSONObject jb = new JSONObject();
            jb.put("key",key);
            jb.put("value",value);
            jsonArray.add(jb);
        }
        System.out.println(jsonArray.toJSONString());
    }
}