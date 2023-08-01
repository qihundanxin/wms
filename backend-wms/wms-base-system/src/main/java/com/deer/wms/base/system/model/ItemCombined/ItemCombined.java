package com.deer.wms.base.system.model.ItemCombined;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 组合产品
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "item_combined")
public class ItemCombined {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

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
     * desc1
     */
    @Column(name = "desc1")
    private String desc1;

    /**
     * desc2
     */
    @Column(name = "desc2")
    private String desc2;

    /**
     * 费率
     */
    @Column(name = "price")
    private Double price;

    /**
     * 工序描述
     */
    @Column(name = "procedure_desc")
    private String procedureDesc;

    /**
     * 激活状态 0未激活  1激活
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 包装材料标志 0关闭 1开启
     */
    @Column(name = "is_packing")
    private Integer isPacking;

    /**
     * 版本号
     */
    @Column(name = "ver")
    private String ver;

    /**
     * 货主id
     */
    @Column(name = "organization_id")
    private Integer organizationId;
}
