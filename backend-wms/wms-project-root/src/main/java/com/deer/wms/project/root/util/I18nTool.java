package com.deer.wms.project.root.util;

import com.deer.wms.project.root.constant.Language;

/**
 * 自定义的i18nTool
 * 
 * @author xuesinuo
 */
public interface I18nTool {
    /**
     * 获取当前环境下的语言
     * 
     * @return 语言
     */
    public Language getLanguage();
}
