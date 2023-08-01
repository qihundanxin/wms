package com.deer.wms.project.root.util;

import java.math.BigDecimal;

public class MyUtils {

    public static double calculateDivide(Integer inventoryPackDetailId, Integer outPackDetailId, double quantity, double transRatio) {
        if (inventoryPackDetailId.equals(outPackDetailId)) {
            return quantity;
        } else {
//            return quantity/transRatio;
            String a = String.valueOf(quantity);
            String b = String.valueOf(transRatio);
            BigDecimal one = new BigDecimal(a);
            BigDecimal two = new BigDecimal(b);
            return (one.divide(two).setScale(4)).doubleValue();
        }
    }

    public static double calculateMultiplication(Integer inventoryPackDetailId, Integer outPackDetailId, double quantity, double transRatio) {
        if (inventoryPackDetailId.equals(outPackDetailId)) {
            return quantity;
        } else {
            String a = String.valueOf(quantity);
            String b = String.valueOf(transRatio);
            BigDecimal one = new BigDecimal(a);
            BigDecimal two = new BigDecimal(b);
            return (one.multiply(two).setScale(4)).doubleValue();
        }
    }
}
