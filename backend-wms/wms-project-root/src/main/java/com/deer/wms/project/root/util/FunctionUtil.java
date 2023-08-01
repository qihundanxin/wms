package com.deer.wms.project.root.util;

public class FunctionUtil {

    public static double nvl(Double value) {
        if (value == null) {
            return 0.0d;
        }
        return value.doubleValue();
    }

}
