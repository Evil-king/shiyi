package com.baibei.shiyi.user.common;


import java.util.Calendar;

/**
 * @author: hyc
 * @date: 2019/5/27 20:42
 * @description:
 */
public class GenerateCertificateTokenUtil {

    public static String appKey() {
        return SecureRandomUtil.generateRandomCode(32);
    }

    public static String appSecret() {
        return SecureRandomUtil.generateRandomCode(32);
    }

    public static String accessToken() {
        return SecureRandomUtil.generateRandomCode(32);
    }

    public static String refreshToken() {
        return SecureRandomUtil.generateRandomCode(32);
    }

    /**
     * 过期时间7天
     *
     * @return
     */
    public static long accessTokenExpireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        return calendar.getTime().getTime();
    }

    /**
     * 过期时间10天
     *
     * @return
     */
    public static long refreshTokenExpireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        return calendar.getTime().getTime();
    }
}

