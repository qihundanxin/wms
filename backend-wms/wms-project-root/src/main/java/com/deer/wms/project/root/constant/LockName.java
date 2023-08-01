package com.deer.wms.project.root.constant;

/**
 * 共用的锁名称，不重复即可<br>
 * 
 * 场景需要区分Key时，结尾增加冒号分隔；不区分Key则不需要冒号。
 * 
 * @author xuesinuo 2022-03-01
 *
 */
public interface LockName {
    /** 举例 */
    String EX = "l0";
    /** 小红车Order顺序处理锁 */
    String XHC_ORDER = "l1:";
    /** 拣货任务 */
    String PICK_TASK = "l2:";
    /** 波次任务 */
    String GENERATE_WAVES_TASK = "l3:";
    /** 拣货单任务 */
    String RY_TASK = "l4:";
    /** 库存操作 */
    String INVENTORY = "l5:";
    /** 验收操作 */
    String ACCEPT = "l6:";
    /** SO单优先级 */
    String SO_SET_PRIORITY = "l7:";
    /** 拣货 */
    String PICK_AFFIRM = "l8:";
    /** SO + SKU 拣货 */
    String PICK_AFFIRM_SO_SKU = "l9:";
    /** batch + SKU 拣货 */
    String PICK_AFFIRM_BATCH = "l10:";
}
