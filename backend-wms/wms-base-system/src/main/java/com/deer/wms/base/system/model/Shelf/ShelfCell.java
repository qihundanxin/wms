package com.deer.wms.base.system.model.Shelf;

import com.deer.wms.base.system.model.Cell.CellInfo;

import lombok.Data;

/**
 * 包含ShelfInfo CellInfo信息，详见各自实体Class
 * 
 * @see {@link ShelfInfo} {@link CellInfo}
 * 
 * @author xuesinuo
 */
@Data
public class ShelfCell {

    //
    // shelf
    //

    private Integer shelfInfoId;

    private String shelfName;

    private String shelfCode;

    private String shelfDescribe;

    private Integer type;

    private String memo;

    private Integer state;

    private Integer areaId;

    private Integer xCoordinate;

    private Integer yCoordinate;

    private Integer shelfNo;

    private Integer shelfGroup;

    private Integer shelfRow;

    private Integer shelfColumn;

    private Integer isSys;

    //
    // cell
    //

    private Integer cellId;

    private String cellCode;

    private String cellName;

    private Integer wareId;

    private String wareName;

    private String groundOrder;

    private String pickOrder;

    private Integer cellType;

    private Integer cellUseType;

    private String cellAttribute;

    private String cellHandle;

    private Double length;

    private Double wide;

    private Double high;

    private Integer cellAbcLevel;

    private Double volumeLimit;

    private Double weightLimit;

    private Double boxLimit;

    private Double skuLimit;

    private Double quantityLimit;

    private Double palletLimit;

    private Integer zCoordinate;

    private Integer cellPickType;

    private Integer status;

    private Integer sRow;

    private Integer sColumn;
}
