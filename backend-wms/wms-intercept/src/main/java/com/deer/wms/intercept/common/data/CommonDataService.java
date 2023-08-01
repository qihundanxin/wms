package com.deer.wms.intercept.common.data;

import java.util.List;

/**
 * Created by guo on 2017/9/29.
 */
public interface CommonDataService {

    /**
     * 将当前登录用户信息缓存到 Redis
     *
     * @param currentUser 当前登录用户信息
     */
    void putCurrentUserDataToRedis(CurrentUser currentUser);

    /**
     * 从 Redis 中移除当前用户数据
     *
     * @param token 缓存当前用户数据的key
     */
    void removeCurrentUserDataFromRedis(String token);

    /**
     * 从 Redis 中获取当前用户数据
     *
     * @param token 缓存当前用户数据的key
     * @return 返回当前用户数据
     */
    CurrentUser getCurrentUserDataFromRedis(String token);

    /**
     * @Author luolin
     * @Description 移除用户权限
     * @Date 13:52 2023/6/30
     * @param userId
     * @return
     **/
    void removeAuthDataFromRedis(String userId);

    /**
     * @Author luolin
     * @Description 根据token获取userId
     * @Date 14:44 2023/6/30
     * @param token
     * @return java.lang.String
     **/
    String getUserIdByToken(String token);

    /**
     * @Author luolin
     * @Description 根据用户id获取用户缓存
     * @Date 17:02 2023/6/30
     * @param userId
     * @return com.deer.wms.intercept.common.data.CurrentUser
     **/
    CurrentUser getCurrentUserDataFromRedisByUserId(String userId);

    /**
     * @Author luolin
     * @Description 根据用户id移除用户信息
     * @Date 13:52 2023/6/30
     * @param userId
     * @return
     **/
    void removeCurrentUserDataFromRedisByUserId(String userId);

    /**
     * @Author luolin
     * @Description 根据用户id获取用户权限
     * @Date 13:52 2023/6/30
     * @param userId
     * @return java.util.List<java.lang.String>
     **/
    List<String> getAuthDataFromRedis(String userId);

    /**
     * @Author luolin
     * @Description PDA缓存用户信息
     * @Date 17:12 2023/7/4
     * @param currentUser
     **/
    void putPdaCurrentUserDataToRedis(CurrentUser currentUser);

    /**
     * @Author luolin
     * @Description PDA根据用户id获取用户缓存
     * @Date 17:02 2023/6/30
     * @param userId
     * @return com.deer.wms.intercept.common.data.CurrentUser
     **/
    CurrentUser getPdaCurrentUserDataFromRedisByUserId(String userId);

    /**
     * @Author luolin
     * @Description PDA根据用户id获取用户权限
     * @Date 13:52 2023/6/30
     * @param userId
     * @return java.util.List<java.lang.String>
     **/
    List<String> getPdaAuthDataFromRedis(String userId);

    /**
     * @Author luolin
     * @Description PDA根据token获取CurrentUser
     * @Date 9:19 2023/7/5
     * @param token
     * @return com.deer.wms.intercept.common.data.CurrentUser
     **/
    CurrentUser getPdaCurrentUserDataFromRedis(String token);

    /**
     * @Author luolin
     * @Description PDA根据token移除用户id
     * @Date 11:18 2023/7/5
     * @param token
     * @return
     **/
    void removePdaUserIdFromRedis(String token);

    /**
     * @Author luolin
     * @Description PDA根据用户id移除currentUser
     * @Date 11:19 2023/7/5
     * @param userId
     * @return
     **/
    void removePdaCurrentUserFromRedis(String userId);

    /**
     * @Author luolin
     * @Description PDA根据用户id移除用户权限
     * @Date 11:20 2023/7/5
     * @param userId
     * @return
     **/
    void removePdaAuthFromRedis(String userId);

    /**
     * @Author luolin
     * @Description PDA根据token获取用户id
     * @Date 13:38 2023/7/5
     * @param token
     * @return java.lang.String
     **/
    String getPdaUserIdByToken(String token);

    /**
     * @Author luolin
     * @Description //TODO
     * @Date 11:36 2023/7/7
     * @param userId
     * @return
     **/
    void removePdaToken(String userId);
}
