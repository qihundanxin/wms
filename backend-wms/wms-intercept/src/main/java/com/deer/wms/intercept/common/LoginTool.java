package com.deer.wms.intercept.common;

import com.deer.wms.intercept.common.data.CurrentUser;

import java.util.List;

/**
 * @ClassName LoginTool
 * @Description 登录工具，需要有具体的实现，已完成登录需要的功能
 * @Author dell
 * @Date 2023/6/30 15:12
 * @Version 1.0
 **/
public interface LoginTool {
    /**
     * @Author luolin
     * @Description 根据用户id查询登录用户信息
     * @Date 15:14 2023/6/30
     * @param userId
     * @return com.deer.wms.intercept.common.data.CurrentUser
     **/
    CurrentUser getLoginUserInfo(String userId);

    /**
     * @Author luolin
     * @Description 根据用户id获取用户权限
     * @Date 17:20 2023/7/1
     * @param userId
     * @return java.util.List<java.lang.String>
     **/
    List<String> getUserAuthByUserId(String userId);
}
