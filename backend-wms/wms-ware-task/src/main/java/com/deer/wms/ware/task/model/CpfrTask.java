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
 * 补货任务
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "cpfr_task")
public class CpfrTask {

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
    @Column(name = "cpfr_task_id")
    private Integer cpfrTaskId;

    /**
     * 仓库Id
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
     * 来源货位编码
     */
    @Column(name = "from_cell_code")
    private String fromCellCode;

    /**
     * to 货位编码
     */
    @Column(name = "to_cell_code")
    private String toCellCode;

    /**
     * 上架 货位编码
     */
    @Column(name = "commit_cell_code")
    private String commitCellCode;

    /**
     * 上架人 id
     */
    @Column(name = "commit_user_id")
    private Integer commitUserId;

    /**
     * 上架人名称
     */
    @Column(name = "commit_user_name")
    private String commitUserName;

    /**
     * 上架时间
     */
    @Column(name = "commit_time")
    private Date commitTime;

    /**
     * 0：待执行 1：执行完  -1：取消
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 1：配置驱动  2：订单驱动  3：主动补货
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 来源库存id
     */
    @Column(name = "from_inventory_id")
    private Integer fromInventoryId;

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
}

