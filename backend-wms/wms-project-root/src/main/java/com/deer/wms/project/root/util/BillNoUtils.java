package com.deer.wms.project.root.util;

public class BillNoUtils {
    /**
     * 生成入库单号
     */
    public static String generateInBillNo() {
        String billNo = "RK" + "-" + RandomNo.createNo();
        return billNo;
    }

    /**
     * 生成出库单号
     */
    public static String generateOutBillNo() {
        String billNo = "CK" + "-" + RandomNo.createNo();
        return billNo;
    }

    /**
     * 生成采购单号
     */
    public static String generateBuyBillNo() {
        String billNo = "CG" + "-" + RandomNo.createNo();
        return billNo;
    }

    /**
     * 生成冻结单号
     */
    public static String generateFreezeBillNo() {
        String billNo = "DJ" + "-" + RandomNo.createNo();
        return billNo;
    }

    /**
     * 生成ASN单号
     */
    public static String generateAsnBillNo() {
        String billNo = "ASN" + "-" + RandomNo.createNo();
        return billNo;
    }

    /**
     * 生成QC单号
     */
    public static String generateQcBillNo() {
        String billNo = "QC" + "-" + RandomNo.createNo();
        return billNo;
    }

    /**
     * 生成HB单号
     */
    public static String generateHbBillNo() {
        String billNo = "HB" + "-" + RandomNo.createNo();
        return billNo;
    }

    public static String generateWaveCode(Integer wareId) {
        String code = "WAVE-" + wareId + "-" + RandomNo.createTimeString();
        return code;
    }

    public static String generateManufactureBillNo() {
        String code = "MF-" + RandomNo.createNo();
        return code;
    }

    public static String generateLoadNo() {
        String code = "ZC-" + RandomNo.createTimeString();
        return code;
    }

    public static String generateIvTransOrgNo() {
        String code = "HQZY" + RandomNo.createTimeString();
        return code;
    }

    public static String generateCPTNo() {
        return "CPT" + RandomNo.createTimeString();
    }

    public static void test(String[] args) {
        System.out.println(generateInBillNo());
        System.out.println(generateOutBillNo());
        System.out.println(generateFreezeBillNo());
        System.out.println(generateBuyBillNo());
        System.out.println(generateAsnBillNo());
        System.out.println(generateQcBillNo());
        System.out.println(generateHbBillNo());
        System.out.println(generateManufactureBillNo());
        System.out.println(generateWaveCode(123));
        System.out.println(generateCPTNo());

    }
}
