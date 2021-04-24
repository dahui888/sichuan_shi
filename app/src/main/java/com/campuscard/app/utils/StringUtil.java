package com.campuscard.app.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class StringUtil {


    /**
     * double保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        DecimalFormat nf = new DecimalFormat("#.00");
        return nf.format(num).toString();
    }

    /**
     * float保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String floatToString(float num) {
        DecimalFormat nf = new DecimalFormat("0.00");
        return nf.format(num).toString();
    }

    /**
     * double保留小数点后两位--用于显示金额（首页余额，有大小字体区分的）
     *
     * @param num
     * @return
     */
    public static String douToString(double num) {
        String strNum = String.valueOf(num);
        int n = strNum.indexOf(".");
        if (n > 0) {
            //截取小数点后的数字
            String dotNum = strNum.substring(n + 1);
            if ("0".equals(dotNum)) {
                return strNum + "0";
            } else {
                if (dotNum.length() == 1) {
                    return strNum + "0";
                } else {
                    return strNum;
                }
            }
        } else {
            return strNum + ".00";
        }
    }

    /**
     * 判断是字符串处理
     *
     * @param str
     * @return
     */
    public static String isNumeric(String str) {
        String name = null;
        if (!TextUtils.isEmpty(str)) {
            if (str.contains(".")) {
                String[] strings = str.split("\\.");
                if (strings.length > 0) {
                    if (strings[1].equals("0") || strings[1].equals("00")) {
                        name = strings[0];
                    } else {
                        name = str;
                    }
                } else {
                    name = str;
                }
            } else {
                name = str;
            }
        } else {
            name = "0";
        }
        return name;
    }
}