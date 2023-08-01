package com.deer.wms.system.manage.model.user;

import java.util.List;

import com.deer.wms.system.manage.model.permission.Menu;

import lombok.ToString;

/**
 * 用户登录成功以后，返回的用户信息详情。
 * <p>
 * Created by guo on 2017/10/2.
 */
@ToString
public class UserDetail {
    /**
     * 访问系统以及相关api接口的token
     */
    private String token;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 性别：1=先生；2=女士；
     */
    private Integer gender;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String iconUrl;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 账户角色id
     */
    private List<Integer> roleIds;

    /**
     * 账户角色名称
     */
    private List<String> roleNames;

    /**
     * 账户部门id
     */
    private List<Integer> deptIds;

    /**
     * 账户部门名称
     */
    private List<String> deptNames;

    /**
     * 账户货主id
     */
    private List<Integer> organizationIds;

    /**
     * 账户货主名称
     */
    private List<String> organizationNames;

    /**
     * 账户仓库id
     */
    private List<Integer> wareIds;

    /**
     * 账户仓库名称
     */
    private List<String> wareNames;

    /**
     * 权限范围内的页面路径信息列表
     */
    private List<String> urls;

    /**
     * 权限范围内的操作代码数组
     */
    private List<String> permission;

    /**
     * 权限范围内的菜单信息
     */
    private List<Menu> menus;

    /**
     * 权限范围内的菜单信息
     */
    private List<Menu> menus1;
    /**
     * 权限范围内的菜单信息
     */
    private List<Menu> menus2;
    /**
     * 权限范围内的菜单信息
     */
    private List<Menu> menus3;
    private List<Menu> menus4;

    private Integer deptId;

    private Integer isAdmin;

    private String jobNumber;

    /**
     * 登录账号
     */
    private String account;

    public List<Menu> getMenus4() {
        return menus4;
    }

    public void setMenus4(List<Menu> menus4) {
        this.menus4 = menus4;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    private String deptName;

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public List<Menu> getMenus1() {
        return menus1;
    }

    public void setMenus1(List<Menu> menus1) {
        this.menus1 = menus1;
    }

    public List<Menu> getMenus2() {
        return menus2;
    }

    public void setMenus2(List<Menu> menus2) {
        this.menus2 = menus2;
    }

    public List<Menu> getMenus3() {
        return menus3;
    }

    public void setMenus3(List<Menu> menus3) {
        this.menus3 = menus3;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public List<Integer> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<Integer> deptIds) {
        this.deptIds = deptIds;
    }

    public List<String> getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(List<String> deptNames) {
        this.deptNames = deptNames;
    }

    public List<Integer> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<Integer> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public List<String> getOrganizationNames() {
        return organizationNames;
    }

    public void setOrganizationNames(List<String> organizationNames) {
        this.organizationNames = organizationNames;
    }

    public List<Integer> getWareIds() {
        return wareIds;
    }

    public void setWareIds(List<Integer> wareIds) {
        this.wareIds = wareIds;
    }

    public List<String> getWareNames() {
        return wareNames;
    }

    public void setWareNames(List<String> wareNames) {
        this.wareNames = wareNames;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getPermission() {
        return permission;
    }

    public void setPermission(List<String> permission) {
        this.permission = permission;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
