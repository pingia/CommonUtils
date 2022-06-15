package cn.sunline.uicommonlib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateFormatUtils {
    public static final String DATE_FORMAT_YMDHMS = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_YMD = "yyyyMMdd";
    public static final String DATE_FORMAT_Y_M_D_H_M = "yyyy.MM.dd HH:mm";
    public static final String DATE_FORMATE_Y_M_D = "yyyy.MM.dd";
    public static final String DATE_FORMAT_YM="yyyyMM";
    public static final String DATE_FORMAT_Y_M_D_H_M_ ="yyyy-MM-dd HH:mm";
    /**
     * 将带格式的时间字符串转换为long
     * @param dateStr
     * @param format
     * @return
     */
    public static long formatDateStrToLong(String dateStr,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date==null){
            return 0;
        }
        return date.getTime();
    }

    /**
     * 把long型的时间转换为带格式的字符串
     * @param time
     * @param format
     * @return
     */
    public static String formatDateLongToStr(long time,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }

    public static String formatDateString(String initDateString, String initFormatPattern,String resultFormatPattern){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(initFormatPattern);
            Date date = dateFormat.parse(initDateString);
            return new SimpleDateFormat(resultFormatPattern).format(date);
        }catch (ParseException pe){
            return initDateString;
        }
    }
}
