package com.deer.wms.project.root.model;

import com.alibaba.fastjson2.JSON;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * WMS系统中，统一的二维码对象格式
 * 
 * @author xuesinuo
 *
 */
@Data
public class QrEntity {
    /**
     * SO单号
     */
    private String so;
    /**
     * SKU编号
     */
    private String sku;
    /**
     * 顺序号
     */
    private String skui;
    /**
     * 本单中相同SKU的数量
     */
    private String skuqty;

    /**
     * 二维码方式解析方式
     * 
     * @author xuesinuo
     *
     */
    public enum QrStringType {
        /** JSON */
        JSON,
        /**
         * 极简二维码字符串：每个特殊ASCII标点代表后续出现的内容是一种编号，遇到下个特殊标点是截止，遇到分号时整个二维码内容结束。<br>
         * 
         * 追加规则是，建议按照QWERTY键盘上的标点顺序依次添加，跳过-_=+;:'",.<br>
         * 
         * let codeArray = ['`','~','!','@','#','$','%','^','&','*','+','|'];<br>
         * 
         * 为方便解析，符号代表的含义，顺序均固定不变！<br>
         * 
         */
        SIMPLE_STRING
    }

    /**
     * 字符串变为Qr对象
     * 
     * @param type     字符串格式类型
     * @param qrString 字符串
     * @return Qr对象
     */
    public static QrEntity stringToEntity(QrStringType type, String qrString) {
        if (StrUtil.isBlank(qrString)) {
            return null;
        }
        if (type == QrStringType.JSON) {
            return JSON.parseObject(qrString, QrEntity.class);
        }
        if (type == QrStringType.SIMPLE_STRING) {
            QrEntity result = new QrEntity();
            if (StrUtil.isBlank(qrString)) {
                return result;
            }
            // 【正序列举】每种已经被定义的特殊字
            String[] codeArray = { "`", "~", "!", "@", "#", "$", "%", "^", "&", "*", "+", "|" };
            // 结束符
            int endIndex = qrString.indexOf(';');
            if (endIndex <= 0) {
                return null;
            }
            // 【倒序解析】
            for (int i = 0; i < codeArray.length; i++) {
                int beginIndex = qrString.indexOf(codeArray[codeArray.length - 1 - i]);
                if (beginIndex >= 0) {
                    String k = codeArray[codeArray.length - 1 - i];
                    String v = qrString.substring(beginIndex + 1, endIndex);
                    switch (k) {
                    case "`":
                        result.so = v;
                        break;
                    case "~":
                        result.sku = v;
                        break;
                    case "!":
                        result.skui = v;
                        break;
                    case "@":
                        result.skuqty = v;
                        break;
                    default:
                        break;
                    }
                    endIndex = beginIndex;
                }
            }
            return result;
        }
        return null;
    }

    /**
     * Qr对象转字符串
     * 
     * @param type 转换方式
     * @return 转换结果
     */
    public String entityToString(QrStringType type) {
        if (type == QrStringType.JSON) {
            return JSON.toJSONString(this);
        }
        if (type == QrStringType.SIMPLE_STRING) {
            StringBuffer sb = new StringBuffer();
            if (StrUtil.isNotBlank(this.so)) {
                sb.append("`").append(this.so);
            }
            if (StrUtil.isNotBlank(this.sku)) {
                sb.append("~").append(this.sku);
            }
            if (StrUtil.isNotBlank(this.skui)) {
                sb.append("!").append(this.skui);
            }
            if (StrUtil.isNotBlank(this.skuqty)) {
                sb.append("@").append(this.skuqty);
            }
            if (StrUtil.isNotBlank(sb)) {
                sb.append(";");
            }
            return sb.toString();
        }
        return null;
    }
}
