package com.deer.wms.ware.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 拣货任务异常信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "pick_task_ex")
public class PickTaskEx {

    // 兼容旧的String类型日期
    public String getCommitTime() {
        return DateUtils.dateToStrNullable(this.commitTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = DateUtils.strToDate(commitTime);
    }

    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_task_ex_id")
    private Integer pickTaskExId;

    /**
     * 货位编码
     */
    @Column(name = "cell_code")
    private String cellCode;

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
     * 商品图片
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 货主id
     */
    @Column(name = "org_id")
    private Integer orgId;

    /**
     * 拣货任务id
     */
    @Column(name = "pick_task_id")
    private Integer pickTaskId;

    /**
     * 总需求拣货数
     */
    @Column(name = "total_quantity")
    private Double totalQuantity;

    /**
     * 实际拣货数
     */
    @Column(name = "pick_quantity")
    private Double pickQuantity;

    /**
     * 异常数
     */
    @Column(name = "ex_quantity")
    private Double exQuantity;

    /**
     * 冻结库存数
     */
    @Column(name = "freeze_quantity")
    private Double freezeQuantity;

    /**
     * 上架数量
     */
    @Column(name = "commit_quantity")
    private Double commitQuantity;

    /**
     * 上架用户id
     */
    @Column(name = "commit_user_id")
    private Integer commitUserId;

    /**
     * 上架用户名称
     */
    @Column(name = "commit_user_name")
    private String commitUserName;

    /**
     * 上架时间
     */
    @Column(name = "commit_time")
    private Date commitTime;

    /**
     * 波次masterid
     */
    @Column(name = "wave_master_id")
    private Integer waveMasterId;

    /**
     * 追踪号
     */
    @Column(name = "box_code")
    private String boxCode;

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
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 0-初始化 1-已处理
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 库存id
     */
    @Column(name = "inventory_id")
    private Integer inventoryId;
}
