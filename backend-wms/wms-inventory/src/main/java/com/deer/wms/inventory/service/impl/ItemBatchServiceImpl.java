package com.deer.wms.inventory.service.impl;

import com.deer.wms.base.system.model.Item.*;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.ItemTypeService;
import com.deer.wms.system.manage.service.OrganizationService;
import com.deer.wms.busine.tactic.model.Batch.*;
import com.deer.wms.busine.tactic.service.BatchTacticDetailService;
import com.deer.wms.busine.tactic.service.BatchTacticService;
import com.deer.wms.inventory.dao.ItemBatchMapper;
import com.deer.wms.inventory.model.Inventory.ItemBatch;
import com.deer.wms.inventory.model.Inventory.ItemBatchCriteria;
import com.deer.wms.inventory.model.Inventory.ItemBatchDto;
import com.deer.wms.inventory.service.ItemBatchService;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.GenerateSelfId;
import com.deer.wms.system.manage.service.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/12/02.
 */
@Service
@Transactional
public class ItemBatchServiceImpl extends AbstractService<ItemBatch, Integer> implements ItemBatchService {

    @Autowired
    private ItemBatchMapper itemBatchMapper;

    @Autowired
    private ItemBatchService itemBatchService;

    @Autowired
    private BatchTacticService batchTacticService;

    @Autowired
    private BatchTacticDetailService batchTacticDetailService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private ValidatorService validatorService;

    @Override
    public List<ItemBatchDto> findList(ItemBatchCriteria criteria) {
        return itemBatchMapper.findList(criteria);
    }

    @Override
    public List<ItemBatchDto> findByOrganizationIdAndItemCode(Integer organizationId, String itemCode) {
        return itemBatchMapper.findByOrganizationIdAndItemCode(organizationId, itemCode);
    }

