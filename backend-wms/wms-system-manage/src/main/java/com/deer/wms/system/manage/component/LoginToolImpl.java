package com.deer.wms.system.manage.component;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.intercept.common.LoginTool;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.system.manage.model.permission.Menu;
import com.deer.wms.system.manage.model.user.UserDetail;
import com.deer.wms.system.manage.model.user.UserInfo;
import com.deer.wms.system.manage.service.PermissionService;
import com.deer.wms.system.manage.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName LoginToolImpl
 * @Description 获取登录信息工具类
 * @Author luolin
 * @Date 2023/6/30 15:19
 * @Version 1.0
 **/
@Component(value = "loginTool")
public class LoginToolImpl implements LoginTool {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PermissionService permissionService;
    @Override
    public CurrentUser getLoginUserInfo(String userId) {
        UserInfo userInfo = userInfoService.findById(Integer.parseInt(userId));
        UserDetail detail = userInfoService.findUserDetailByAccount(userInfo.getAccount());
        CurrentUser currentUser = new CurrentUser();
        BeanUtils.copyProperties(detail, currentUser);
        return currentUser;
    }

    @Override
    public List<String> getUserAuthByUserId(String userId) {
        List<Menu> menusOrigin = permissionService.findPermissionMenuByUserId(Integer.valueOf(userId));//查询到的是模块,页面，按钮级别所有数据
        List<String> newMenus = menusOrigin.stream().filter(x->x.getAuthorityCode()!=null).collect(Collectors.toList()).stream().map(x->x.getAuthorityCode()).collect(Collectors.toList());
        return newMenus;
    }
}
