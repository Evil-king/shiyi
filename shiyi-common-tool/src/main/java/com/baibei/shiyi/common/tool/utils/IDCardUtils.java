package com.baibei.shiyi.common.tool.utils;

import com.ctrip.framework.apollo.core.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: hyc
 * @date: 2019/11/4 17:26
 * @description:
 */
public class IDCardUtils {
    public static Integer IdNOToAge(String IdNO){
        Integer selectYear = Integer.valueOf(IdNO.substring(6, 10));         //出生的年份
        Integer selectMonth = Integer.valueOf(IdNO.substring(10, 12));       //出生的月份
        Integer selectDay = Integer.valueOf(IdNO.substring(12, 14));         //出生的日期
        Calendar cal = Calendar.getInstance();
        Integer yearMinus = cal.get(Calendar.YEAR) - selectYear;
        Integer monthMinus = cal.get(Calendar.MONTH) + 1 - selectMonth;
        Integer dayMinus = cal.get(Calendar.DATE) - selectDay;
        Integer age = yearMinus;
        if (yearMinus < 0) {
            age = 0;
        } else if (yearMinus == 0) {
            age = 0;
        } else if (yearMinus > 0) {
            if (monthMinus == 0) {
                if (dayMinus < 0) {
                    age = age - 1;
                }
            } else if (monthMinus == 12) {
                age = age + 1;
            }
        }
        return age;
    }
    public static String getSex(String CardCode){
        String sex;
        if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
            sex = "女";
        } else {
            sex = "男";
        }
        return sex;
    }
    public static String  hide(String card){
        return card.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1****$2");
    }
    public static String hideCardNo(String cardNo) {
        if(StringUtils.isBlank(cardNo)) {
            return cardNo;
        }

        int length = cardNo.length();
        int beforeLength = 4;
        int afterLength = 4;
        //替换字符串，当前使用“*”
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++) {
            if(i < beforeLength || i >= (length - afterLength)) {
                sb.append(cardNo.charAt(i));
            } else {
                sb.append(replaceSymbol);
            }
        }

        return sb.toString();
    }
}
