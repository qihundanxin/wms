package com.deer.wms.project.root.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

public class StringUtil {
    public static final String SEPARATOR = ",";
    private static byte ba = 'a';
    private static byte bz = 'z';
    private static byte bA = 'A';
    private static byte bZ = 'Z';
    private static byte b0 = '0';
    private static byte b9 = '9';
    private static byte b_ = '_';

    /**
     * 以toString()处理，列表转字符串
     * 
     * @param <T>       数据类型
     * @param arg       源
     * @param separator 分隔符
     * @return 转换成的字符
     */
    public static <T> String list2String(Iterable<T> arg, String separator) {
        if (arg == null) {
            return null;
        }
        if (separator == null) {
            separator = SEPARATOR;
        }
        StringBuilder sb = new StringBuilder();
        for (T t : arg) {
            String tString;
            if (StrUtil.isNotBlank(tString = t.toString())) {
                sb.append(tString).append(separator);
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 以toString()处理，数组转字符串
     * 
     * @param <T>       数据类型
     * @param arg       源
     * @param separator 分隔符
     * @return 转换成的字符
     */
    public static <T> String array2String(Object[] arg, String separator) {
        if (arg == null) {
            return null;
        }
        if (separator == null) {
            separator = SEPARATOR;
        }
        StringBuilder sb = new StringBuilder();
        for (Object t : arg) {
            String tString;
            if (StrUtil.isNotBlank(tString = t.toString())) {
                sb.append(tString).append(separator);
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 字符串转数组
     * 
     * @param s         源字符
     * @param separator 分隔符
     * @return 转换成的字符数组
     */
    public static List<String> string2Array(String s, String separator) {
        if (s == null) {
            return null;
        }
        if (separator == null) {
            separator = SEPARATOR;
        }
        String[] array = s.split(separator);
        List<String> result = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++) {
            String item = StrUtil.trim(array[i]);
            if (StrUtil.isNotBlank(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 位置
     */
    public enum Seat {
        /** 左端 */
        LEFT,
        /** 右端 */
        RIGHT
    }

    /**
     * 模板化字符串（字符串对齐）
     * 
     * @param source 源
     * @param length 对齐长度
     * @param fill   填充
     * @param seat   填充或截取的位置
     * @return 字符串对齐
     */
    public static String format(String source, int length, char fill, Seat seat) {
        if (source == null) {
            source = "";
        }
        if (seat == null) {
            seat = Seat.LEFT;
        }
        int fillIndex = length - source.length();
        if (fillIndex > 0) {
            // 填充
            StringBuilder result = new StringBuilder();
            if (seat == Seat.LEFT) {
                Np.i(result).doo(x -> x.append(fill), fillIndex);
            }
            result.append(source);
            if (seat == Seat.RIGHT) {
                Np.i(result).doo(x -> x.append(fill), fillIndex);
            }
            return result.toString();
        }
        if (fillIndex < 0) {
            // 截取
            if (seat == Seat.LEFT) {
                return source.substring(source.length() - length, source.length());
            }
            if (seat == Seat.RIGHT) {
                return source.substring(0, length);
            }
        }
        return source;
    }

    /**
     * Map转Form表单格式字符串
     * 
     * @param map map
     * @return 表单字符串
     */
    public static String toFormString(Map<?, ?> map, boolean urlEncode) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object val = entry.getValue();
            if (result.length() > 0) {
                result.append("&");
            }
            if (urlEncode) {
                val = URLUtil.encode(val.toString());
            }
            result.append(key).append("=").append(val);
        }
        return result.toString();
    }

    /**
     * 将对象中String属性 去掉前后空格
     *
     * @param model
     */
    public static void trimObjectStringProperties(Object model) {
        try {
            if (model instanceof String) {
                ((String) model).trim();
            } else {
                Class<?> clazz = model.getClass();
                Field[] field = clazz.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
                for (int j = 0; j < field.length; j++) { // 遍历所有属性
                    String name = field[j].getName(); // 获取属性的名字
                    // System.out.println("attribute name:"+name);
                    name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                    String type = field[j].getGenericType().toString(); // 获取属性的类型
                    if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                        Method m = model.getClass().getMethod("get" + name);
                        String value = (String) m.invoke(model); // 调用getter方法获取属性值
                        if (value != null) {
                            String methodStr = "set" + name.toUpperCase().substring(0, 1) + name.substring(1);
                            Method method = clazz.getMethod(methodStr, new Class[] { field[j].getType() });
                            method.invoke(model, value.trim());// 处理：将值trim
                        }
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符串内容在【a~z A~Z 0~9 _】范围之中<br>
     * 
     * 提高效率，使用ASCII，不使用正则
     * 
     * @return
     */
    public static boolean inAZaz09_(String s) {
        for (byte b : s.getBytes()) {
            if (ba <= b && b <= bz || bA <= b && b <= bZ || b0 <= b && b <= b9 || b == b_) {} else {
                return false;
            }
        }
        return true;
    }
}
