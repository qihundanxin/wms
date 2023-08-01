package com.deer.wms.ware.task.model.print;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * 打印波次贱货表格参数
 * @author lixuehe
 * @date 2023/5/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BcPrintTableParam {

    /**
     * 波次序号
     */
    private String bcNumber;

    /**
     * 仓库id
     */
    private Integer wareId;

    /**
     * 波次打印label 详情数据列表
     */
    private List<BcPrintTable> bcPrintTables;

    /**
     * 数量
     */
    private int num;

    /**
     * 序号
     */
    private int n;
}
