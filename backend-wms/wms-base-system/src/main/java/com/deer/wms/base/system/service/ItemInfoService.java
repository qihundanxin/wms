package com.deer.wms.base.system.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Item.ItemInfoCriteria;
import com.deer.wms.base.system.model.Item.ItemInfoDto;
import com.deer.wms.base.system.model.Item.ItemType;
import com.deer.wms.base.system.model.Item.TongBuSkuCriteria;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetail;
import com.deer.wms.busine.tactic.model.QC.QcTacticDetail;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by guo on 2019/11/19.
 */
public interface ItemInfoService extends Service<ItemInfo, Integer> {

    List<ItemInfoDto> findList(ItemInfoCriteria criteria);

    // FIXME xuesinuo 检查此方法的引用，尽量不使用
    ItemInfoDto findByCode(String itemCode,Integer organizationId);
    ItemInfo  findByItemCode(String itemCode );
    List<ItemInfoDto> selectItem(ItemInfoCriteria criteria);

    List<ItemInfo> findByName(String itemName);

    // FIXME xuesinuo 检查此方法的引用，尽量不使用
    List<ItemInfo> findBySkuOrUpc(String code);

    List<ItemInfoDto> itemInfoFindByItemTypeId(Integer itemTypeId);

    void deleteByCode(String itemCode);

    String findBatchTacticCode(String itemCode);

    List<BatchTacticDetail> findBatchTacticDetails(String itemCode);

    List<QcTacticDetail> findQcTacticDetails(String itemCode);

    void modifyItemState(String itemCode, String sate, CurrentUser currentUser);

    void xhcSku(TongBuSkuCriteria criteria) throws IOException;

    int updateItem(ItemInfo itemInfo);

    ItemType selectItemTypeCode(Integer itemTypeId);

    ItemInfo selectItemWeightAndSpec(String itemCode, Integer organizationId);
    /**
     * 根据id或编码查询sku
     * @author luolin
     * @param id
     * @param itemCode SKU编码
     * @return com.deer.wms.base.system.model.Item.ItemInfoDto
     **/
    ItemInfoDto findItemByIdOrCode(Integer id, String itemCode);

    /**
     * 存储条件（干货、冷货）
     */
    public static interface StorageConditions {
        /**
         * 干货
         */
        String SCB = "SCB";
        /**
         * 冷货
         */
        String COLD = "COLD";
    }

    /**
     * 干货还是冷货？
     * 
     * @param itemCode SKU
     * @return 干货 or 冷货。返回值参考{@link StorageConditions}
     */
    @Nullable
    String getStorageConditions(String itemCode);

    /**
     * 获取特定语言的SKU名称
     * 
     * @param itemName 原SKU名
     * @return SKU名称
     */
    String getItemNameI18n(String itemName);
}
