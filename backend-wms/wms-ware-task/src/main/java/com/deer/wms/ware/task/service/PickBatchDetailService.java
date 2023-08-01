package com.deer.wms.ware.task.service;

import java.util.Collection;
import java.util.List;

import com.deer.wms.ware.task.model.pickTask.PickBatchDetail;

/**
 * @author lixuehe
 * @date 2023/6/1
 */
public interface PickBatchDetailService {

    /**
     * 只更新有效的Detail
     * 
     * @param pickBatchGroupNo 波次组
     * @param status           更新后的状态
     */
    void updateStatusByGroup(String pickBatchGroupNo, Integer status);

    /**
     * 根据ID更新状态
     * 
     * @param id     id
     * @param status 状态
     */
    void updateStatusById(Long id, Integer status);

    PickBatchDetail save(PickBatchDetail detail);

    /**
     * 根据波次号查询List 只查询有效的
     * 
     * @param pickBatchNo 波次号
     * @return 查询结果
     */
    List<PickBatchDetail> findByPickBatchNo(String pickBatchNo);

    /**
     * 波次拣货更新detail
     * @author luolin
     * @param pickBatchDetail
     * @return
     **/
    void updatePickBatchDetail(PickBatchDetail pickBatchDetail);

    /**
     * 根据波次组No查询
     * 
     * @param pickBatchGroupNo 波次组No
     * @return list
     */
    List<PickBatchDetail> findByPickBatchGroupNo(String pickBatchGroupNo);

    /**
     * 按多个ID查询
     * 
     * @param pickBatchDetailIds ids
     * @return 查询结果
     */
    List<PickBatchDetail> getByIds(Collection<Long> pickBatchDetailIds);

    /**
     * 全量更新！按照ID修改
     * 
     * @param pickBatchDetail 含ID的对象
     */
    void putById(PickBatchDetail pickBatchDetail);
}
