package com.deer.wms.system.manage.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CommonDataServiceManager;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

/**
 * 退出系统api接口
 * <p>
 * Created by guo on 2017/9/17.
 */
@Authority
@RestController
@RequestMapping("/signout")
public class SignOutController {

    /**
     * 用户信息管理的业务处理接口
     */
    @Autowired
    private CommonDataServiceManager commonDataService;

    /**
     * 当前登录用户退出系统
     *
     * @param currentUser 当前操作用户信息
     * @return
     */
    @PostMapping()
    @Authority("signout_out")
    public Result signout(@User CurrentUser currentUser) {
        commonDataService.removeCurrentUserDataFromRedis(currentUser.getToken());
        return ResultGenerator.genSuccessResult();
    }
}
