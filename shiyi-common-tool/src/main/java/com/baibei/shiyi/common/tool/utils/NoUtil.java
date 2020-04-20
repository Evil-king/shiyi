package com.baibei.shiyi.common.tool.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/3/24 4:33 PM
 * @description:
 */
public class NoUtil {
    public static void main(String[] args) {
        System.out.println(getHoldRecordNo());
    }

    /**
     * 线程安全的日期格式类
     */
    private static ThreadLocal<DateFormat> yyyyMMddHHmmss = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    /**
     * 获取持仓交易记录编码
     *
     * @return
     */
    public static String getHoldRecordNo() {
        StringBuffer sb = new StringBuffer().append(System.currentTimeMillis() / 1000).append(randomNumbers(6));
        return sb.toString();
    }

    /**
     * 获取委托单号
     *
     * @return
     */
    public static String getEntrustNo() {
        StringBuffer sb = new StringBuffer("2").append(System.currentTimeMillis() / 1000).append(randomNumbers(5));
        return sb.toString();
    }

    /**
     * 获取成交单单号
     *
     * @return
     */
    public static String getDealNo() {
        StringBuffer sb = new StringBuffer("1").append(System.currentTimeMillis() / 1000).append(randomNumbers(5));
        return sb.toString();
    }

    /**
     * 生成唯一订单号
     *
     * @return
     */
    public static String generateOrderNo() {
        return yyyyMMddHHmmss.get().format(new Date()) + randomNumbers(6);
    }

    /**
     * 获取商城订单号
     *
     * @return
     */
    public static String getMallOrderNo() {
        return randomNumbers(15);
    }

    /**
     * 获取随机15位订单号
     *
     * @return
     */
    public static String getRamdomOrderNo() {
        return randomNumbers(15);
    }

    public static String randomNumbers(int length) {
        return randomString("0123456789", length);
    }

    public static String randomString(String baseString, int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }
}

