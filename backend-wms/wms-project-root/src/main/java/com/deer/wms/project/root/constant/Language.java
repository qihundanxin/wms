package com.deer.wms.project.root.constant;

import java.util.Arrays;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;

/**
 * 系统支持的语言枚举
 */
@AllArgsConstructor
public enum Language {
    /** 简体中文 */
    CN("Chinese", new String[] { "cn", "zh-cn", "zh" }),
    /** 英语 */
    EN("English", new String[] { "en" }),
    /** 法语 */
    FR("French", new String[] { "fr" });

    /**
     * 语言名
     */
    private String name;
    /**
     * 语言编码
     */
    private String[] codes;

    /**
     * 根据编码Code查询语言
     * 
     * @param code 编码/名称（忽略大小写，忽略横线和下滑线的区别）
     * @return 语言
     */
    public static Language getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        code = code.toLowerCase().trim().replaceAll("_", "-");
        for (Language l : Language.values()) {
            if (Arrays.asList(l.codes).contains(code)) {
                return l;
            }
            if (l.name.toLowerCase().equals(code)) {
                return l;
            }
        }
        return null;
    }
}
