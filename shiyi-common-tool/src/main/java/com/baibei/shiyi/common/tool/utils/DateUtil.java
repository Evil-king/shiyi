package com.baibei.shiyi.common.tool.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/27 4:53 PM
 * @description:
 */
public class DateUtil {

    private DateUtil() {

    }
    /**
     * 线程安全的日期格式类
     */
    public static ThreadLocal<DateFormat> yyyyMMddHHmmssWithLine = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 线程安全的日期格式类
     */
    public static ThreadLocal<DateFormat> yyyyMMddHHmmssNoLine = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    /**
     * 线程安全的日期格式类
     */
    public static ThreadLocal<DateFormat> yyyyMMddWithLine = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 线程安全的日期格式类
     */
    public static ThreadLocal<DateFormat> yyyyMMddNoLine = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    /**
     * 线程安全的日期格式类
     */
    public static ThreadLocal<DateFormat> HHmmssLine = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("HH:mm");
        }
    };

    /**
     * 线程安全的日期格式类
     */
    public static ThreadLocal<DateFormat> HHmmssNoLine = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("HHmmss");
        }
    };

    /**
     * 获取当天指定时分秒的时间
     * 如：传 00:00:00或23:59:59
     * @return
     */
    public static Date getCurrentStartOrEndTime(String period){
        String s = yyyyMMddWithLine.get().format(new Date()) + " "+period;
        try {
            Date date = yyyyMMddHHmmssWithLine.get().parse(s);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取指定日期0点
     *
     * @param date
     * @return
     */
    public static Date getBeginDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取当天的0点
     *
     * @return
     */
    public static Date getBeginDay() {
        return getBeginDay(new Date());
    }

    /**
     * 获取指定日期23:59:59秒日期
     *
     * @param date
     * @return
     */
    public static Date getEndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

    /**
     * 获取指定日期23:59:59秒日期
     *
     * @return
     */
    public static Date getEndDay() {
        return getEndDay(new Date());
    }

    /**
     * 拼接指定的时分秒,年月日为当前时间
     *
     * @param hhmmss
     * @return
     */
    public static Date appendHhmmss(String hhmmss) {
        Date now = new Date();
        StringBuffer sb = new StringBuffer(yyyyMMddWithLine.get().format(now)).append(" ").append(hhmmss);
        try {
            return yyyyMMddHHmmssWithLine.get().parse(sb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取当天中午12点的时间
     *
     * @return
     */
    public static Date getMiddleTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取从现在开始的一天
     * @return
     */
    public static String get1DayByNowTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return yyyyMMddHHmmssWithLine.get().format(cal.getTime());
    }

    /**
     * 获取当前日期基础上增加指定分数后的日期
     *
     * @param minute
     * @return
     */
    public static Date addMinute(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
    /**
     * 获取当前日期基础上增加指定天数后的日期
     *
     * @param day
     * @return
     */
    public static Date addDay(int day) {
        long addTime=(60*1000*60*24)*day;//一天
        String nowDay = yyyyMMddWithLine.get().format(new Date());
        try {
            Date currentDate = yyyyMMddHHmmssWithLine.get().parse(nowDay + " 00:00:00");
            long currentTime=currentDate.getTime();
            long resultTime=currentTime+addTime;
            Date resultDate = new Date(resultTime);
            return resultDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String changeTimes(Long  time){
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 此处的time格式必须为时间，不能包含日期
     * @param time
     * @return
     */
    public static Date getNowDate(String time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd ");
        Date date=new Date();
        try {
            date = df.parse(ds.format(now) +time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 判断传入时间是否是当天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        return yyyyMMddNoLine.get().format(date).equals(yyyyMMddNoLine.get().format(new Date()));
    }

    /**
     * 将时间字符串转为毫秒值（以HH:mm:ss格式）
     * @param date
     * @return
     */
    public static long strToLong(String date){
        long timeInMillis = 0L;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("HH:mm:ss").parse(date));
            timeInMillis = calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMillis;
    }

    /**
     * 字符串转时间（以yyyy-MM-dd HH:mm:ss格式）
     * @param date
     * @return
     */
    public static Date strToDate(String date){
        Date time = null;
        try {
            time  = DateUtil.yyyyMMddHHmmssWithLine.get().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static void main(String[] args) {
        System.out.println(getCurrentStartOrEndTime("23:59:59").getTime());
    }
}
