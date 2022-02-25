package com.myxiaowang.logistics.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月24日 16:06:00
 */
public class TimeUtil {
    private static final String TIME_ID="yyyyMMddHHmmss";

    public static Date getNowDate(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return new Date(timestamp.getTime());
    }

    /**
     * 获得想要的指定格式时间
     * @param format 格式化
     * @return 返回结果集
     */
    public static String getLocalDateTimeByFormatter(String format){
       return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 根据时间错转换时间
     * @param timestamp 时间搓
     * @return 结果
     */
    public static LocalDateTime getTimeByTimeStamp(Timestamp timestamp){
        Date date = new Date(timestamp.getTime());
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 获取一个时间不充分的id
     * @return 结果集
     */
    public static String getTimeId(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_ID));
        String eplates = String.valueOf(timestamp.getTime()).substring(0, 8);
        Random random = new Random(99999);
        return time+eplates+random.nextInt(9999);
    }
}
