package com.deer.wms.ware.task.model.Send;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 发运详情单
 *
 * @author lixuehe
 * @date 2023/02/22
 */
@Data
@Table(name = "send_detail")
public class SendDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "send_detail_id")
    private Integer sendDetailId;

    /**
     * 出库单号
     */
    @Column(name = "so_bill_no")
    private String soBillNo;


    /**
     * 状态 状态 0新建 1部分发运 2 全部发运
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 物料编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 物料名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 订货数
     */
    @Column(name = "order_quantity")
    private Double orderQuantity;


    /**
     * 发运数量
     */
    @Column(name = "send_quantity")
    private Double sendQuantity;


    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;

    /**
     * 发运时间
     */
    @Column(name = "send_time")
    private Date sendTime;

    /**
     * 发运人ID
     */
    @Column(name = "send_user_id")
    private Integer sendUserId;

    /**
     * 发运人
     */
    @Column(name = "send_user_name")
    private String sendUserName;

}
