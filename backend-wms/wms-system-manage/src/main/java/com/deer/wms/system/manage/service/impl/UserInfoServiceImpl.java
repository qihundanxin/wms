package com.deer.wms.system.manage.service.impl;

import java.util.List;

import com.deer.wms.intercept.common.data.CommonDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.Constants;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.MD5Utils;
import com.deer.wms.project.root.util.RandomUtil;
import com.deer.wms.system.manage.dao.UserInfoMapper;
import com.deer.wms.system.manage.model.user.UserData;
import com.deer.wms.system.manage.model.user.UserDetail;
import com.deer.wms.system.manage.model.user.UserInfo;
import com.deer.wms.system.manage.model.user.UserInfoCreate;
import com.deer.wms.system.manage.model.user.UserInfoCriteria;
import com.deer.wms.system.manage.model.user.UserInfoDetailVO;
import com.deer.wms.system.manage.model.user.UserInfoDto;
import com.deer.wms.system.manage.model.user.UserInfoModify;
import com.deer.wms.system.manage.model.user.UserListVO;
import com.deer.wms.system.manage.model.user.UserPassword;
import com.deer.wms.system.manage.model.user.UserPasswordRetrieval;
import com.deer.wms.system.manage.service.UserInfoService;
import com.deer.wms.system.manage.service.UserOrganizationService;
import com.deer.wms.system.manage.service.UserRoleService;
import com.deer.wms.system.manage.service.WareUserService;

/**
 * Created by guo on 2018/8/11.
 */
@Service
@Transactional
public class UserInfoServiceImpl extends AbstractService<UserInfo, Integer> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserOrganizationService userOrganizationService;

    @Autowired
    private WareUserService wareUserService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CommonDataService commonDataService;

    @Override
    public List<UserListVO> findUserFormList(UserInfoCriteria criteria) {
        List<UserListVO> list = userInfoMapper.selectUserFormList(criteria);
        return list;
    }

    @Override
    public UserInfo findUserInfoByAccount(String account) {

        UserInfo userInfo = userInfoMapper.selectUserInfo(account);
        return userInfo;
    }

    @Override
    public UserDetail findUserDetailByAccount(String account) {
        if (StringUtils.isEmpty(account)) {
            return null;
        }
        return userInfoMapper.selectUserDetailByAccount(account);
    }

    @Override
    public UserInfoDetailVO findUserInfoDetailByUserId(Integer userId) {
        if (null == userId) {
            return null;
        }
        return userInfoMapper.selectUserInfoDetailVOByUserId(userId);
    }

    @Override
    public UserInfo validate(String account, String password) {
        // 验证账户是否存在

        UserInfo userInfo = userInfoMapper.selectUserInfo(account);

        if (null == userInfo || !Constants.INFO_STATE_NORMAL.equals(userInfo.getState())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此账号已禁用");
        }

        // 验证密码是否正确
        String loginPassword = MD5Utils.encryptPassword(userInfo.getAccount(), password, userInfo.getSalt());
        if (!loginPassword.equals(userInfo.getPassword())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "登录账号或密码错误");
        }

        // 验证账号状态是否正常
        if (Constants.ACCOUNT_STATUS_DISABLE.equals(userInfo.getAccountStatus())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "登录账号被锁定");
        }

        // 验证账号信息状态是否正常
        if (!Constants.INFO_STATE_NORMAL.equals(userInfo.getState())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "登录账号状态异常，请与系统管理员联系");
        }

        return userInfo;
    }

    @Override
    public List<UserInfoDto> findUserByWareId(Integer wareId) {
        return userInfoMapper.findUserByWareId(wareId);
    }

    @Override
    public void modifyPassword(UserPassword userPassword, CurrentUser currentUser) {
        UserInfo userInfo = findUserInfoByAccount(currentUser.getAccount());
        String oldPassword = MD5Utils.encryptPassword(userInfo.getAccount(), userPassword.getOldPassword(),
                userInfo.getSalt());
        if (!oldPassword.equals(userInfo.getPassword())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "旧密码不正确");
        }
