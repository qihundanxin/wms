package com.deer.wms.system.manage.service.impl;

import com.deer.wms.intercept.common.data.CommonDataServiceManager;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.SignatureUtils;
import com.deer.wms.system.manage.model.permission.Menu;
import com.deer.wms.system.manage.model.user.UserDetail;
import com.deer.wms.system.manage.model.user.UserInfo;
import com.deer.wms.system.manage.model.user.UserLogin;
import com.deer.wms.system.manage.service.LoginService;
import com.deer.wms.system.manage.service.PermissionService;
import com.deer.wms.system.manage.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by guo on 2018/8/11.
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PermissionService permissionService;

    @Autowired()
    private CommonDataServiceManager commonDataService;

    @Override
    @Transactional
    public UserDetail login(UserLogin login) {
        //验证账号和密码是否正确，以及账号状态是否正常，通过验证后获取登录的账号信息
        UserInfo userInfo = userInfoService.validate(login.getAccount(), login.getPassword());
        //生成登录用户访问系统和api接口的token
        String[] tokenParams = {userInfo.getAccount(), String.valueOf(System.currentTimeMillis())};
        String token = SignatureUtils.generator(tokenParams);

        //初始化登录用户的详细信息
        UserDetail detail = userInfoService.findUserDetailByAccount(userInfo.getAccount());
        detail.setToken(token);
        detail.setPermission(permissionService.findPermissionCodeByUserId(detail.getUserId()));
        List<Menu> menusOrigin = permissionService.findPermissionMenuByUserId(detail.getUserId());//查询到的是模块,页面，按钮级别所有数据
        List<Menu> menus =menusOrigin.stream().filter(x->("F".equals(x.getResourceType()) || "M".equals(x.getResourceType()))).collect(Collectors.toList());
        List<Menu> authMenus =menusOrigin.stream().filter(x->x.getAuthorityCode()!=null).collect(Collectors.toList());
        detail.setMenus(menus);
        detail.setUrls(permissionService.findPermissionUrlByUserId(detail.getUserId()));
        //初始化当前用户数据并将当前用户数据放入缓存
        //TODO 将用户拥有的权限标识一并放入缓存
        List<String> newMenus = authMenus.stream().map(x->x.getAuthorityCode()).collect(Collectors.toList());
        CurrentUser currentUser = new CurrentUser();
        BeanUtils.copyProperties(detail, currentUser);
        currentUser.setAccount(userInfo.getAccount()); //用户登录账号就是手机号
        currentUser.setAuthMenus(newMenus);
        currentUser.setIp(login.getIp());
        currentUser.setToken(token);
        currentUser.setIconUrl(detail.getIconUrl());
        currentUser.setDeptId(detail.getDeptId());
        currentUser.setIsAdmin(detail.getIsAdmin());
        currentUser.setCurrentLoginTime(DateUtils.dateToStr(new Date(), DateUtils.DEFAULT_DATETIME_FORMAT));
        commonDataService.putCurrentUserDataToRedis(currentUser);

        return detail;
    }

    @Override
    public String pdaLogin(UserLogin login) {
        //验证账号和密码是否正确，以及账号状态是否正常，通过验证后获取登录的账号信息
        UserInfo userInfo = userInfoService.validate(login.getAccount(), login.getPassword());
        //移除上次登录的token
        commonDataService.removePdaToken(userInfo.getUserId().toString());
        //生成登录用户访问系统和api接口的token
        String[] tokenParams = {userInfo.getAccount(), String.valueOf(System.currentTimeMillis())};
        String token = SignatureUtils.generator(tokenParams);

        //初始化登录用户的详细信息
        UserDetail detail = userInfoService.findUserDetailByAccount(userInfo.getAccount());
        detail.setToken(token);
        List<Menu> menusOrigin = permissionService.findPermissionMenuByUserId(detail.getUserId());//查询到的是模块,页面，按钮级别所有数据
        List<Menu> authMenus =menusOrigin.stream().filter(x->x.getAuthorityCode()!=null).collect(Collectors.toList());
        List<String> newMenus = authMenus.stream().map(x->x.getAuthorityCode()).collect(Collectors.toList());
        CurrentUser currentUser = new CurrentUser();
        BeanUtils.copyProperties(detail, currentUser);
        currentUser.setAccount(userInfo.getAccount()); //用户登录账号就是手机号
        currentUser.setAuthMenus(newMenus);
        currentUser.setIp(login.getIp());
        currentUser.setToken(token);
        currentUser.setIconUrl(detail.getIconUrl());
        currentUser.setDeptId(detail.getDeptId());
        currentUser.setIsAdmin(detail.getIsAdmin());
        currentUser.setCurrentLoginTime(DateUtils.dateToStr(new Date(), DateUtils.DEFAULT_DATETIME_FORMAT));
        commonDataService.putPdaCurrentUserDataToRedis(currentUser);
        return token;
    }

    /**
     * 初始化当前用户数据
     *
     * @param user            用户基本信息
     * @param currentDateTime 当前登录系统的时间
     * @param ip              登录系统的ip地址
     * @return 返回当前用户数据
     */
    private CurrentUser initCurrentUserData(UserInfo user, Date currentDateTime, String ip) {
        CurrentUser currentUser = new CurrentUser();
        currentUser.setUserId(user.getUserId());
        currentUser.setUserName(user.getUserName());
        currentUser.setAccount(user.getAccount());
        currentUser.setIp(ip);

        //设置本次登录时间(格式转换)
        String currentLoginTime = DateUtils.dateToStr(currentDateTime, DateUtils.DEFAULT_DATETIME_FORMAT);
        currentUser.setCurrentLoginTime(currentLoginTime);

        //设置登录用户访问系统和api接口的token
        String[] tokenParams = {user.getAccount(), String.valueOf(System.currentTimeMillis())};
        currentUser.setToken(SignatureUtils.generator(tokenParams));

        return currentUser;
    }

    @Override
    public void pdaLogout(CurrentUser currentUser) {
        commonDataService.removePdaUserIdFromRedis(currentUser.getToken());
        commonDataService.removePdaCurrentUserFromRedis(String.valueOf(currentUser.getUserId()));
        commonDataService.removePdaAuthFromRedis(String.valueOf(currentUser.getUserId()));
    }

}