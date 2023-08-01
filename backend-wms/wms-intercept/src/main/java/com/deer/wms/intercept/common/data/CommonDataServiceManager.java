package com.deer.wms.intercept.common.data;

import com.deer.wms.intercept.common.LoginTool;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deer.wms.intercept.dao.User2Mapper;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.util.CacheTool;

import java.util.List;

/**
 * Created by guo on 2017/9/29.
 */
@Service(value = "commonDataService")
public class CommonDataServiceManager implements CommonDataService {

    @Autowired
    private CacheTool cache;

    @Autowired
    private User2Mapper user2Mapper;

    @Autowired
    private LoginTool loginTool;

    @Override
    public void putCurrentUserDataToRedis(CurrentUser currentUser) {
        if (null == currentUser || StringUtils.isEmpty(currentUser.getToken())) {
            return;
        }

        String token = currentUser.getToken();
        String userId = String.valueOf(currentUser.getUserId());
        List<String> authMenus = currentUser.getAuthMenus();
        currentUser.setAuthMenus(null);
        cache.setObject(CacheKey.NEW_ACCESS_TOKEN, token,userId, CacheTime.ACCESS_TOKEN);
        cache.setObject(CacheKey.NEW_ACCESS_USERID, userId, currentUser, CacheTime.ACCESS_TOKEN);
        cache.setObject(CacheKey.USER_AUTH, userId, authMenus, CacheTime.ACCESS_TOKEN);
    }

    @Override
    public void removeCurrentUserDataFromRedis(String key) {
        cache.del(CacheKey.NEW_ACCESS_TOKEN, key);
    }

    @Override
    public CurrentUser getCurrentUserDataFromRedis(String token) {
        String userId = cache.getObject(CacheKey.NEW_ACCESS_TOKEN, token);
        CurrentUser currentUser = new CurrentUser();
        if (StringUtils.isNotEmpty(userId)){
            currentUser = cache.getObject(CacheKey.NEW_ACCESS_USERID, userId);
            if (currentUser == null){
                currentUser = loginTool.getLoginUserInfo(userId);
            }
            List<String> authMenu = cache.getObject(CacheKey.USER_AUTH, userId);
            if (authMenu == null){
                authMenu = loginTool.getUserAuthByUserId(userId);
            }
            currentUser.setAuthMenus(authMenu);
        }else{
            throw new ServiceException(CommonCode.TOKEN_INVALID);
        }
//        if (currentUser == null) {
//            UserInfo2 userInfo2 = user2Mapper.findByToken(token);
//            if (userInfo2 != null) {
//                currentUser = new CurrentUser();
//                currentUser.setUserId(userInfo2.getUserId());
//                currentUser.setUserName(userInfo2.getUserName());
//            }
//        }

        return currentUser;
    }

    @Override
    public void removeAuthDataFromRedis(String userId) {
        cache.del(CacheKey.USER_AUTH, userId);
    }

    @Override
    public String getUserIdByToken(String token) {
        return cache.getObject(CacheKey.NEW_ACCESS_TOKEN, token);
    }

    @Override
    public CurrentUser getCurrentUserDataFromRedisByUserId(String userId) {
        CurrentUser currentUser = cache.getObject(CacheKey.NEW_ACCESS_USERID, userId);
        return currentUser;
    }

    @Override
    public void removeCurrentUserDataFromRedisByUserId(String userId) {
        cache.del(CacheKey.NEW_ACCESS_USERID, userId);
    }

    @Override
    public List<String> getAuthDataFromRedis(String userId) {
        List<String> authMenus = cache.getObject(CacheKey.USER_AUTH, userId);
        return authMenus;
    }

    @Override
    public void putPdaCurrentUserDataToRedis(CurrentUser currentUser) {
        if (null == currentUser || StringUtils.isEmpty(currentUser.getToken())) {
            return;
        }

        String token = currentUser.getToken();
        String userId = String.valueOf(currentUser.getUserId());
        List<String> authMenus = currentUser.getAuthMenus();
        currentUser.setAuthMenus(null);
        cache.setObject(CacheKey.PDA_ACCESS_TOKEN, token,userId, CacheTime.ACCESS_TOKEN);
        cache.setObject(CacheKey.PDA_ACCESS_USERID, userId, currentUser, CacheTime.ACCESS_TOKEN);
        cache.setObject(CacheKey.PDA_USER_AUTH, userId, authMenus, CacheTime.ACCESS_TOKEN);
    }

    @Override
    public CurrentUser getPdaCurrentUserDataFromRedisByUserId(String userId) {
        CurrentUser currentUser = cache.getObject(CacheKey.PDA_ACCESS_USERID, userId);
        return currentUser;
    }

    @Override
    public List<String> getPdaAuthDataFromRedis(String userId) {
        List<String> authMenus = cache.getObject(CacheKey.PDA_USER_AUTH, userId);
        return authMenus;
    }

    @Override
    public CurrentUser getPdaCurrentUserDataFromRedis(String token) {
        String userId = cache.getObject(CacheKey.PDA_ACCESS_TOKEN, token);
        CurrentUser currentUser = new CurrentUser();
        if (StringUtils.isNotEmpty(userId)){
            currentUser = cache.getObject(CacheKey.PDA_ACCESS_USERID, userId);
            if (currentUser == null){
                currentUser = loginTool.getLoginUserInfo(userId);
            }
            List<String> authMenu = cache.getObject(CacheKey.PDA_USER_AUTH, userId);
            if (authMenu == null){
                authMenu = loginTool.getUserAuthByUserId(userId);
            }
            currentUser.setAuthMenus(authMenu);
        }else{
            throw new ServiceException(CommonCode.TOKEN_INVALID);
        }

        return currentUser;
    }

    @Override
    public void removePdaUserIdFromRedis(String token) {
        cache.del(CacheKey.PDA_ACCESS_TOKEN, token);
    }

    @Override
    public void removePdaCurrentUserFromRedis(String userId) {
        cache.del(CacheKey.PDA_ACCESS_USERID, userId);
    }

    @Override
    public void removePdaAuthFromRedis(String userId) {
        cache.del(CacheKey.PDA_USER_AUTH, userId);
    }

    @Override
    public String getPdaUserIdByToken(String token){
        return cache.getObject(CacheKey.PDA_ACCESS_TOKEN, token);
    }

    @Override
    public void removePdaToken(String userId){
        CurrentUser currentUser = this.getPdaCurrentUserDataFromRedisByUserId(userId);
        if (currentUser != null){
            cache.del(CacheKey.PDA_ACCESS_TOKEN, currentUser.getToken());
        }
    }
}
