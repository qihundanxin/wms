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
 * 还货上架
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "pick_task_return")
public class PickTaskReturn {

    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getCommitTime() {
        return DateUtils.dateToStrNullable(this.commitTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = DateUtils.strToDate(commitTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_task_return_id")
    private Integer pickTaskReturnId;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 货主id
     */
    @Column(name = "org_id")
    private Integer orgId;

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
     * 数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * 来源库存id
     */
    @Column(name = "from_inventory_id")
    private Integer fromInventoryId;

    /**
     * 来源货位
     */
    @Column(name = "from_cell_code")
    private String fromCellCode;

    /**
     * 推荐货位
     */
    @Column(name = "to_cell_code")
    private String toCellCode;

    /**
     * 上架货位
     */
    @Column(name = "commit_cell_code")
    private String commitCellCode;

    /**
     * 状态 0-待上架  1-已完成
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 1-配货异常还货  2-订单取消还货（未发运）  3-订单取消发货（已发运）
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 快递单号
     */
    @Column(name = "ship_bill_no")
    private String shipBillNo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 上架人 id
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
     * 波次masterId
     */
    @Column(name = "wave_master_id")
    private Integer waveMasterId;

    /**
     * 追踪号
     */
    @Column(name = "box_code")
    private String boxCode;
}
