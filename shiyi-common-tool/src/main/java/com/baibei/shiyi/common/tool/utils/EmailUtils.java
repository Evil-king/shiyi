package com.baibei.shiyi.common.tool.utils;

/**
 * @author: hyc
 * @date: 2019/12/5 19:42
 * @description:
 */
public class EmailUtils {
    /**
     * 校验EMAIL格式，真为正确
     *
     * @author
     * @date 2017-7-19
     * @param email
     * @return true 为格式正确 false 为格式错误
     */
    public static boolean emailFormat(String email) {
        boolean tag = true;
        if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            tag = false;
        }
        return tag;
    }
}
