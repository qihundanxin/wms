package com.deer.wms.system.manage.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.Constants;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.system.manage.model.user.UserData;
import com.deer.wms.system.manage.model.user.UserDetail;
import com.deer.wms.system.manage.model.user.UserInfo;
import com.deer.wms.system.manage.model.user.UserInfoCreate;
import com.deer.wms.system.manage.model.user.UserInfoCriteria;
import com.deer.wms.system.manage.model.user.UserInfoDetailVO;
import com.deer.wms.system.manage.model.user.UserInfoDto;
import com.deer.wms.system.manage.model.user.UserInfoModify;
import com.deer.wms.system.manage.model.user.UserPassword;
import com.deer.wms.system.manage.model.user.UserPasswordRetrieval;
import com.deer.wms.system.manage.service.UserInfoService;
import com.deer.wms.system.manage.service.UserOrganizationService;
import com.deer.wms.system.manage.service.UserRoleService;
import com.deer.wms.system.manage.service.WareUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 用户信息api接口
 * <p>
 * Created by guo on 2017/10/01.
 */
@RestController
@RequestMapping("/users")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private WareUserService wareUserService;

    @Autowired
    private UserOrganizationService userOrganizationService;

    /**
     * 分页查账户信息
     *
     * @param criteria
     * @return
     */
    @GetMapping("/userList")
    @Authority("users_userList")
    public Result list(UserInfoCriteria criteria, @User CurrentUser currentUser) {
        if (currentUser.getIsAdmin() == 1) {
            criteria.setIsAdmin(1);
        } else {
            criteria.setIsAdmin(0);
        }
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<UserInfoDto> list = userInfoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

//    public Result userList(UserInfoCriteria criteria, @User CurrentUser currentUser) {
//    	if(currentUser==null){
//            throw new ServiceException( CommonCode.SERVICE_ERROR,"未登录错误",null );
//        }
//    	StringUtil.trimObjectStringProperties(criteria);
//    	if (currentUser.getCompanyType() != SystemManageConstant.COMPANY_TYPE_OPERATE){
//    		criteria.setCompanyId(currentUser.getCompanyId());
//		}
//        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
//        List<UserListVO> list = userInfoService.findUserFormList(criteria);
//        return ResultGenerator.genSuccessResult(new PageInfo<>(list));
//    }

    /**
     * 用户修改自己的密码
     *
     * @param userPassword
     * @param currentUser
     * @return
     */
    @PostMapping("/modifyPassword")
    @Authority("users_modifyPassword")
    public Result modifyPassword(@RequestBody UserPassword userPassword, @User CurrentUser currentUser) {
        if (!userPassword.getPassword().equals(userPassword.getConfirmPassword())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "确认密码与新密码不一致");
        }
        userInfoService.modifyPassword(userPassword, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 找回密码，设置密码
     *
     * @param retrieval
     * @return
     */
    @PutMapping("/retrieval/password")
    @Authority("users_retrieval_password")
    public Result retrievalPassword(@Valid @RequestBody UserPasswordRetrieval retrieval) {
        userInfoService.retrievalPassword(retrieval);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 获取当前登录用户的信息
     *
     * @param currentUser 当前登录用户信息
     * @return
     */
    @GetMapping("/info")
    @Authority("users_get")
    public Result getCurrentUserDetail(@User CurrentUser currentUser) {
        UserDetail detail = userInfoService.findUserDetailByAccount(currentUser.getAccount());
        detail.setToken(currentUser.getToken());
        return ResultGenerator.genSuccessResult(detail);
    }

    /**
     * 用户修改自己的资料信息
     *
     * @param userData
     * @param currentUser
     * @return
     */
    @PostMapping("/info")
    @Authority("users_update")
    public Result modifyInfo(UserData userData, HttpServletRequest request, @User CurrentUser currentUser) {
        userInfoService.modifyUserData(userData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 新增账户信息
     *
     * @param create      账户信息
     * @param currentUser 当前操作人员信息
     * @return
     */
    @PostMapping
    @Authority("users_add")
    public Result createUserInfo(@Valid @RequestBody UserInfoCreate create, @User CurrentUser currentUser) {
        List<UserInfo> list = userInfoService.findByAccount(create.getAccount());
        if (list.size() != 0) {
            throw new ServiceException(CommonCode.ACCOUNT_EXISTS);
        }
        userInfoService.createUserInfo(create, currentUser);
        return ResultGenerator.genSuccessResult();

    }

    /**
     * 修改账户信息
     *
     * @param modify      账户信息
     * @param currentUser 当前操作人员信息
     * @return
     */
    @PostMapping("/modifyUserInfo")
    @Authority("users_modifyUserInfo")
    public Result modifyUserInfo(@RequestBody UserInfoModify modify, @User CurrentUser currentUser) {

        String account = modify.getAccount();
        List<UserInfo> userInfoList = userInfoService.findByAccount(account);

        if (userInfoList.size() == 1 && userInfoList.get(0).getUserId().equals(modify.getUserId())) {
            userInfoService.modifyUserInfo(modify, currentUser);
            return ResultGenerator.genSuccessResult();
        } else if (userInfoList.size() == 0) {
            userInfoService.modifyUserInfo(modify, currentUser);
            return ResultGenerator.genSuccessResult();
        } else {
            throw new ServiceException(CommonCode.ACCOUNT_EXISTS);
        }

    }

    /**
     * 获取账户详细信息
     *
     * @param userId 账户信息id
     * @return
     */
    @GetMapping("/{userId}/info")
    @Authority("users_info")
    public Result getUserInfoDetail(@PathVariable Integer userId) {
        UserInfoDetailVO vo = userInfoService.findUserInfoDetailByUserId(userId);
        return ResultGenerator.genSuccessResult(vo);
    }

    /**
     * 启用账户
     *
     * @param userId 用户信息id
     * @return
     */
    @GetMapping("/enable")
    @Authority("users_enable")
    public Result enable(Integer userId, @User CurrentUser currentUser) {
//        UserInfo userInfo = userInfoService.findById(userId);

        userInfoService.modifyUserInfoAccountState(userId, Constants.ACCOUNT_STATUS_ENABLE, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 停用账户
     *
     * @param userId 用户信息id
     * @return
     */
    @GetMapping("/disable")
    @Authority("users_disable")
    public Result disable(Integer userId, @User CurrentUser currentUser) {
//        UserInfo userInfo = userInfoService.findById(userId);

        userInfoService.modifyUserInfoAccountState(userId, Constants.ACCOUNT_STATUS_DISABLE, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除账户
     *
     * @param userId 用户信息id
     * @return
     */
    @GetMapping("/delete")
    @Authority("users_delete")
    public Result delete(Integer userId, @User CurrentUser currentUser) {
//        UserInfo userInfo = userInfoService.findById(userId);

//        userInfoService.modifyUserInfoState(userId, Constants.INFO_STATE_DELETED, currentUser);
        userInfoService.deleteByUserId(userId);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 一键启用禁用账户
     */
    @GetMapping("/updateUserInfoAccountStatus")
    @Authority("users_updateUserInfoAccountStatus")
    public Result updateUserInfoAccountStatus(Integer accountStatus, @User CurrentUser currentUser) {
        if (accountStatus.equals(1)) {
            userInfoService.updateUserInfoAccountStatus("disable");
        } else if (accountStatus.equals(2)) {
            userInfoService.updateUserInfoAccountStatus("enable");
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 绑定仓库、角色、货主
     */
    @PostMapping("/bindWare")
    @Authority("users_bindWare")
    public Result bindWare(@RequestBody UserInfoCreate create, @User CurrentUser currentUser) {
        userInfoService.bindAuth(create, currentUser);
        return ResultGenerator.genSuccessResult();
    }
}
