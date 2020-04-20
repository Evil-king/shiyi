package com.baibei.shiyi.common.tool.utils;


import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/19 15:27
 * @description:
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {
    /**
     * 从集合中查询已存在的元素
     *
     * @param list
     * @param element
     * @param <T>
     * @return
     */
    public static <T> T findExist(List<T> list, T element) {
        for (T t : list) {
            if (t.equals(element)) {
                return t;
            }
        }
        return null;
    }
}