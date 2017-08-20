package com.ximalaya.ops.schedule.service.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nihao on 16/10/10.
 */
public class SimpleDateUtil {
    private static final String dateFormatString="yyyy-MM-dd HH:mm:ss";
    private static final String dateFormatShortString="yyyy-MM-dd";
    private static final String dateFormatMinString="yyyyMMdd";
    private static final String pppp="HH:mm:ss";
    private static ThreadLocal<DateFormat> threadLocal=new ThreadLocal();
    private static ThreadLocal<DateFormat> threadLocalShort=new ThreadLocal();
    private static ThreadLocal<DateFormat> threadLocalMin=new ThreadLocal();
    private static ThreadLocal<DateFormat> threadPPPP=new ThreadLocal();

    public static DateFormat getDateFormatPPP(){
        DateFormat df=threadPPPP.get();
        if(df==null){
            df=new SimpleDateFormat(pppp);
            threadPPPP.set(df);
        }
        return df;
    }

    public static DateFormat getDateFormat(){
        DateFormat df=threadLocal.get();
        if(df==null){
            df=new SimpleDateFormat(dateFormatString);
            threadLocal.set(df);
        }
        return df;
    }
    public static DateFormat getShortDateFormat(){
        DateFormat df=threadLocalShort.get();
        if(df==null){
            df=new SimpleDateFormat(dateFormatShortString);
            threadLocalShort.set(df);
        }
        return df;
    }
    public static DateFormat getMinDateFormat(){
        DateFormat df=threadLocalMin.get();
        if(df==null){
            df=new SimpleDateFormat(dateFormatMinString);
            threadLocalMin.set(df);
        }
        return df;
    }

    /**
     * Date转String
     * @param date
     * @return
     */
    public static String formatPPP(Date date){
        return getDateFormatPPP().format(date);
    }

    /**
     * String转Date
     * @param strDate
     * @return
     */
    public static Date parsePPP(String strDate){
        try {
            return getDateFormatPPP().parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Date转String
     * @param date
     * @return
     */
    public static String format(Date date){
        return getDateFormat().format(date);
    }

    /**
     * String转Date
     * @param strDate
     * @return
     */
    public static Date parse(String strDate){
        try {
            return getDateFormat().parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Date转String
     * @param date
     * @return
     */
    public static String shortFormat(Date date){
        return getShortDateFormat().format(date);
    }

    /**
     * String转Date
     * @param strDate
     * @return
     */
    public static Date shortParse(String strDate){
        try {
            return getShortDateFormat().parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Date转String
     * @param date
     * @return
     */
    public static String minFormat(Date date){
        return getMinDateFormat().format(date);
    }

    /**
     * String转Date
     * @param strDate
     * @return
     */
    public static Date minParse(String strDate){
        try {
            return getMinDateFormat().parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
