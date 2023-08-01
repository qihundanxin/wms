package com.deer.wms.system.manage.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.system.manage.model.user.UserDetail;
import com.deer.wms.system.manage.model.user.UserLogin;

/**
 * Created by guo on 2018/8/11.
 */
public interface LoginService {

    /**
     * 用户登录
     *
     * @param login 登录信息
     * @return 登录成功后的用户详细信息
     */
    UserDetail login(UserLogin login);

    /**
     * @Author luolin
     * @Description pda App登录
     * @Date 17:04 2023/7/4
     * @param login
     * @return com.deer.wms.system.manage.model.user.UserDetail
     **/
    String pdaLogin(UserLogin login);

    /**
     * @Author luolin
     * @Description PDA退出
     * @Date 10:12 2023/7/5
     * @param currentUser
     **/
    void pdaLogout(CurrentUser currentUser);
}
