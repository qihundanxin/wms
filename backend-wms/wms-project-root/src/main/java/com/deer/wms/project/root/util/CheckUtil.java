package com.deer.wms.project.root.util;

import java.util.regex.Pattern;

import cn.hutool.core.util.IdcardUtil;

public class CheckUtil {
    /** 6~16位 ，字母开头，包含a-z A-Z 0-9 _ */
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{5,15}$");
    /** 6~16位 ，必须包含a-z A-Z 0-9 */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.*(?=.{6,16})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).*$");
    /**
     * <pre>
     *  
     *  电信  133,149,153,173,174,177,180,181,189,191,193,199
     *  移动  134,135,136,137,138,139,147,148,150,151,152,157,158,159,172,178,182,183,184,187,188,195,198
     *  联通  130,131,132,145,146,155,156,166,175,176,185,186,196
     *  广电  190,192,197
     *  电信虚拟    162,1700,1701,1702
     *  移动虚拟    165,1703,1705,1706
     *  联通虚拟    167,1704,1707,1708,1709,171
     * </pre>
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$");

    /**
     * 格式验证：电话
     * 
     * @param s 字符串
     * @return 验证结果
     */
    public static boolean isPhone(String s) {
        return PHONE_PATTERN.matcher(Np.i(s).o("")).matches();
    }

    /**
     * 格式验证：用户名
     * 
     * @param s 字符串
     * @return 验证结果
     */
    public static boolean isUsername(String s) {
        return USERNAME_PATTERN.matcher(Np.i(s).o("")).matches();
    }

    /**
     * 格式验证：密码
     * 
     * @param s 字符串
     * @return 验证结果
     */
    public static boolean isPassword(String s) {
        return PASSWORD_PATTERN.matcher(Np.i(s).o("")).matches();
    }

    /**
     * 格式验证：身份证号
     * 
     * @param s 字符串
     * @return 验证结果
     */
    public static boolean isIdcard(String s) {
        return IdcardUtil.isValidCard(Np.i(s).o(""));
    }
}
