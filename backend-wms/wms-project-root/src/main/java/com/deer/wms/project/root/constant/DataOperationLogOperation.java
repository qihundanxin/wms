package com.deer.wms.project.root.constant;

/**
 * 数据操作日志的操作分类，{@link DataOperationLogModule}
 * 
 * @author xuesinuo
 *
 */
public interface DataOperationLogOperation {
    // SO
    /** 新增SO单 */
    String SO_ADD = DataOperationLogModule.SO + ":" + "add";
    /** 修改SO单 */
    String SO_SET = DataOperationLogModule.SO + ":" + "set";

    // PICK_TASK
    /** 新增拣货任务分配 */
    String PICK_TASK_ADD = DataOperationLogModule.PICK_TASK + ":" + "add";

    // PRINT
    /** 打印SO单1号：发票、收据、送货确认单 */
    String PRINT_SO1 = DataOperationLogModule.PRINT + ":" + "so1";
    /** 打印SO单2号：拣货标签（item） */
    String PRINT_SO2 = DataOperationLogModule.PRINT + ":" + "so2";
    /** 打印SO单3号：拣货单（order） */
    String PRINT_SO3 = DataOperationLogModule.PRINT + ":" + "so3";
    /** 打印SO单4号：拣货两件套（order+item） */
    String PRINT_SO4 = DataOperationLogModule.PRINT + ":" + "so4";
}
