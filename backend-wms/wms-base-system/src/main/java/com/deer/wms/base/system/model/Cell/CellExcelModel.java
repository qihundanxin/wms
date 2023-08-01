package com.deer.wms.base.system.model.Cell;

import com.alibaba.excel.annotation.ExcelProperty;
import com.deer.wms.base.system.converter.CellUseTypeConverter;
import com.deer.wms.base.system.converter.ShelfTypeConverter;

import com.deer.wms.base.system.annotation.ExcelValid;
import lombok.Data;

/**
 * Excel导入货位
 * 
 * @ClassName CellExcelModel
 * @Author luolin
 * @Date 2023/6/6 17:29
 * @Version 1.0
 **/
@Data
public class CellExcelModel {


    /**
     * 货区名称
     */
    @ExcelProperty("货区名称")
    @ExcelValid(message = "货区名称列不能有空数据！")
    private String areaName;

    /**
     * 货区编码
     */
    @ExcelProperty("货区编码")
    @ExcelValid(message = "货区编码列不能有空数据！")
    private String areaCode;

    /**
     * 货位组名称
     */
    @ExcelProperty("货位组名称")
    @ExcelValid(message = "货位组名称列不能有空数据！")
    private String shelfName;

    /**
     * 货位组编码
     */
    @ExcelProperty("货位组编码")
    @ExcelValid(message = "货位组编码列不能有空数据！")
    private String shelfCode;

    /**
     * 货位组类型 货架 地堆
     */
    @ExcelProperty(value = "货位组类型", converter = ShelfTypeConverter.class)
    @ExcelValid(message = "货位组类型列数据有误，请核对数据！")
    private Integer type;

    /**
     * 货位编码
     */
    @ExcelProperty("货位编码")
    @ExcelValid(message = "货位编码列不能有空数据！")
    private String cellCode;

    /**
     * 货位名称
     */
    @ExcelProperty("货位名称")
    @ExcelValid(message = "货位名称列不能有空数据！")
    private String cellName;

    /**
     * 货位使用类型  -1 垃圾货位 0-验收位 1-拣货位 2-仓储位 3-集货位 4-上架车
     */
    @ExcelProperty(value = "货位使用类型", converter = CellUseTypeConverter.class)
    @ExcelValid(message = "货位使用类型列数据有误，请核对数据！")
    private Integer cellUseType;

    /**
     * sku种类限制
     */
    @ExcelProperty("SKU种类限制")
    @ExcelValid(message = "SKU种类限制列不能有空数据！")
    private Double skuLimit;

    /**
     * x坐标
     */
    @ExcelProperty("X坐标")
    @ExcelValid(message = "X坐标列不能有空数据！")
    private Integer xCoordinate;

    /**
     * y坐标
     */
    @ExcelProperty("Y坐标")
    @ExcelValid(message = "Y坐标列不能有空数据！")
    private Integer yCoordinate;
}
