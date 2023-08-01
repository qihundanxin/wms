package com.deer.wms.project.root.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class GenerateSelfId {
    public static String getSelfIdByUUId() {

        int machineId = 1;// 最大支持1-9个集群机器部署

        int hashCodeV = UUID.randomUUID().toString().hashCode();

        if (hashCodeV < 0) {// 有可能是负数

            hashCodeV = -hashCodeV;

        }

// 0 代表前面补充0

// 4 代表长度为4

// d 代表参数为正数型
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        int random = (int) (Math.random() * 14);
        return str.charAt(random) + String.format("%08d", hashCodeV);

    }

    public static void test(String[] args) {

        System.out.println(getSelfIdByUUId());

    }
}
