package com.campuscard.app.utils;

import com.base.frame.utils.XDateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    private static DateTimeUtils instance;

    public static DateTimeUtils getInstance() {
        if (instance == null) {
            instance = new DateTimeUtils();
        }
        return instance;
    }

    public String newTime() {
        SimpleDateFormat df = new SimpleDateFormat(XDateUtil.dateFormatYMD);//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    public String newTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }


    public String weekTime() {
        SimpleDateFormat format = new SimpleDateFormat(XDateUtil.dateFormatYMD);
        Calendar c = Calendar.getInstance();
        //过去七天
        c.setTime(new Date());
        c.add(Calendar.DATE, -7);
        Date d = c.getTime();
        return format.format(d);
    }

    public String oneMothTime() {
        SimpleDateFormat format = new SimpleDateFormat(XDateUtil.dateFormatYMD);
        Calendar c = Calendar.getInstance();
        //过去一月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        return format.format(m);
    }

    public String threeMothTime() {
        SimpleDateFormat format = new SimpleDateFormat(XDateUtil.dateFormatYMD);
        Calendar c = Calendar.getInstance();
        //过去三个月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -3);
        Date m3 = c.getTime();
        return format.format(m3);
    }

}
