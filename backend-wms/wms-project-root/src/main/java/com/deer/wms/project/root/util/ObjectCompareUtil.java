package com.deer.wms.project.root.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 对象比较工具
 * 
 * @author xuesinuo
 */
public class ObjectCompareUtil {
    /**
     * Object compare method.
     */
    public static enum EqType {
        /** use compareTo() */
        compareTo,
        /** use toString() */
        toString,
        /** use equals() */
        equals,
        /** use == */
        eqeq,
        /** 当做pojo对比各个属性，每个属性使用compareTo + equals对比 */
        @Deprecated
        pojo,
        /** 允许不同class的pojo进行对比，忽略null */
        @Deprecated
        pojos,
        /** 使用相同的json序列化规则，对比序列化结果（序列化忽略null） */
        @Deprecated
        pojoJson,

    }

    /**
     * Object对比实现
     */
    public static class ObjectCompare {
        /**
         * 空相等对比
         * 
         * @param obj1 对象1
         * @param obj2 对象2
         * @return 结果
         */
        private static Boolean compareNull(Object obj1, Object obj2) {
            if (obj1 == null || obj2 == null) {
                return obj1 == obj2;
            } else {
                return null;
            }
        }

        /**
         * 获取Comparable的泛型类
         * 
         * @param clazz 原数据类型
         * @return 目标泛型类型
         */
        private static Type getComparableGeneric(Class<?> clazz) {
            Type[] objInterfaces = clazz.getGenericInterfaces();
            for (Type objInterface : objInterfaces) {
                if (objInterface instanceof ParameterizedType) {
                    ParameterizedType objParameterizedInterface = (ParameterizedType) objInterface;
                    if (objParameterizedInterface.getRawType().equals(Comparable.class)) {
                        return objParameterizedInterface.getActualTypeArguments()[0];
                    }
                }
            }
            if (clazz.getSuperclass() != null) {
                return getComparableGeneric(clazz.getSuperclass());
            }
            return null;
        }

        /**
         * 比较
         * 
         * <pre>
         * compareTo比较对象： 
         * 1.null,null返回true 
         * 2.非Comparable实现返回false 
         * 3.Comparable泛型不同返回false 
         * 4.compareTo()==0返回true
         * </pre>
         * 
         * @param obj1 对象1
         * @param obj2 对象2
         * @return 结果
         */
        public static boolean compareTo(Object obj1, Object obj2) {
            if (obj1 instanceof Comparable<?> && obj2 instanceof Comparable<?>) {
                Type objComparableGeneric = getComparableGeneric(obj1.getClass());
                Type itemObjectComparableGeneric = getComparableGeneric(obj2.getClass());
                if (objComparableGeneric.equals(itemObjectComparableGeneric)) {// Comparable泛型相同，可以比较
                    @SuppressWarnings("unchecked")
                    Comparable<Object> objComparable = (Comparable<Object>) obj1;
                    if (objComparable.compareTo(obj2) == 0) {
                        return true;
                    }
                }
            }
            return false;
        }

        public static boolean toString(Object obj1, Object obj2) {
            return obj1.toString().equals(obj2.toString());
        }

        public static boolean equals(Object obj1, Object obj2) {
            return obj1.equals(obj2);
        }

        public static boolean eqeq(Object obj1, Object obj2) {
            return obj1 == obj2;
        }

        public static boolean pojo(Object obj1, Object obj2) {
            return obj1 == obj2;
        }

        public static boolean pojos(Object obj1, Object obj2) {
            return obj1 == obj2;
        }

        public static boolean pojoJson(Object obj1, Object obj2) {
            return obj1 == obj2;
        }

    }

    /**
     * 若干次对比：
     * 
     * <pre>
     * if eqType not empty: use eqType, all pass.
     * if eqType is empty: use compareTo or equals.
     * </pre>
     * 
     * @param obj1   NULLable
     * @param obj2   NULLable
     * @param eqType 对比方式数组
     * @return 对比结果
     */
    public static boolean eq(Object obj1, Object obj2, EqType... eqType) {
        if (ObjectCompare.compareNull(obj1, obj2) != null) {
            return ObjectCompare.compareNull(obj1, obj2);
        }
        if (eqType == null || eqType.length == 0) {
            return eq(obj1, obj2, EqType.compareTo) ? true : eq(obj1, obj2, EqType.equals);
        }
        boolean result = false;
        for (EqType itemEqType : eqType) {
            switch (itemEqType) {
            case compareTo:
                result = ObjectCompare.compareTo(obj1, obj2);
                break;
            case equals:
                result = ObjectCompare.equals(obj1, obj2);
                break;
            case toString:
                result = ObjectCompare.toString(obj1, obj2);
                break;
            case eqeq:
                result = ObjectCompare.eqeq(obj1, obj2);
                break;
            case pojo:
                result = ObjectCompare.pojo(obj1, obj2);
                break;
            case pojos:
                result = ObjectCompare.pojos(obj1, obj2);
                break;
            case pojoJson:
                result = ObjectCompare.pojoJson(obj1, obj2);
                break;
            default:
            }
            if (result == true) {
                return result;
            }
        }
        return result;
    }

    /**
     * 不相等对比
     * 
     * @param obj1   NULLable
     * @param obj2   NULLable
     * @param eqType 对比方式
     * @return 对比结果
     */
    public static boolean notEq(Object obj1, Object obj2, EqType[] eqType) {
        return !eq(obj1, obj2, eqType);
    }

    /**
     * 在其中（挨个对比直至发现true）
     * 
     * @param obj  要对比的项
     * @param objs 目标数组
     * @return 对比结果
     */
    public static boolean in(Object obj, Object... objs) {
        if (objs == null) {
            return false;
        }
        for (Object itemObject : objs) {
            if (eq(obj, itemObject)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 不在其中
     * 
     * @param obj  要对比的项
     * @param objs 目标数组
     * @return 对比结果
     */
    public static boolean notIn(Object obj, Object... objs) {
        return !in(obj, objs);
    }
}
