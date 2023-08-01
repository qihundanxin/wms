package com.deer.wms.ware.task.model.pickTask;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 波次拣货分组
 * @author lixuehe
 * @date 2023/6/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pick_batch_group")
public class PickBatchGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 组编号
     */
    @Column(name = "pick_batch_group_no")
    private String pickBatchGroupNo;

    /**
     * 产品总数（实时）
     */
    @Column(name = "total_qty")
    private Double totalQty;

    /**
     * 单号组（生成时的快照）
     */
    @Column(name = "pq_order_lst")
    private String pqOrderLst;

    /**
     * 订单数量（生成时的快照，此处订单指PQ订单）
     */
    @Column(name = "total_order_count")
    private Integer totalOrderCount;

    /**
     * 司机
     */
    @Column(name = "driver_name")
    private String driverName;

    /**
     * 状态：-1-取消；0-正常；1-部分完成；2-完成
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;



}
