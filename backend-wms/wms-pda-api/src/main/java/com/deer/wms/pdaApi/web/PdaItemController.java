package com.deer.wms.pdaApi.web;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Item.ItemInfoDto;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 产品接口
 * @author luolin
 **/
@ApiResultPacking
@RestController
@RequestMapping("/pda/itemInfo")
@RequiredArgsConstructor
public class PdaItemController {

    @Autowired
    private ItemInfoService itemInfoService;

    @Data
    public static class ItemFromVo{

        private Integer id;

        /**
         * SKU编码
         **/
        private String itemCode;
    }

    @Data
    public static class ItemResultVo{

        private  Integer id;

        /**
         * SKU编码
         **/
        private String itemCode;

        /**
         * SKU名称
         **/
        private String itemName;

        private String imgUrl;

        /**
         * 供应商ID
         **/
        private Integer supplierId;

        /**
         * 供应商名称
         **/
        private String supplierName;

        /**
         * 货主ID
         **/
        private Integer organizationId;

        /**
         * 货主名称
         **/
        private String organizationName;

        /**
         * KG重量
         **/
        private BigDecimal weight;

        /**
         * 是否要称重
         */
        private Boolean weighting;

        /**
         * 是否是散件
         */
        private Boolean unpacking;
    }

    /**
     * 查询产品
     * @author luolin
     * @param itemFromVo
     * @return com.deer.wms.pdaApi.web.PdaItemController.ItemResultVo
     **/
    @PostMapping("/getById")
    @Authority("pda_itemInfo_getById")
    public ItemResultVo getById(@RequestBody ItemFromVo itemFromVo){
        if (itemFromVo.getId() == null && itemFromVo.getItemCode() == null){
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "ID和itemCode不可同时为空");
        }
        ItemInfoDto item = itemInfoService.findItemByIdOrCode(itemFromVo.getId(), itemFromVo.getItemCode());
        ItemResultVo result = new ItemResultVo();
        if (item != null){
            item.setItemName(itemInfoService.getItemNameI18n(item.getItemName()));
            BeanUtils.copyProperties(item, result);
        }
        return result;
    }
}