    /***
     *
     * @param itemBatch   批次属性值
     * @return 返回新生成的批次号，或者合并到的批次号
     * 这个方法用来在验收或者其他业务中生成新的批次，或者合并到原有批次
     * 1.获取该物料所使用的批次策略，如果该物料没有设置，从物料类型中去找，如果类型中也没有，去货主里面找
     * 2.判断批次属性的详情，哪个字段是启用的
     * 3.判断所有的启用的字段，在数据库中找有没有可以合并的批次，如果有就合并，如果没有就新生成一个
     *
     * 备注：上面的合并这里解释一下，如果新生成为批次和数据库原有的一模一样，那还干嘛要生成
     *
     */
    @Override
    public ItemBatch createItemBatch(ItemBatch itemBatch) {

        List<BatchTacticDetail> list = null;
        ItemInfo itemInfo = itemInfoService.findBy("itemCode", itemBatch.getItemCode());
        if (itemInfo == null) {
            list = batchTacticDetailService.findByBatchTacticCode("sys_batch_tactic");
        } else {
            list = batchTacticDetailService.findByBatchTacticCode(itemInfo.getBatchTacticCode());
        }

        ItemBatchCriteria criteria = new ItemBatchCriteria();
        criteria.setItemCode(itemBatch.getItemCode());

        for (BatchTacticDetail batchTacticDetail : list) {

            /**
             * 批次属性-生产日期
             */
            if (batchTacticDetail.getDetailCode().equals("productDate")) {
                if (itemBatch.getProductDate() != null && !itemBatch.getProductDate().equals("")) {
                    if (batchTacticDetail.getState() == 1) {
                        criteria.setProductDate(itemBatch.getProductDate());
                    } else if (batchTacticDetail.getState() == 0) {
                        itemBatch.setProductDate(null);
                    }
                }

            }

            /**
             * 批次属性-失效日期
             */
            if (batchTacticDetail.getDetailCode().equals("exDate")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setExDate(itemBatch.getExDate());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setExDate(null);
                }
            }

            /**
             * 批次属性-入库日期
             */
            if (batchTacticDetail.getDetailCode().equals("inDate")) {
                if (itemBatch.getInDate() != null && !itemBatch.getInDate().equals("")) {
                    if (batchTacticDetail.getState() == 1) {
                        criteria.setInDate(itemBatch.getInDate());
                    } else if (batchTacticDetail.getState() == 0) {
                        itemBatch.setInDate(null);
                    }
                }

            }
            /**
             * 批次属性-供应商代码
             */
            if (batchTacticDetail.getDetailCode().equals("supplierCode")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setSupplierCode(itemBatch.getSupplierCode());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setSupplierCode(null);
                }
            }
            /**
             * 批次属性-供应商批次
             */
            if (batchTacticDetail.getDetailCode().equals("supplierBatch")) {
                if (itemBatch.getSupplierBatch() != null && !itemBatch.getSupplierBatch().equals("")) {
                    if (batchTacticDetail.getState() == 1) {
                        criteria.setSupplierBatch(itemBatch.getSupplierBatch());
                    } else if (batchTacticDetail.getState() == 0) {
                        itemBatch.setSupplierBatch(null);
                    }
                }

            }
            /**
             * 批次属性-质量状态
             */
            if (batchTacticDetail.getDetailCode().equals("qcState")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setQcState(itemBatch.getQcState());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setQcState(null);
                }
            }
            /**
             * 批次属性-成本单价
             */
            if (batchTacticDetail.getDetailCode().equals("costPrice")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setCostPrice(itemBatch.getCostPrice());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setCostPrice(null);
                }
            }
            /**
             * 批次属性-销售单价
             */
            if (batchTacticDetail.getDetailCode().equals("salePrice")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setSalePrice(itemBatch.getSalePrice());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setSalePrice(null);
                }
            }
            /**
             * 批次属性-单据号
             */
            if (batchTacticDetail.getDetailCode().equals("detailNo")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setDetailNo(itemBatch.getDetailNo());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setDetailNo(null);
                }
            }
            /**
             * 单位重量
             */
            if (batchTacticDetail.getDetailCode().equals("weight")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setWeight(itemBatch.getWeight());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setWeight(null);
                }
            }
            /**
             * 批次属性-单据扩展字段1
             */
            if (batchTacticDetail.getDetailCode().equals("memo1")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setMemo1(itemBatch.getMemo1());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setMemo1(null);
                }
            }
            /**
             * 批次属性-单据扩展字段2
             */
            if (batchTacticDetail.getDetailCode().equals("memo2")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setMemo2(itemBatch.getMemo2());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setMemo2(null);
                }
            }
            /**
             * 批次属性-单据扩展字段3
             */
            if (batchTacticDetail.getDetailCode().equals("memo3")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setMemo3(itemBatch.getMemo3());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setMemo3(null);
                }
            }
            /**
             * 批次属性-用户自定义1
             */
            if (batchTacticDetail.getDetailCode().equals("batchAttribute1")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setBatchAttribute1(itemBatch.getBatchAttribute1());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setBatchAttribute1(null);
                }
            }
            /**
             * 批次属性-用户自定义2
             */
            if (batchTacticDetail.getDetailCode().equals("batchAttribute2")) {
                if (batchTacticDetail.getState() == 1) {
                    criteria.setBatchAttribute2(itemBatch.getBatchAttribute2());
                } else if (batchTacticDetail.getState() == 0) {
                    itemBatch.setBatchAttribute2(null);
                }
            }
        }

        List<ItemBatchDto> itemBatchDtos = itemBatchService.findList(criteria);
        if (itemBatchDtos.size() == 0) {
            String tableName = "item_batch";
            String nextId = validatorService.getNextIdString(tableName);
            String batchName = "IB" + "-" + nextId;
//            String batchName =itemInfo.getId() + "-"+DateUtils.getNowDateTimeString2() +"-"+ GenerateSelfId.getSelfIdByUUId().substring(1,4);
//            String batchName1= criteria.getProductDate().toString().substring(1,3)+criteria.getExDate().toString().substring(1,3)+criteria.getInDate().toString().substring(1,3)
//                    + criteria.getSupplierCode().substring(1,3)+criteria.getSupplierBatch().substring(1,3)+criteria.getQcState()+criteria.getCostPrice().toString().substring(1,3)
//                    +criteria.getSalePrice().toString().substring(1,3)+criteria.getDetailNo().substring(1,3)+criteria.getMemo1().substring(1,3)
//                    +criteria.getMemo2().substring(1,3)+criteria.getMemo3().substring(1,3)
//                    +criteria.getBatchAttribute1().substring(1,3)+criteria.getBatchAttribute2().substring(1,3);
            if(itemBatch.getSupplierBatch()!=null && !itemBatch.getSupplierBatch().trim().equals("")){
                itemBatch.setBatchName(itemBatch.getSupplierBatch());
            }else {
                itemBatch.setBatchName(batchName);
            }

            itemBatchService.save(itemBatch);
        } else if (itemBatchDtos.size() > 0) {
            itemBatch = itemBatchDtos.get(0);
        }

        return itemBatch;

    }
}
