package com.deer.wms.ware.task.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.deer.wms.ware.task.model.pickTask.MyBatchKanbanRespVO;
import com.deer.wms.ware.task.model.pickTask.PickBatch;
import com.deer.wms.ware.task.model.pickTask.PickBatchDto;
import com.deer.wms.ware.task.model.pickTask.PickBatchParam;
import com.deer.wms.ware.task.model.pickTask.PickBatchSearchParam;

/**
 * @author lixuehe
 * @date 2023/6/1
 */
public interface PickBatchService {

    /**
     * 只更新有效的波次
     * 
     * @param pickBatchGroupNo 波次组
     * @param status           更新后的状态
     */
    void updateStatusByGroup(String pickBatchGroupNo, Integer status);

    PickBatch getByNo(String pickBatchNo);

    /**
     * Id查询波次
     * 
     * @param id id
     * @return 波次
     */
    PickBatch getById(Long id);

    /**
     * 实体转Dto 增加一些扩展属性
     * 
     * @param pickBatch 实体
     * @return Dto
     */
    PickBatchDto entityToDto(PickBatch pickBatch);

    /**
     * 更新状态
     * 
     * @param pickBatchNo 被更新的波次
     * @param status      状态
     */
    void updateStatusByNo(String pickBatchNo, Integer status);

    PickBatch save(PickBatch pickBatch);

    /**
     * 单一波次拣货
     * @author luolin
     * @param pickParam 确认拣货参数
     **/
    void pickBatch(PickBatchParam pickParam);

    /**
     * 批发单拣货
     * @author luolin
     * @param type 拣货工作模式
     * 【工作模式，二进制表示：】
     * 参考{@link PickTaskService}.pick
     * @param pickParam 确认拣货参数
     * @return
     **/
    void pickBatchWholesale(int type, PickBatchParam pickParam);

    /**
     * 根据时间去查询当前看板信息
     * @param startDate 时间区间开始时间
     * @param endDate 时间区间结束时间
     * @param userId 用户id
     * @return
     */
    MyBatchKanbanRespVO getBatchKanbanByDate(Date startDate, Date endDate, Integer userId);

    /**
     * 查询列表，默认不查询无效状态的
     * 
     * @param param 查询参数
     * @return 查询结果
     */
    List<PickBatch> getBatchList(PickBatchSearchParam param);

    /**
     * 查询Dto列表，带一些扩展结果，默认不查询无效状态的
     * 
     * @param param 查询参数
     * @return 查询结果
     */
    List<PickBatchDto> getBatchDtoList(PickBatchSearchParam param);

    /**
     * 为我标记一个波次
     * @author han.li
     * @date 2023/07/18 17:10
     * @param pickBatchNo
     * @param userId
     * @return com.deer.wms.ware.task.model.pickTask.PickBatchDto
     */
    PickBatchDto tagBatch(String pickBatchNo,Integer userId);

    /**
     * 根据波次组编号查询，只查询有效的
     * 
     * @param pickBatchGroupNo 波次组编号
     * @return list
     */
    List<PickBatch> getByGroupNo(String pickBatchGroupNo);

    /**
     * 根据多个编号查询
     * 
     * @param pickBatchNos 编号集合
     * @return list
     */
    List<PickBatch> getByNos(Collection<String> pickBatchNos);

    /**
     * 全量更新！根据ID更新
     * 
     * @param pickBatch 含ID的对象
     */
    void putById(PickBatch pickBatch);

}
