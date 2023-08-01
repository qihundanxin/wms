package com.deer.wms.project.root.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Function;

/**
 * 本地同步锁工具
 * 
 * @author xuesinuo 2020-08-13
 */
public class SyncUtil {

    /**
     * 获取多个本地同步锁并执行一个方法
     * 
     * @param keyCollection 锁对象的收集集合，仅作收集的临时容器（此集合会被移除元素）
     * @param obj           执行方法的对象
     * @param method        方法
     * @param args          参数
     * @throws InvocationTargetException 方法执行异常
     * @throws IllegalArgumentException  方法参数异常
     * @throws IllegalAccessException    方法访问异常
     * @return 返回结果
     */
    public static Object getAllLocalLock(Collection<?> keyCollection, Object obj, Method method, Object[] args)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (keyCollection.size() > 0) {
            Object lock = keyCollection.iterator().next();
            synchronized (lock) {
                keyCollection.remove(lock);
                return getAllLocalLock(keyCollection, obj, method, args);
            }
        } else {
            return method.invoke(obj, args);
        }
    }

    /**
     * 获取多个本地同步锁并执行一个方法
     * 
     * @param <T>           执行方法入参类型
     * @param <R>           执行方法回参类型
     * @param keyCollection 全部同步锁
     * @param obj           入参
     * @param fun           执行的方法（函数式，方便编写，通用性弱）
     * @return 返回结果
     */
    public static <T, R> R getAllLocalLock(Collection<?> keyCollection, T obj, Function<T, R> fun) {
        if (keyCollection.size() > 0) {
            Object lock = keyCollection.iterator().next();
            synchronized (lock) {
                keyCollection.remove(lock);
                return getAllLocalLock(keyCollection, obj, fun);
            }
        } else {
            return fun.apply(obj);
        }
    }
}
