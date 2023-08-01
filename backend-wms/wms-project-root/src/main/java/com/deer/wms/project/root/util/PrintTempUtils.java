package com.deer.wms.project.root.util;

import java.util.List;

import org.jsoup.nodes.Element;

public class PrintTempUtils {
    /**
     * qrCode205*205 barCode12*0.7
     * 
     * @param doc Dom对象，将会被改变，解析并查出对应的值
     */
    public static int setTempData(String eleId, Object data2, Element doc, String type) {
        List<Element> aaa = doc.getElementsByAttributeValue("id", eleId);
        if (aaa == null || aaa.size() == 0) {
            return 0;
        }
        String data = "";
        if (data2 != null) {
            data = data2.toString();
        }
        for (Element ele : aaa) {
            switch (type) {
            case "text":
                ele.text(data);
                break;
            case "qrCode":
                ele.getElementsByTag("img").attr("src", BarCodeUtils.encode(data, 205, 205));
                break;
            case "barCode":
                ele.getElementsByTag("img").attr("src", BarCodeUtils.barCode(data, 12.0, 0.7));
                break;
            }
        }
        return 1;
    }

    /**
     * qrCode205*205 barCode18*0.7
     * 
     * 这个条码比较长 专门给面单用的
     * 
     * @param doc Dom对象，将会被改变，解析并查出对应的值
     */
    public static int setTempData2(String eleId, Object data2, Element doc, String type) {
        List<Element> aaa = doc.getElementsByAttributeValue("id", eleId);
        if (aaa == null || aaa.size() == 0) {
            return 0;
        }
        String data = "";
        if (data2 != null) {
            data = data2.toString();
        }
        for (Element ele : aaa) {
            switch (type) {
            case "text":
                ele.text(data);
                break;
            case "qrCode":
                ele.getElementsByTag("img").attr("src", BarCodeUtils.encode(data, 205, 205));
                break;
            case "barCode":
                ele.getElementsByTag("img").attr("src", BarCodeUtils.barCode(data, 18.0, 0.7));
                break;
            }
        }
        return 1;
    }
}