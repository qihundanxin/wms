package com.deer.wms.pdaApi.web;

import cn.hutool.core.util.StrUtil;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.PdaUser;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.pdaApi.constant.PdaApiConstant;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import com.deer.wms.project.root.util.NetworkUtil;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.system.manage.model.permission.Menu;
import com.deer.wms.system.manage.model.user.UserLogin;
import com.deer.wms.system.manage.service.LoginService;
import com.deer.wms.system.manage.service.PermissionService;
import com.deer.wms.ware.task.constant.WareTaskConstant;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * PDA用户信息接口
 * @author luolin
 **/
@ApiResultPacking
@RestController
@RequestMapping("/pda/user")
@RequiredArgsConstructor
public class PdaUserController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private PermissionService permissionService;

    @Data
    public static class UserFromVo{

        /**
         * 账号
         **/
        @NotBlank
        private String account;

        /**
         * 密码
         **/
        @NotBlank
        private String password;
    }

    /**
     * 登录
     * @author luolin
     * @param user
     * @param request
     * @return com.deer.wms.project.root.util.SimpleMap
     **/
    @PostMapping("/login")
    public SimpleMap pdaAppLogin(@RequestBody @Validated UserFromVo user, HttpServletRequest request) {
        UserLogin login = new UserLogin();
        BeanUtils.copyProperties(user, login);
        String ip = NetworkUtil.getIpAddress(request);
        login.setIp(ip);
        String token = loginService.pdaLogin(login);
        SimpleMap tokenMap = SimpleMap.init()
                .append("token", token)
                .append("batchScanLimit", WareTaskConstant.BATCH_SCAN_LIMIT);
        return tokenMap;
    }

    /**
     * 退出
     * @author luolin
     * @param currentUser
     * @return
     **/
    @PostMapping("/logout")
    @Authority("pda_user_logout")
    public void logout(@PdaUser CurrentUser currentUser){
        loginService.pdaLogout(currentUser);
    }

    @Data
    public static class UserInfoResultVo{

        /**
         * 用户id
         **/
        private Integer userId;

        /**
         * 用户登录账号
         */
        private String account;

        /**
         * 用户名
         **/
        private String userName;

        /**
         * 0否 1是 超级管理员可以无视权限列表配置，拥有全部权限
         **/
        private Integer isAdmin;
    }

    /**
     * 获取当前登录人信息
     * @author luolin
     * @param currentUser 当前登录人
     * @return com.deer.wms.pdaApi.web.PdaUserController.UserInfoResultVo
     **/
    @PostMapping("/getMyInfo")
    @Authority("pda_user_getMyInfo")
    public UserInfoResultVo getMyInfo(@PdaUser CurrentUser currentUser) {
        UserInfoResultVo result = new UserInfoResultVo();
        BeanUtils.copyProperties(currentUser, result);
        return result;
    }

    /**
     * 我的菜单
     * @author luolin
     * @param currentUser 当前登录人
     * @return com.deer.wms.project.root.util.SimpleMap
     **/
    @PostMapping("/getMyMenus")
    @Authority("pda_user_getMyMenus")
    public SimpleMap getMyMenus(@PdaUser CurrentUser currentUser){
        List<String> menuList = new ArrayList<>();
        List<Menu> menusOrigin = permissionService.findPermissionMenuByUserId(currentUser.getUserId());//查询到的是模块,页面，按钮级别所有数据
        for (Menu menu : menusOrigin) {
            if (menu.getResourceGroup() != null) {
                if (menu.getResourceGroup() == 5 && StrUtil.isNotBlank(menu.getCode())) {
                    menuList.add(menu.getCode());
                }
            }
        }
        SimpleMap menuMap = SimpleMap.init().append("menuList", menuList);
        return menuMap;
    }
}
