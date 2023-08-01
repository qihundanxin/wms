package com.deer.wms.ware.task.model.pickTask;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.deer.wms.ware.task.model.SO.SoMaster;

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
public class PickBatch {

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
     * 波次编号
     */
    @Column(name = "pick_batch_no")
    private String pickBatchNo;


    /**
     * 状态：-1-取消；0-正常；1-部分完成；2-完成
     */
    @Column(name = "status")
    private Integer status;
    SoMaster a;

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

    /**
     * 拣货人
     */
    @Column(name = "pick_user_id")
    private Integer pickUserId;

    /** 任务时间（分配或领取的时间） */
    @Column(name = "task_time")
    private Date taskTime;

    /** 最后拣货时间 */
    @Column(name = "pick_time")
    private Date pickTime;

    /** 是否为批发单 */
    @Column(name = "wholesale")
    private Boolean wholesale;

    /** 拣货容器（笼车还是板车） */
    @Column(name = "container_type")
    private String containerType;
}
