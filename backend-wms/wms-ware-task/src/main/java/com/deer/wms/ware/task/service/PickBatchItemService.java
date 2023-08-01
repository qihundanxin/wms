package com.deer.wms.ware.task.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.deer.wms.ware.task.model.pickTask.PickBatchItem;
import com.deer.wms.ware.task.model.pickTask.PickBatchItemDto;

/**
 * 波次拣货Item Service
 * 
 * @author xuesinuo
 */
public interface PickBatchItemService {
    /**
     * 只更新有效的ItemInfo
     * 
     * @param pickBatchGroupNo 波次组
     * @param status           新状态
     */
    void updateStatusByGroup(String pickBatchGroupNo, Integer status);

    /**
     * 根据ID更新
     * 
     * @param pickBatchItem item对象
     */
    void updateById(PickBatchItem pickBatchItem);

    /**
     * 注意：不包含已取消
     * 
     * @param pickBatchGroupNo 波次组
     * @return 全部详情
     */
    List<PickBatchItem> getByPickBatchGroupNo(String pickBatchGroupNo);

    /**
     * 注意：不包含已取消
     * 
     * @param pickBatchNo 波次
     * @return 全部详情
     */
    List<PickBatchItem> getByPickBatchNo(String pickBatchNo);

    /**
     * 注意：不包含已取消
     * 
     * @param detailId detailId
     * @return 全部详情
     */
    List<PickBatchItem> getByDetailId(Long detailId);

    /**
     * 添加一个Item
     * 
     * @param pickBatchItem Adder
     * @return Result
     */
    PickBatchItem add(PickBatchItem pickBatchItem);

    /**
     * 根据二维码查询一个有效的Item
     * 
     * @return Result
     */
    PickBatchItem getByQrCode(String qrCode);

    /**
     * 将一组波次Item汇总出一个状态
     * 
     * @param items 波次Item集合
     * @return 最终状态 -1取消 0未拣货 1部分完成 2已完成
     */
    Integer summaryBatchStatus(Collection<PickBatchItem> items);

    /**
     * 根据id批量修改item状态
     * @author luolin
     * @param subBatchItemList 需要修改状态的item集合
     * @return
     **/
    void batchUpdateStatusById(List<PickBatchItem> subBatchItemList);

    /**
     * 根据时间用户查询拣货数量
     * @author han.li
     * @date 2023/07/17 16:45
     * @param startDate
     * @param endDate
     * @param userId
     * @return long
     */
    Double getPickTotalByUserAndDate(Date startDate,Date endDate,  Integer userId);

    /**
     * PickBatchItem转Dto
     * 
     * @param item item
     * @return dto
     */
    PickBatchItemDto entityToDto(PickBatchItem item);

    /**
     * 查询异常item
     * @author luolin
     * @return java.util.List<com.deer.wms.ware.task.model.pickTask.PickBatchItem>
     **/
    List<PickBatchItem> getExceptionItem();

    /**
     * 批量添加pick_item_list
     * @author luolin
     * @param pickBatchItemList
     **/
    void insertList(List<PickBatchItem> pickBatchItemList);

    /**
     * 调用前，将创建、更新好的<b>Item先存入数据库！！！</b><br>
     * 本方法计算并刷新：【波次组】【波次】【波次Detail】的【状态】【数量】等
     * 
     * @param itemIds Item的ID集合
     */
    void refreshPickBatchInfoByItems(Collection<Long> itemIds);

    /**
     * 根据Id查询，只查询有效的
     * 
     * @param itemIds ids
     * @return 查询结果
     */
    List<PickBatchItem> getByIds(Collection<Long> itemIds);

    /**
     * 根据GroupNos查询，只查询有效的
     * 
     * @param pickBatchGroupNos 组编号集合
     * @return 查询结果
     */
    List<PickBatchItem> getByPickBatchGroupNos(Collection<String> pickBatchGroupNos);

    /**
     * 根据QR查询全部，包含失效的
     * 
     * @param qrCode 二维码
     * @return 查询结果
     */
    List<PickBatchItem> getAllByQr(String qrCode);
}
