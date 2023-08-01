package com.deer.wms.base.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Item.ItemInfoCriteria;
import com.deer.wms.base.system.model.Item.ItemInfoDto;
import com.deer.wms.project.root.core.mapper.Mapper;

public interface ItemInfoMapper extends Mapper<ItemInfo> {

    ItemInfo findByItemCode(@Param("itemCode") String itemCode);

    ItemInfoDto findByCode(@Param("itemCode") String itemCode, @Param("organizationId") Integer organizationId);
    List<ItemInfoDto> findList(ItemInfoCriteria criteria);
    List<ItemInfoDto> selectItem(ItemInfoCriteria criteria);

    List<ItemInfo> findByName(@Param("itemName") String itemName);

    List<ItemInfo> findBySkuOrUpc(@Param("code") String code);

    List<ItemInfoDto> itemInfoFindByItemTypeId(@Param("itemTypeId") Integer itemTypeId);

    void deleteByCode(@Param("itemCode") String itemCode);

    ItemInfo selectItemWeightAndSpec(@Param("itemCode")String itemCode, @Param("organizationId")Integer organizationId);

    ItemInfoDto findItemByIdOrCode(@Param("id") Integer id, @Param("itemCode") String itemCode);
}
