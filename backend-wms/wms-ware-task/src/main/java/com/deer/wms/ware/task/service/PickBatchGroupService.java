package com.deer.wms.ware.task.service;

import java.util.Collection;
import java.util.List;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.ware.task.model.pickTask.PickBatchGroup;
import com.deer.wms.ware.task.model.pickTask.PickBatchResponse;

/**
 * @author lixuehe
 * @date 2023/6/1
 */
public interface PickBatchGroupService {


    void saveGroupBatchAndDetail(PickBatchResponse result, String driverName, CurrentUser user);

    /**
     * 查询列表，默认不查询
     * 
     * @param criteria 查询条件
     * @return 查询结果
     */
    List<PickBatchGroup> findList(PickBatchGroup criteria);

    /**
     * 更新状态，只更新有效的波次组，已经取消的不更新
     * 
     * @param pickBatchGroupNo 波次组
     * @param status           更新成的状态
     */
    void updateStatus(String pickBatchGroupNo, Integer status);

    /**
     * 只查询有效的 根据波次组编号去查询波次组
     * 
     * @author han.li
     * @date 2023/07/18 16:17
     * @param pickBatchGroupNo
     * @return com.deer.wms.ware.task.model.pickTask.PickBatchGroup
     */
    PickBatchGroup getByNo(String pickBatchGroupNo);

    /**
     * 根据编号查询
     * 
     * @param pickBatchGroupNoCollection
     * @return
     */
    List<PickBatchGroup> getByNos(Collection<String> pickBatchGroupNos);

    /**
     * 全量修改！根据ID修改
     * 
     * @param pickBatchGroup 含ID，修改后的结果
     */
    void putById(PickBatchGroup pickBatchGroup);
}
