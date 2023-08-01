package com.deer.wms.project.root.util;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 切面工具
 * 
 * @author xuesinuo
 */
public class AspectUtil {
    /**
     * 获取切点的Method
     * 
     * @param joinPoint 切点
     * @return 切点Method
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static Method getMethod(ProceedingJoinPoint joinPoint) throws ClassNotFoundException, NoSuchMethodException, SecurityException {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            Method targetMethod = methodSignature.getMethod();
            return targetMethod;
        } else {
            String argsTypeString = signature.toLongString().substring(signature.toLongString().indexOf("(") + 1, signature.toLongString().indexOf(")"));
            List<String> argsTypeStrings = StringUtil.string2Array(argsTypeString, ",");
            Class<?>[] argsTypes = new Class<?>[argsTypeStrings.size()];
            for (int i = 0; i < argsTypeStrings.size(); i++) {
                argsTypes[i] = Class.forName(argsTypeStrings.get(i));
            }
            Method targetMethod = ((Class<?>) signature.getDeclaringType()).getMethod(signature.getName(), argsTypes);
            return targetMethod;
        }
    }
}
