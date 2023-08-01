package com.deer.wms.base.system.model.Cell;

import java.util.List;

import lombok.Data;

/**
 * CellInfo导入数据
 * 
 * @ClassName ExcelModel
 * @Author luolin
 * @Date 2023/6/7 14:47
 * @Version 1.0
 **/
@Data
public class CellInfoImportParam {

    /**
     * 仓库id
     */
    private Integer wareId;

    /**
     * 仓库名称
     */
    private String wareName;

    private List<CellExcelModel> list;
}
