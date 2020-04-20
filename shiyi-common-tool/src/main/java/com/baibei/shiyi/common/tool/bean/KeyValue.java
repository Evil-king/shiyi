package com.baibei.shiyi.common.tool.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/27 15:19
 * @description:
 */
@Data
public class KeyValue {
    private String key;
    private String value;


    public static List<KeyValue> merge(List<KeyValue> kvList) {
        KeyValue tempKv;
        Map<String, KeyValue> map = new HashMap();
        for (KeyValue kv : kvList) {
            tempKv = map.get(kv.getKey());
            if (tempKv != null) {
                tempKv.setValue(new BigDecimal(tempKv.getValue()).add(new BigDecimal(kv.getValue())).toPlainString());
            } else {
                map.put(kv.getKey(), kv);
            }
        }
        List<KeyValue> result = new ArrayList<>();
        for (Map.Entry<String, KeyValue> entry : map.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyValue keyValue = (KeyValue) o;
        return Objects.equals(key, keyValue.key) &&
                Objects.equals(value, keyValue.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, value);
    }
}