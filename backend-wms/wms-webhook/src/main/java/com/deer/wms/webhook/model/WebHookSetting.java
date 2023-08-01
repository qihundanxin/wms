package com.deer.wms.webhook.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.deer.wms.project.root.validation.group.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * WebHook设置
 * 
 * @author xuesinuo
 *
 */
@Data
@Table(name = "web_hook_setting")
public class WebHookSetting {

    public static @interface Enable {}
    // 在支持更多版本时，引入version=1
    @NotNull(groups = Enable.class)
    @Null(groups = Post.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "web_hook_setting_id")
    private Long webHookSettingId;

    /**
     * 描述
     */
    @Column(name = "name")
    private String name;

    /**
     * 能力枚举{@link WebHookAbility}
     */
    @NotNull(groups = Post.class)
    @Column(name = "ability")
    private WebHookAbility ability;

    /**
     * 接收方URL
     */
    @NotBlank(groups = Post.class)
    @Column(name = "url")
    private String url;

    /**
     * 是否启用
     */
    @NotNull(groups = Enable.class)
    @Column(name = "has_enable")
    private Boolean hasEnable;

    /**
     * 创建时间
     */
    @Null(groups = Post.class)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人ID
     */
    @Null(groups = Post.class)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 秘钥
     */
    @JsonIgnore
    @Null(groups = Post.class)
    @Column(name = "secret_key")
    private String secretKey;
}
