package com.deer.wms.ware.task.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.pickTask.PickBatchItem;

public interface PickBatchItemMapper extends Mapper<PickBatchItem> {

    /**
     * 根据波次组修改状态
     * 
     * @param pickBatchGroupNo
     * @param oldStatus
     * @param newStatus
     */
    void updateStatusByGroup(@Param("pickBatchGroupNo") String pickBatchGroupNo, @Param("status") Integer status);

    /**
     * 更新状态
     * 
     * @param id
     * @param status
     */
    void update(Long id, Integer status);

    /**
     * 按波次组查询
     * 
     * @param pickBatchGroupNo 波次组
     * @return
     */
    List<PickBatchItem> getByPickBatchGroupNo(@Param("pickBatchGroupNo") String pickBatchGroupNo);

    /**
     * 按多个波次组查询
     * 
     * @param pickBatchGroupNos 波次组
     * @return
     */
    List<PickBatchItem> getByPickBatchGroupNos(@Param("pickBatchGroupNos") Collection<String> pickBatchGroupNos);

    /**
     * 按波次查询
     * 
     * @param pickBatchNo 波次
     * @return
     */
    List<PickBatchItem> getByPickBatchNo(String pickBatchNo);

    /**
     * 二维码查询有效的波次item
     * 
     * @param qrCode 二维码
     * @return 波次Item
     */
    PickBatchItem getByQrCode(String qrCode);

    /**
     * 按波次详情查询
     * 
     * @param detailId 详情id
     * @return 波次Item
     */
    List<PickBatchItem> getByDetailId(Long detailId);

    /**
     * 根据时间查询当前用户拣货数量
     * @author han.li
     * @date 2023/07/17 16:48
     * @param startDate
     * @param endDate
     * @param userId
     * @return Double
     */
    Double getPickTotalByUserAndDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate,  @Param("userId") Integer userId);
    /**
     * 根据id批量修改item状态
     * @author luolin
     * @param pickBatchItem 需要修改状态的item集合
     * @return
     **/
    void batchUpdateStatusById(List<PickBatchItem> pickBatchItem);

    /**
     * 查询异常item
     * @author luolin
     * @return java.util.List<com.deer.wms.ware.task.model.pickTask.PickBatchItem>
     **/
    List<PickBatchItem> getExceptionItem();

}
