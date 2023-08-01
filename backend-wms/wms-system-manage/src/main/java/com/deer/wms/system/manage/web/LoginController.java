package com.deer.wms.system.manage.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.NetworkUtil;
import com.deer.wms.system.manage.model.permission.Menu;
import com.deer.wms.system.manage.model.user.UserDetail;
import com.deer.wms.system.manage.model.user.UserLogin;
import com.deer.wms.system.manage.service.LoginService;

/**
 * 用户登录api接口
 * 
 * Created by guo on 2017/9/16.
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     */
    @PostMapping
    public Result userLogin(@RequestBody UserLogin login, HttpServletRequest request) {

        String ip = NetworkUtil.getIpAddress(request);
        login.setIp(ip);
        login.setClient("WMS");
        UserDetail detail = loginService.login(login);
        List<Menu> menus = detail.getMenus();
        List<Menu> menus1 = new ArrayList<Menu>();
        List<Menu> menus2 = new ArrayList<Menu>();
        List<Menu> menus3 = new ArrayList<Menu>();
        List<Menu> pda = new ArrayList<Menu>();
        for (Menu menu : menus) {
            if (menu.getResourceGroup() != null) {
                if (menu.getResourceGroup() == 1) {
                    menus1.add(menu);
                }
                if (menu.getResourceGroup() == 2) {
                    menus2.add(menu);
                }
                if (menu.getResourceGroup() == 3) {
                    menus3.add(menu);
                }
//                if (menu.getResourceGroup() == 4) {
//                    pda.add(menu);
//                }

            }

        }

        detail.setMenus(null);
        detail.setMenus1(menus1);
        detail.setMenus2(menus2);
        detail.setMenus3(menus3);
        // detail.setMenus3(pda);
        return ResultGenerator.genSuccessResult(detail);
    }

    /**
     * 用户登录
     */
    @PostMapping("/pdaLogin")
    @Authority("login_pdaLogin")
    public Result pdaLogin(@RequestBody UserLogin login, HttpServletRequest request) {

        String ip = NetworkUtil.getIpAddress(request);
        login.setIp(ip);
        login.setClient("PDA");
        UserDetail detail = loginService.login(login);
        List<Menu> menus = detail.getMenus();

        List<Menu> pdaMemus = new ArrayList<Menu>();
        for (Menu menu : menus) {
            if (menu.getResourceGroup() != null) {

                if (menu.getResourceGroup() == 4) {
                    pdaMemus.add(menu);
                }

            }

        }

        detail.setMenus(null);

        detail.setMenus4(pdaMemus);
        return ResultGenerator.genSuccessResult(detail);
    }

    /**
     * 用户登录
     */
    @PostMapping("/loginWMS")
    @Authority("login_loginWMS")
    public Result loginWMS(@RequestBody UserLogin login, HttpServletRequest request) {
        /*
         * String macNum = MacUtil.getLocatMac(); if(!macNum.equals("60-30-D4-65-C4-70") ){ throw new ServiceException(CommonCode.ILLEGAL_LANDING,null); }
         */
        String ip = NetworkUtil.getIpAddress(request);
//        login.setIp("122.112.219.208");
        login.setIp(ip);
        UserDetail detail = loginService.login(login);
        UserDetail detail2 = new UserDetail();
        detail2.setToken(detail.getToken());

        return ResultGenerator.genSuccessResult(detail2);
    }

//    @GetMapping("/findOnlineUsers")
//    public Result userLogin2() {
//        List<Object> list = redisUtil.findByPattern("AccessToken*");
//        List<CurrentUser> currentUsers = new ArrayList<>();
//        list.forEach(o -> {
//            currentUsers.add((CurrentUser) o);
//        });
//
//        return ResultGenerator.genSuccessResult(currentUsers);
//    }

}
