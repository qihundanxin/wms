package com.deer.wms.ware.task.model.pickTask;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 拣货任务信息表
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "pick_task")
public class PickTask {

    // 兼容旧的String类型日期
    public String getPickTime() {
        return DateUtils.dateToStrNullable(this.pickTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setPickTime(String pickTime) {
        this.pickTime = DateUtils.strToDate(pickTime);
    }

    public String getCheckTime() {
        return DateUtils.dateToStrNullable(this.checkTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = DateUtils.strToDate(checkTime);
    }

    public String getDeliveryTime() {
        return DateUtils.dateToStrNullable(this.deliveryTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = DateUtils.strToDate(deliveryTime);
    }

    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_task_id")
    private Integer pickTaskId;

    /**
     * 出库单详情id
     */
    @Column(name = "so_detail_id")
    private Integer soDetailId;

    /**
     * 出库单id
     */
    @Column(name = "so_master_id")
    private Integer soMasterId;

    /**
     * 波次masterId
     */
    @Column(name = "wave_master_id")
    private Integer waveMasterId;

    /**
     * 仓库id
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 货主id
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 货区编码
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 货区名
     */
    @Column(name = "area_name")
    private String areaName;

    /**
     * 货位组编码
     */
    @Column(name = "shelf_code")
    private String shelfCode;

    /**
     * 货位组名称
     */
    @Column(name = "shelf_name")
    private String shelfName;

    /**
     * 货位编码
     */
    @Column(name = "cell_code")
    private String cellCode;

    /**
     * 货位名称
     */
    @Column(name = "cell_name")
    private String cellName;

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
     * 批次ID
     */
    @Column(name = "batch_id")
    private Integer batchId;

    /**
     * 批次名称
     */
    @Column(name = "batch_name")
    private String batchName;

    /**
     * 追踪号
     */
    @Column(name = "box_code")
    private String boxCode;

    /**
     * LPN号码
     */
    @Column(name = "lpn")
    private String lpn;

    /**
     * 包装ID
     */
    @Column(name = "pack_detail_id")
    private Integer packDetailId;

    /**
     * 包装描述
     */
    @Column(name = "pack_describe")
    private String packDescribe;

    /**
     * 需求数
     */
    @Column(name = "pick_quantity")
    private Double pickQuantity;

    /**
     * 实际分拣数
     */
    @Column(name = "review_quantity")
    private Double reviewQuantity;

    /**
     *  -1订单取消 0-已分配  1-部分分拣  2-完成拣货  3-取消  4-被合并  5-已经发运
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 被合并的拣货任务ID
     */
    @Column(name = "parent_task_id")
    private Integer parentTaskId;

    /**
     * 库存id
     */
    @Column(name = "inventory_id")
    private Integer inventoryId;

    /**
     * 1-手动创建  2-自动创建
     */
    @Column(name = "source")
    private Integer source;

    /**
     * 目标货位
     */
    @Column(name = "to_cell_code")
    private String toCellCode;

    /**
     * 追踪号
     */
    @Column(name = "tracking_num")
    private String trackingNum;

    /**
     * 拣货时间
     */
    @Column(name = "pick_time")
    private Date pickTime;

    /**
     * 拣货人id
     */
    @Column(name = "pick_user_id")
    private Integer pickUserId;

    /**
     * 拣货人
     */
    @Column(name = "pick_user_name")
    private String pickUserName;

    /**
     * 审核时间
     */
    @Column(name = "check_time")
    private Date checkTime;

    /**
     * 审核人id
     */
    @Column(name = "check_user_id")
    private Integer checkUserId;

    /**
     * 审核人
     */
    @Column(name = "check_user_name")
    private String checkUserName;

    /**
     * 发货时间
     */
    @Column(name = "delivery_time")
    private Date deliveryTime;

    /**
     * 发货人id
     */
    @Column(name = "delivery_user_id")
    private Integer deliveryUserId;

    /**
     * 发货人
     */
    @Column(name = "delivery_user_name")
    private String deliveryUserName;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 播种数量
     */
    @Column(name = "seeded_quantity")
    private Double seededQuantity;

    /**
     * 是否为为合并任务
     */
    @Column(name = "is_parent")
    private Integer isParent;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;

    /**
     * 拣货的库存id
     * 
     * TODO 缕清From To 目前逻辑里面：
     * 
     * inventoryId在分配拣货任务时存放了From，拣货任务完成后存放了To（相当于Qty位置），但是支持部分完成后，Qty不在一起，无法使用一个字段表达。
     * 
     * fromInventoryId在分配拣货任务是为null（此字段一直为null，支持部分拣货才刚刚重新启用）。在拣货部分完成时，将From存入此处，以便下次提交拣货时使用。
     * 
     * 我终于明白容器号干啥吃的了。。。 291333 与 容器号 如何取舍？
     */
    @Column(name = "from_inventory_id")
    private Integer fromInventoryId;
}
//
//
//    public PickTask(Integer soDetailId, Integer soMasterId, Integer wareId, String wareName, Integer organizationId, String organizationName, String areaCode, String areaName, String shelfCode, String shelfName, String cellCode, String cellName, String itemCode, String itemName, Integer batchId, String batchName, String boxCode, String lpn, Integer packDetailId, String packDescribe, Double pickQuantity, Integer state, Integer inventoryId, Integer source, String imgUrl) {
//        this.soDetailId = soDetailId;
//        this.soMasterId = soMasterId;
//        this.wareId = wareId;
//        this.wareName = wareName;
//        this.organizationId = organizationId;
//        this.organizationName = organizationName;
//        this.areaCode = areaCode;
//        this.areaName = areaName;
//        this.shelfCode = shelfCode;
//        this.shelfName = shelfName;
//        this.cellCode = cellCode;
//        this.cellName = cellName;
//        this.itemCode = itemCode;
//        this.itemName = itemName;
//        this.batchId = batchId;
//        this.batchName = batchName;
//        this.boxCode = boxCode;
//        this.lpn = lpn;
//        this.packDetailId = packDetailId;
//        this.packDescribe = packDescribe;
//        this.pickQuantity = pickQuantity;
//        this.state = state;
//        this.inventoryId = inventoryId;
//        this.source = source;
//        this.imgUrl = imgUrl;
//
//    }
//
//    public PickTask(Integer soDetailId, Integer soMasterId, Integer wareId, String wareName, Integer organizationId, String organizationName, String cellCode, String itemCode, String itemName, Integer batchId, String batchName, String boxCode, String lpn, Integer packDetailId, String packDescribe, Double pickQuantity, Integer state, Integer inventoryId, Integer source, String imgUrl) {
//        this.soDetailId = soDetailId;
//        this.soMasterId = soMasterId;
//        this.wareId = wareId;
//        this.wareName = wareName;
//        this.organizationId = organizationId;
//        this.organizationName = organizationName;
//
//        this.cellCode = cellCode;
//        this.itemCode = itemCode;
//        this.itemName = itemName;
//        this.batchId = batchId;
//        this.batchName = batchName;
//        this.boxCode = boxCode;
//        this.lpn = lpn;
//        this.packDetailId = packDetailId;
//        this.packDescribe = packDescribe;
//        this.pickQuantity = pickQuantity;
//        this.state = state;
//        this.inventoryId = inventoryId;
//        this.source = source;
//        this.imgUrl = imgUrl;
//    }
//}