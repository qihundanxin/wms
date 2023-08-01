package com.deer.wms.ware.task.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.model.pickTask.PickTaskCriteria;
import com.deer.wms.ware.task.model.pickTask.PickTaskDto;
import com.deer.wms.ware.task.model.pickTask.SeedingWallAlertInfo;

public interface PickTaskMapper extends Mapper<PickTask> {
    List<PickTaskDto> findList(PickTaskCriteria criteria);

    List<PickTaskDto> findByBillNo(PickTaskCriteria criteria);

    List<PickTaskDto> findByWaveMasterId(PickTaskCriteria criteria);
    List<PickTaskDto> findRunningByBillNo(PickTaskCriteria criteria);

    List<PickTaskDto> findByBox(PickTaskCriteria criteria);

    List<PickTaskDto> allotTask(PickTaskCriteria criteria);

    List<PickTask> findBySoDetailId(@Param("soDetailId") Integer soDetailId);

    List<PickTaskDto> findFirst(PickTaskCriteria criteria);

    List<PickTaskDto> findBySo(@Param("soNo") String soNo);

    List<PickTaskDto> findByWave(@Param("waveId") Integer waveId);

    List<SeedingWallAlertInfo> getSeedingWallAlertInfo(PickTaskCriteria criteria);

    List<PickTaskDto> mergeTask();

    List<PickTask> findMergeTask(PickTaskDto pickTaskDto);

    List<PickTaskDto> getChildTasksByParentId(@Param("parentId") Integer parentId);

    PickTaskDto getTaskById(@Param("taskId") Integer taskId);

    List<PickTask> findBySoMasterId(@Param("soMaterId") Integer soMaterId);

    PickTask findByWaveIdAndItemCode(@Param("waveMasterId") Integer waveMasterId, @Param("itemCode") String itemCode);

    int update(PickTask pickTask);

    int deleteByIdVersion(@Param("pickTaskId") Integer pickTaskId, @Param("version") Integer version);

    List<PickTask> findPickingByInventory(Integer inventoryId);

    List<PickTaskDto> pickTaskMoveList(PickTaskCriteria criteria);
}