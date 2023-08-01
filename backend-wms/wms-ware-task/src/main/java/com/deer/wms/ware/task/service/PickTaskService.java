package com.deer.wms.ware.task.service;

import java.util.List;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.model.pickTask.PickParam;
import com.deer.wms.ware.task.model.pickTask.PickSkuQtyBySoParam;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.model.pickTask.PickTaskCriteria;
import com.deer.wms.ware.task.model.pickTask.PickTaskDto;
import com.deer.wms.ware.task.model.pickTask.PickTaskMoveCriteria;
import com.deer.wms.ware.task.model.pickTask.PickTaskResetParam;
import com.deer.wms.ware.task.model.pickTask.SeedingWallAlertInfo;

/**
 * Created by guo on 2020/02/02.
 */
public interface PickTaskService extends Service<PickTask, Integer> {
    List<PickTaskDto> findList(PickTaskCriteria criteria);

    List<PickTaskDto> findByBillNo(PickTaskCriteria criteria);

    List<PickTaskDto> findByWaveMasterId(PickTaskCriteria criteria);
    List<PickTaskDto> findRunningByBillNo(PickTaskCriteria criteria);

    List<PickTaskDto> findByBox(PickTaskCriteria criteria);

    List<PickTaskDto> allotTask(PickTaskCriteria criteria);

    void createPickTask(SoDetailDto soDetail);
    void createPickTask3(SoDetailDto soDetail);

    List<PickTask> findBySoDetailId(Integer soDetailId);

    List<PickTaskDto> findFirst(PickTaskCriteria criteria);

    void createBaoKuanPickTask(Inventory inventory, Integer acceptRecordId);

    List<PickTaskDto> findBySo(String soNo);

    List<PickTaskDto> findByWave(Integer waveId);

    List<SeedingWallAlertInfo> getSeedingWallAlertInfo(PickTaskCriteria criteria);

    List<PickTaskDto> getChildTasksByParentId(Integer parentId);

    // TODO 与findById功能重复
    PickTaskDto getTaskById(Integer taskId);

    List<PickTask> findBySoMasterId(Integer soMaterId);

    void ex(Integer waveMasterId, String boxCode, Integer wareId, Integer pickTaskId, String toCellCode, Double pickQuantity, Double exQuantity, CurrentUser currentUser);

    void deleteByIdVersion(Integer pickTaskId, Integer version);

    void mergeTask();

    List<PickTask> findPickingByInventory(Integer inventoryId);

    void pickTaskMove(PickTaskMoveCriteria criteria,CurrentUser currentUser);

    List<PickTaskDto> pickTaskMoveList(PickTaskCriteria criteria);


    /**
     * 确认拣货。
     * 
     * <pre>
     * 【确认拣货包含的功能：】
     * · 验证目标货位号（设置缺省值）
     * · 验证容器号
     * · 验证数量
     * · 验证重复扫码（一物一码、一码一拣）
     * · 更新SO单状态更新
     * · 更新SO详情单状态更新
     * · 记录绩效
     * · 移库（拣出库存扣减、删除，拣入库存创建）
     * · 拣货任务状态更新
     * · 记录库存事务
     * · 拣货任务执行记录PickTaskExecute
     * 【工作模式，二进制表示：】
     * xxx0：普通拣货，拣货完成后不复核。
     * xxx1：边捡边分，拣货完成后状态变为复核。
     * xx0x：容器号不必须。
     * xx1x：容器号必须。
     * x0xx：不允许部分拣货，拣货数量不对则抛出异常。
     * x1xx：允许部分拣货，累计拣货数量超出拣货任务则抛出异常。
     * 0xxx：不限制QrCode。
     * 1xxx：必须一物一码（需要有QrCode且和之前的QrCode不重复）。
     * </pre>
     * 
     * @param type  拣货工作模式
     * @param param 确认拣货参数
     */
    void pick(int type, PickParam param);

    /**
     * SO单 + SKU 确认拣货。
     * 
     * <pre>
     * 【功能包括：】
     * 1. 根据SO + SKU查询拣货任务。验证拣货任务剩余数量 不小于 本次拣货数量。
     * 2. 拣货任务前n个Qty，分次调用确认拣货。
     * 
     * 【工作模式，二进制表示：】
     * 参考{@link PickTaskService}.pick
     * </pre>
     * 
     * @param type  拣货工作模式
     * @param param 确认拣货参数
     */
    void pickAsSoAndSku(int type, PickSkuQtyBySoParam param);


    List<PickTaskDto> findListByParam(PickTaskCriteria criteria);

    /**
     * 拣货任务重置
     * 
     * @param param 重置参数
     */
    void pickTaskReset(PickTaskResetParam param);

    /**
     * 分配波次拣货异常的拣货任务
     * @author luolin
     * @return
     **/
    void createBatchExceptionPickTask();
}
