package com.deer.wms.project.root.util;

import org.springframework.stereotype.Component;

/**
 * 和 SQL 相关的 String 字符串处理<br>
 * {@link Component}方便扩展，可能根据不同系统环境，使用不同匹配规则
 * 
 * @author xuesinuo 2020-09-25
 *
 */
public class SqlUtil {
    /**
     * 【仅限MySQL】用户输入的字符转为Like字符：<br>
     * 1.去除两边空格<br>
     * 2.将_%转义<br>
     * 3.将空格设置为%<br>
     * <br>
     * 此方法决定like查询的匹配习惯
     * 
     * @param column 用户输入的模糊查询条件
     * @return 输出的like查询
     */
    public static String like(String column) {
        if (column == null || column.trim().equals("")) {
            return null;
        }
        return column.trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("_", "\\\\_").replaceAll("%", "\\\\%").replaceAll(" +", "%");
    }

    /**
     * 左侧%的like条件，不使用索引，不推荐！
     * 
     * @param column 用户输入的模糊查询条件
     * @return 输出的like查询
     */
    public static String likeL(String column) {
        if (column == null || column.trim().equals("")) {
            return null;
        }
        return "%" + like(column);
    }

    /**
     * 右侧%的like条件
     * 
     * @param column 用户输入的模糊查询条件
     * @return 输出的like查询
     */
    public static String likeR(String column) {
        if (column == null || column.trim().equals("")) {
            return null;
        }
        return like(column) + "%";
    }

    /**
     * 左右侧%的like条件，不使用索引，不推荐！
     * 
     * @param column 用户输入的模糊查询条件
     * @return 输出的like查询
     */
    public static String likeLR(String column) {
        if (column == null || column.trim().equals("")) {
            return null;
        }
        return "%" + like(column) + "%";
    }
}