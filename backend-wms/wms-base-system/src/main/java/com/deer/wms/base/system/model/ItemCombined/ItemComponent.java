package com.deer.wms.base.system.model.ItemCombined;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 产品组件
 * 
 * @author xuesinuo
 */
@Data
@Table(name = "item_component")
public class ItemComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 组合产品编码
     */
    @Column(name = "combined_item_code")
    private String combinedItemCode;

    /**
     * 组件物资编码
     */
    @Column(name = "component_item_code")
    private String componentItemCode;

    /**
     * 组件物资名称
     */
    @Column(name = "component_item_name")
    private String componentItemName;

    /**
     * 数量
     */
    @Column(name = "quantity")
    private Double quantity;

    /**
     * workIndex
     */
    @Column(name = "work_index")
    private Integer workIndex;

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
}
