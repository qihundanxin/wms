package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.Shelf.ShelfCell;
import com.deer.wms.base.system.model.Shelf.ShelfInfo;
import com.deer.wms.base.system.model.Shelf.ShelfInfoCriteria;
import com.deer.wms.base.system.model.Shelf.ShelfInfoDto;
import com.deer.wms.project.root.core.service.Service;
import org.apache.ibatis.annotations.Param;
import com.deer.wms.system.manage.model.ValidatorShelfInfoModel;

import java.util.List;

/**
 * Created by guo on 2019/11/27.
 */
public interface ShelfInfoService extends Service<ShelfInfo, Integer> {

    List<ShelfInfoDto> findList(ShelfInfoCriteria criteria);

    ShelfInfoDto findByCodeAndWareId(Integer wareId, String shelfCode);

    List<ShelfInfoDto> findByCode(String shelfCode);
    /**
     * @Author luolin
     * @Description 验证货位组编码重复
     * @Date 14:44 2023/6/25
     * @param model
     * @return int
     **/
    int validatorChongfu(ValidatorShelfInfoModel model);

    /**
     * @Author luolin
     * @Description 添加货架
     * @Date 16:08 2023/6/25
     * @param shelfCell
     * @return
     **/
    void saveShelfInfo(ShelfCell shelfCell);

    /**
     * @Author luolin
     * @Description 修改货位组
     * @Date 17:04 2023/6/25
     * @param shelfCell
     * @return
     **/
    void updateShelfInfo(ShelfCell shelfCell);

    /**
     * @Author luolin
     * @Description 删除货位组
     * @Date 17:31 2023/6/25
     * @param shelfInfoId
     * @return
     **/
    void deleteByshelfInfoId(Integer shelfInfoId);
}
