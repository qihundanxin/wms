package com.deer.wms.base.system.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Item.ItemInfoDto;
import com.deer.wms.base.system.model.Item.ItemType;
import com.deer.wms.base.system.model.Item.ItemTypeCriteria;
import com.deer.wms.base.system.model.Item.ItemTypeDto;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.ItemTypeService;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 物料类型api接口
 * 
 * Created by guo on 2019/11/19.
 */
@RestController
@RequestMapping("/item/types")
public class ItemTypeController {

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private ItemInfoService itemInfoService;

    /**
     * 新增itemType以及itemType列表
     */
    @PostMapping("/insert")
    @Authority("item_types_insert")
    public Result add(@RequestBody ItemType itemType, @User CurrentUser currentUser) {
        String nowDate = DateUtils.getNowDateTimeString();
//        Integer organizationId = itemType.getOrganizationId();
//        String itemTypeCode="IT"+ organizationId + RandomNo.createTimeString().substring(7,14);
//        itemType.setItemTypeCode(itemTypeCode);
        itemType.setCreateTime(nowDate);
        itemTypeService.save(itemType);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除itemType信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("item_types_delete")
    public Result delete(Integer itemTypeId) {
        List<ItemInfoDto> list = itemInfoService.itemInfoFindByItemTypeId(itemTypeId);
        if (list.size() == 0) {
            itemTypeService.deleteByIdAndLowLevel(itemTypeId);
        } else {
            throw new ServiceException(CommonCode.ITEM_TYPE_EXISTS, "此物料类型正在使用");
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新itemType信息
     */
    @Transactional
    @PostMapping("/update")
    @Authority("item_types_update")
    public Result update(@RequestBody ItemType itemType) {
        itemTypeService.update(itemType);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("item_types_detail")
    public Result detail(Integer id) {
        ItemType itemType = itemTypeService.findById(id);
        return ResultGenerator.genSuccessResult(itemType);
    }

    /**
     * 物料类型查询列表
     */
    @Transactional
    @GetMapping("/list")
    @Authority("item_types_list")
    public Result list(ItemTypeCriteria criteria, @User CurrentUser currentUser) {
//        if (currentUser.getOrganizationId() == -1) {
//            criteria.setOrganizationId(-1);
//        }
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ItemTypeDto> list = itemTypeService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/treeParentList")
    @Authority("item_types_treeParentList")
    public Result list(ItemType itemType) {
        List<ItemTypeDto> itemTypes = itemTypeService.findParentList(itemType);
        List<Integer> list = itemTypeService.treeParentList(itemTypes, itemType);
        return ResultGenerator.genSuccessResult(list);
    }

    @PostMapping("/uploadItemTypes")
    @Transactional
    @Authority("item_types_uploadItemTypes")
    public Result uploadItemTypes(ImportModel importModel) throws IOException {
        InputStream is = importModel.getModel().getInputStream();
        Reader in = new InputStreamReader(is);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            ItemType parentType = itemTypeService.findBy("itemTypeCode", record.get(2).trim());
            if (parentType == null) {
                parentType = new ItemType();
                parentType.setItemTypeCode(record.get(2));
                parentType.setItemTypeName(record.get(1));
                parentType.setMemo(record.get(6));
                itemTypeService.save(parentType);
            }

            ItemType childType = itemTypeService.findBy("itemTypeCode", record.get(5).trim());
            if (childType != null) {
                itemTypeService.deleteById(childType.getItemTypeId());
            }
            childType = new ItemType();
            childType.setItemTypeCode(record.get(5));
            childType.setItemTypeName(record.get(3));
            childType.setParentId(parentType.getItemTypeId());
            childType.setMemo(record.get(6));
            itemTypeService.save(childType);
        }
        return ResultGenerator.genSuccessResult();
    }

}
