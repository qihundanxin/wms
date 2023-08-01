package com.deer.wms.webhook.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * WebHook设置
 * 
 * @author xuesinuo
 *
 */
@Data
@Table(name = "web_hook_log")
public class WebHookLog {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "web_hook_log_id")
    private Long webHookLogId;

    /**
     * 是否成功
     */
    @Column(name = "has_success")
    private Boolean hasSuccess;

    /**
     * 发送的信息
     */
    @Column(name = "send_message")
    private String sendMessage;

    /**
     * 收到的返回结果
     */
    @Column(name = "result_message")
    private String resultMessage;

    /**
     * 发送时间
     */
    @Column(name = "send_datetime")
    private LocalDateTime sendDatetime;

    /**
     * webhook设置ID
     */
    @Column(name = "web_hook_setting_id")
    private Long webHookSettingId;

    /**
     * 能力枚举{@link WebHookAbility}
     */
    @Column(name = "ability")
    private WebHookAbility ability;

    /**
     * 接收方URL
     */
    @Column(name = "url")
    private String url;

    /**
     * 数据关键词（如单号、ID，用于方便查询）
     */
    @Column(name = "key_word")
    private String keyWord;
}