//        if (!userInfo.getPassword().equals(userPassword.getOldPassword())) {
//            throw new ServiceException(CommonCode.SERVICE_ERROR, "旧密码不正确");
//        }

        String salt = RandomUtil.generateString(10);
        userInfo = new UserInfo();
        userInfo.setUserId(currentUser.getUserId());
        userInfo.setPassword(MD5Utils.encryptPassword(currentUser.getAccount(), userPassword.getPassword(), salt));
        userInfo.setSalt(salt);
        userInfo.setModifyUserId(currentUser.getUserId());
        userInfo.setModifyTime(DateUtils.getNowDateTimeString());

        super.update(userInfo);
    }

    @Override
    public void modifyUserData(UserData userData, CurrentUser currentUser) {
        // 修改个人信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(currentUser.getUserId());
        userInfo.setEmail(userData.getEmail());

        if(userData.getNewPassword()!=null && !userData.getNewPassword().trim().equals("")){
            userInfo.setPassword(MD5Utils.encryptPassword(currentUser.getAccount(), userData.getNewPassword(), userInfo.getSalt()));

        }

        // 如果头像图片文件不为空，说明要修改头像图片
        if (null != userData.getIconUrlFile()) {
            // xuesinuo 2022年11月4日 不支持头像修改
        }
        // super.update(userInfo);
        // 邮箱和头像地址只有一个不为空，责进行修改
        if (!(StringUtils.isEmpty(userInfo.getIconUrl()) && StringUtils.isEmpty(userInfo.getEmail()))) {
            userInfoMapper.updateUserInfoById(userInfo);
        }
    }

    @Override
    public void modifyUserInfoAccountState(Integer userId, String sate, CurrentUser currentUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setAccountStatus(sate);
        userInfo.setModifyUserId(currentUser.getUserId());
        userInfo.setModifyTime(DateUtils.getNowDateTimeString());
        super.update(userInfo);
    }

    @Override
    public void modifyUserInfoState(Integer userId, String sate, CurrentUser currentUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setState(sate);
        userInfo.setModifyUserId(currentUser.getUserId());
        userInfo.setModifyTime(DateUtils.getNowDateTimeString());
        super.update(userInfo);
    }

    @Override
    public void retrievalPassword(UserPasswordRetrieval retrieval) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(retrieval.getMobile());
        userInfo.setState("normal");
        userInfo.setAccountStatus("enable");
