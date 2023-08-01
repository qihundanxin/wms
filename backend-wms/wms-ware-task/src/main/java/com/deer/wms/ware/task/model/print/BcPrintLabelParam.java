package com.deer.wms.ware.task.model.print;

import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 波次打印拣货标签
 * @author lixuehe
 * @date 2023/5/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BcPrintLabelParam {


    /**
     * 出库单
     */
    private String billNo;

    /**
     * 出库单
     */
    private String allotBillNo;

    /**
     * 名称
     */
    private String itemName;

    /**
     * sku
     */
    private String itemCode;

    /**
     * 司机名称
     */
    private String driverName;

    /**
     * 配送
     */
    private Integer orderIndex;

    /**
     * 仓库id
     */
    private Integer wareId;

    /**
     * 位置
     */
    private  String position;

    /**
     * sku 在detail 中的序号
     */
    private Integer detailIndex;

    /**
     * 订单总数
     */
    private Integer orderAllQuantity;
    /**
     * sku 总数
     */
    private Integer skuQuantity;

    /**
     * 订单序号
     */
    private Integer strIndex;
}
