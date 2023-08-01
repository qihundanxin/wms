package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.user.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper extends Mapper<UserInfo> {

    /**
     * 查询账户信息列表
     *
     * @param criteria 查询条件
     * @return
     */
    List<UserListVO> selectUserFormList(UserInfoCriteria criteria);

    /**
     * 查询指定登录账号的用户详细信息
     *
     * @param account 登录帐号
     * @return 登录账号对应的用户详细信息，如果没有找到返回null
     */
    UserDetail selectUserDetailByAccount(@Param("account") String account);

    /**
     * 查询指定用户的详细信息
     *
     * @param userId 用户信息id
     * @return 用户的详细信息
     */
    UserInfoDetailVO selectUserInfoDetailVOByUserId(@Param("userId") Integer userId);

    /**
     * 修改用户信息
     *
     * @param userInfo
     */
    void updateUserInfoById(UserInfo userInfo);

    /**
     * 根据用户名、手机号、邮箱查找用户信息
     *
     * @param
     */
    UserInfo selectUserInfo(String account);

    List<UserInfo> findByAccount(String account);

    List<UserInfoDto> findByDeptId(Integer deptId);

    void updateUserInfoAccountStatus(String accountStatus);

    void deleteByUserId(Integer id);

    List<UserInfoDto> findList(UserInfoCriteria criteria);

    List<UserInfoDto> findAny(UserInfoCriteria criteria);

    List<UserInfoDto> findUserByWareId(Integer wareId);
}