package com.deer.wms.project.root.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomNo {
    public static String createNo() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        String No = str + "-" + rannum;
        return No;
    }

    public static String getNo() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (9999900 - 1000000 + 1)) + 1000000;// 获取5位随机数
        String No = str + "-" + rannum;
        return No;
    }

    public static String createTimeString() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        return str;
    }

    public static String createDateString() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        return str;
    }

    public static void test(String[] args) {

        System.out.print(RandomNo.getNo().substring(2, 22));
    }

}
