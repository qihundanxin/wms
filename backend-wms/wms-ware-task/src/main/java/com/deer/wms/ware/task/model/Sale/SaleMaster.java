package com.deer.wms.ware.task.model.Sale;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.deer.wms.project.root.util.DateUtils;

import lombok.Data;

/**
 * 销售单
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "sale_master")
public class SaleMaster {
    // 兼容旧的String类型日期
    public String getCreateTime() {
        return DateUtils.dateToStrNullable(this.createTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setCreateTime(String createTime) {
        this.createTime = DateUtils.strToDate(createTime);
    }

    public String getExpectTime() {
        return DateUtils.dateToStrNullable(this.expectTime, DateUtils.DEFAULT_DATETIME_FORMAT);
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = DateUtils.strToDate(expectTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_master_id")
    private Integer saleMasterId;

    /**
     * 订单号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 销售类型
     */
    @Column(name = "sale_type")
    private Integer saleType;

    /**
     * 状态   -2-保存为草稿 2-等待审核  1-审核通过 0-审核中 -1 -审核退回 
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 销售单状态 0-初始化 2-部分发红  3-全部发货 
     */
    @Column(name = "sale_state")
    private Integer saleState;

    /**
     * 仓库ID
     */
    @Column(name = "ware_id")
    private Integer wareId;

    /**
     * 仓库名称
     */
    @Column(name = "ware_name")
    private String wareName;

    /**
     * 货主ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 货主名称
     */
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 客户ID
     */
    @Column(name = "customer_id")
    private Integer customerId;

    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    private String customerName;

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
     * 预期发货时间
     */
    @Column(name = "expect_time")
    private Date expectTime;

    /**
     * 自定义1
     */
    @Column(name = "sale_udf_hs1")
    private String saleUdfHs1;

    /**
     * 自定义2
     */
    @Column(name = "sale_udf_hs2")
    private String saleUdfHs2;

    /**
     * 自定义3
     */
    @Column(name = "sale_udf_hs3")
    private String saleUdfHs3;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 0-提交  -2保存为草稿  
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 销售单来源  
     */
    @Column(name = "bill_source")
    private Integer billSource;
}
