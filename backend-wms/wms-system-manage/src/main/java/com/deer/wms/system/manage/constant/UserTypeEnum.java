/**
 * Copyright (c) 2020 All Rights Reserved.
 */
package com.deer.wms.system.manage.constant;

/**
 * @author owen
 * @version $$Id: UserTypeEnum, v 0.1 2021/3/9 下午5:08 owen Exp $$
 */
public enum UserTypeEnum {

    OPERATE_USER(1, "操作员工"),

    ISV_APP(2, "APP");

    private UserTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;

    private String desc;
}
