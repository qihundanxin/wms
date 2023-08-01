package com.deer.wms.system.manage.model.user;

import java.util.List;

import lombok.ToString;

/**
 * 账户管理信息列表视图类
 * <p>
 * Created by guo on 2018/8/11.
 */
@ToString
public class UserListVO {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 性别：1=先生；2=女士；
     */
    private Integer gender;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 是否管理员
     */
    private Integer isAdmin;

    /**
     * 状态
     */
    private String accountStatus;

    /**
     * 注册时间
     */
    private String registerTime;

    private String jobNumber;

    private List<String> roleNames;
    private List<String> wareNames;
    private List<String> organizationNames;

    private String account;

    private String password;

    private Integer deptId;

    private String deptName;

    private String state;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public List<String> getWareNames() {
        return wareNames;
    }

    public void setWareNames(List<String> wareNames) {
        this.wareNames = wareNames;
    }

    public List<String> getOrganizationNames() {
        return organizationNames;
    }

    public void setOrganizationNames(List<String> organizationNames) {
        this.organizationNames = organizationNames;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
