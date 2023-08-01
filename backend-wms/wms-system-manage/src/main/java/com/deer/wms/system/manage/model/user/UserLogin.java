package com.deer.wms.system.manage.model.user;

import lombok.ToString;

/**
 * 用户登录的信息封装类
 * <p>
 * Created by guo on 2018/8/11.
 */
@ToString
public class UserLogin {

    /**
     * 登录账号
     */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 验证码
     */
    private String validateCode;

    /**
     * 用户登录的ip地址
     */
    private String ip;

    /**
     * 客户端
     **/
    private String client;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
