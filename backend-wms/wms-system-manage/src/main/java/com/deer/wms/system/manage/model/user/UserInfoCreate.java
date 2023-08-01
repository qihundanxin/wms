package com.deer.wms.system.manage.model.user;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.ToString;

/**
 * 创建用户的信息类
 * <p>
 * Created by guo on 2018/8/11.
 */
@ToString
public class UserInfoCreate {
    /**
     * 姓名
     */
    private String userName;

    /**
     * 登录密码
     */
    @NotEmpty(message = "登录密码不能为空")
    private String password;

    /**
     * 确认密码
     */
    private String confirmPassword;

    /**
     * 性别：1=先生；2=女士；
     */
    private Integer gender;

    /**
     * 手机号码
     */
    @NotEmpty(message = "手机号码不能为空")
    private String mobile;

    /**
     * 电子邮箱
     */
    private String email;

    private Integer deptId;

    private Integer userId;

    private String account;

    private String jobNumber;

    /**
     * 跳转地址
     */
    private String url;

    private String urlName;

    private List<Integer> roleIds;

    private List<Integer> wareIds;

    private List<Integer> organizationIds;

    private Integer userType;

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public List<Integer> getWareIds() {
        return wareIds;
    }

    public void setWareIds(List<Integer> wareIds) {
        this.wareIds = wareIds;
    }

    public List<Integer> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<Integer> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }
}
