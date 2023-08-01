package com.deer.wms.project.root.util;

import java.util.HashMap;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 * Jexl表达式（可动态编辑的Java脚本）
 * 
 * @author xuesinuo
 */
public class JexlUtil {
 
    /**
     * 基础Jexl工具
     * 
     * @param jexl   表达式
     * @param params 表达式中的参数
     * @return jexl表达式运行结果
     */
    public Object simpleJexl(String jexl, HashMap<String, Object> params) {
        JexlEngine jexlEngine = new JexlEngine();
        Expression e = jexlEngine.createExpression(jexl);
        JexlContext jc = new MapContext();
        for (String key : params.keySet()) {
            jc.set(key, params.get(key));
        }
        Object result = e.evaluate(jc);
        return result;
    }
}