//		List<UserInfo> users = userInfoMapper.select(userInfo);
        UserInfo user = userInfoMapper.selectUserInfo(retrieval.getMobile());
        String salt = RandomUtil.generateString(10);
        // UserInfo userInfo = findUserInfoByAccount(retrieval.getMobile());
        if (null == user) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "手机号码或邮箱未注册");
        }
        if (null != user && Constants.INFO_STATE_DELETED.equals(user.getState())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "账号已被删除");
        }
        if (null != user && Constants.ACCOUNT_STATUS_DISABLE.equals(user.getAccountStatus())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "账号已被停用");
        }
        if (null != user && !Constants.ACCOUNT_STATUS_DISABLE.equals(user.getAccountStatus())
                && !Constants.INFO_STATE_DELETED.equals(user.getState())) {
            BeanUtils.copyProperties(user, userInfo);
        }
        userInfo = user;
        userInfo.setPassword(MD5Utils.encryptPassword(userInfo.getAccount(), retrieval.getPassword(), salt));
        userInfo.setPassword(retrieval.getPassword());
        userInfo.setSalt(salt);
        userInfo.setModifyUserId(userInfo.getUserId());
        userInfo.setModifyTime(DateUtils.getNowDateTimeString());
        super.update(userInfo);
    }

    @Override
    public void createUserInfo(UserInfoCreate create, CurrentUser currentUser) {
        // 保存用户信息和账户信息
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(create, userInfo);
        String salt = RandomUtil.generateString(10);
//		userInfo.setAccount(create.getMobile());
        userInfo.setPassword(MD5Utils.encryptPassword(userInfo.getAccount(), create.getPassword(), salt));
//        userInfo.setPassword(create.getPassword());
        userInfo.setSalt(salt);
        userInfo.setCreateUserId(currentUser.getUserId());
        userInfo.setCreateTime(DateUtils.getNowDateTimeString());
        userInfo.setModifyTime(DateUtils.getNowDateTimeString());
        userInfo.setIsAdmin(0);

        super.save(userInfo);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        userOrganizationService.deleteByUserId(userId);
        userRoleService.deleteByUserId(userId);
        wareUserService.deleteByUserId(userId);
        userInfoMapper.deleteByUserId(userId);
    }

    @Override
    public void modifyUserInfo(UserInfoModify modify, CurrentUser currentUser) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(modify, userInfo);
        userInfo.setModifyUserId(currentUser.getUserId());
        userInfo.setModifyTime(DateUtils.getNowDateTimeString());
        String salt = RandomUtil.generateString(10);
        if(modify.getNewPassword()!=null && !modify.getNewPassword().trim().equals("")){
            userInfo.setPassword(MD5Utils.encryptPassword(userInfo.getAccount(), modify.getNewPassword(), salt));
            userInfo.setSalt(salt);

        }
        super.update(userInfo);
        commonDataService.removeCurrentUserDataFromRedisByUserId(String.valueOf(userInfo.getUserId()));
    }

    @Override
    public List<UserInfo> selectByInfos(UserInfo userInfo) {
        return userInfoMapper.select(userInfo);
    }

    @Override
    public List<UserInfo> findByAccount(String account) {
        return userInfoMapper.findByAccount(account);
    }

    @Override
    public List<UserInfoDto> findByDeptId(Integer deptId) {
        return userInfoMapper.findByDeptId(deptId);
    }

    @Override
    public void updateUserInfoAccountStatus(String accountStatus) {
        userInfoMapper.updateUserInfoAccountStatus(accountStatus);
    }

    @Override
    public void bindRole(UserInfoCreate create) {
        // 添加用户角色信息
        for (Integer roleId : create.getRoleIds()) {
            userRoleService.addUserRole(create.getUserId(), roleId);
        }
    }

    @Override
    public void bindWare(UserInfoCreate create, Integer createUserId) {
        // 添加用户仓库信息
        for (Integer wareId : create.getWareIds()) {
            wareUserService.addWareUser(create.getUserId(), wareId, createUserId);
        }

    }

    @Override
    public void bindOrganization(UserInfoCreate create) {
        // 添加用户货主信息
        for (Integer organizationId : create.getOrganizationIds()) {
            userOrganizationService.addUserOrganization(create.getUserId(), organizationId);
        }
    }

    @Override
    public List<UserInfoDto> findList(UserInfoCriteria criteria) {
        return userInfoMapper.findList(criteria);
    }

    @Override
    public List<UserInfoDto> findAny(UserInfoCriteria criteria) {
        return userInfoMapper.findAny(criteria);
    }

    @Override
    public void bindAuth(UserInfoCreate create, CurrentUser currentUser) {
        wareUserService.deleteByUserId(create.getUserId());
        userRoleService.deleteByUserId(create.getUserId());
        userOrganizationService.deleteByUserId(create.getUserId());
        if (create.getWareIds() != null) {
            Integer createUserId = currentUser.getUserId();
            userInfoService.bindWare(create, createUserId);
        }
        if (create.getRoleIds() != null) {
            userInfoService.bindRole(create);
        }
        if (create.getOrganizationIds() != null) {
            userInfoService.bindOrganization(create);
        }
        commonDataService.removeAuthDataFromRedis(String.valueOf(create.getUserId()));
    }

}
