package com.deer.wms.project.root.util;

import java.lang.reflect.Method;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * SPEL解析工具
 * 
 * @author xuesinuo
 *
 */
@Slf4j
public class SpelUtil {

    /**
     * SPEL解析
     * 
     * @param target 目标对象
     * @param spel   表达式
     * @param method 目标方法
     * @param args   方法参数
     * @return 解析结果
     */
    public static String getSpelValue(Object target, String spel, Method method, Object[] args) {
        log.debug("target: " + target);
        log.debug("spel: " + spel);
        log.debug("method: " + method);
        log.debug("args: " + args);
        if (spel == null || spel.trim().isEmpty()) {
            return "";
        }
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new MethodBasedEvaluationContext(target, method, args, u);
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(spel).getValue(context, String.class);
    }
}
