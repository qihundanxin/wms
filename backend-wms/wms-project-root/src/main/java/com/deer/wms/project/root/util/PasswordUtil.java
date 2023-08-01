package com.deer.wms.project.root.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具
 * 
 * 此工具在2022年8月引入，还未使用在密码加解密中
 * 
 * @author xuesinuo
 *
 */
public class PasswordUtil {
    private static final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

    /**
     * 密码加密
     * 
     * @param password 密码
     * @return 加密编码后的密码
     */
    public static String bcrypt(String password) {
        return bCryptEncoder.encode(password);
    }

    /**
     * 
     * @param password        被验证的密码（用户本次填写的密码）
     * @param passwordEncoded 编码后的密码（存储的密码）
     * @return 密码是否正确
     */
    public static boolean bcryptCheck(String password, String passwordEncoded) {
        return bCryptEncoder.matches(password, passwordEncoded);
    }
}
