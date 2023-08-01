package com.deer.wms.base.system.model.Message;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 系统通知表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    /**
     * 通知信息
     */
    @Column(name = "message_name")
    private String messageName;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
}
