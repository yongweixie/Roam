package com.example.xieyo.roam.tools;

import java.math.BigDecimal;
public class NumberUtils {

    private static final Double MILLION = 10000.0;
    private static final Double BILLION = 100000000.0;


    /**
     * 将数字转换成以万为单位或者以亿为单位，因为在前端数字太大显示有问题
     *
     * @author
     * @version 1.00.00
     *
     * @date 2018年1月18日
     * @param amount 报销金额
     * @return
     */
    public static String amountConversion(int amount){
        String result = "";
        if (amount <= 0) {
            result = "";
        } else if (amount < MILLION) {
            result = amount+"";
        } else if(amount>=MILLION&&amount<BILLION){
            double d = (double) amount;
            double num = d / 10000;//1.将数字转换成以万为单位的数字

            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后一位;
            result = f1 + "万";
        } else if(amount >= BILLION){
            double d = (double) amount;
            double num = d / 100000000;//1.将数字转换成以万为单位的数字

            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后一位;
            result = f1 + "亿";
        }
        return result;
    }

}