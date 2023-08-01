package com.deer.wms.base.system.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.deer.wms.base.system.dao.ItemInfoMapper;
import com.deer.wms.base.system.event.UpdateItemInfoEvent;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Item.ItemInfoCriteria;
import com.deer.wms.base.system.model.Item.ItemInfoDto;
import com.deer.wms.base.system.model.Item.ItemType;
import com.deer.wms.base.system.model.Item.TongBuSkuCriteria;
import com.deer.wms.base.system.model.xhc.Data;
import com.deer.wms.base.system.model.xhc.Products;
import com.deer.wms.base.system.model.xhc.XhcSku;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.ItemTypeService;
import com.deer.wms.busine.tactic.config.PassQuanConfig;
import com.deer.wms.busine.tactic.config.PassQuanConfig.PassQuanPathConfig;
import com.deer.wms.busine.tactic.model.Batch.BatchTacticDetail;
import com.deer.wms.busine.tactic.model.QC.QcTacticDetail;
import com.deer.wms.busine.tactic.service.BatchTacticDetailService;
import com.deer.wms.busine.tactic.service.QcTacticDetailService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.constant.Language;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.project.root.util.HttpClientUtil;
import com.deer.wms.project.root.util.I18nTool;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.service.OrganizationService;

import cn.hutool.core.util.StrUtil;

/**
 * Created by guo on 2019/11/19.
 */
@Service
@Transactional
public class ItemInfoServiceImpl extends AbstractService<ItemInfo, Integer> implements ItemInfoService {

    @Autowired
    private ItemInfoMapper itemInfoMapper;

    @Autowired
    private BatchTacticDetailService batchTacticDetailService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private QcTacticDetailService qcTacticDetailService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private PassQuanConfig passQuanConfig;

    @Autowired
    private PassQuanPathConfig passQuanPathConfig;

    @Autowired
    private CacheTool cache;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private I18nTool i18n;

    @Override
    public List<ItemInfoDto> findList(ItemInfoCriteria criteria) {
        return itemInfoMapper.findList(criteria);
    }

    @Override
    public ItemInfoDto findByCode(String itemCode, Integer organizationId) {
        return itemInfoMapper.findByCode(itemCode, organizationId);
    }

    @Override
    public ItemInfo findByItemCode(String itemCode) {
        return itemInfoMapper.findByItemCode(itemCode);
    }

    @Override
    public List<ItemInfoDto> selectItem(ItemInfoCriteria criteria) {
        return itemInfoMapper.selectItem(criteria);
    }

    @Override
    public List<ItemInfo> findByName(String itemName) {
        return itemInfoMapper.findByName(itemName);
    }

    @Override
    public List<ItemInfo> findBySkuOrUpc(String code) {
        return itemInfoMapper.findBySkuOrUpc(code);
    }

    @Override
    public List<ItemInfoDto> itemInfoFindByItemTypeId(Integer itemTypeId) {
        return itemInfoMapper.itemInfoFindByItemTypeId(itemTypeId);
    }

    @Override
    public void deleteByCode(String itemCode) {
        itemInfoMapper.deleteByCode(itemCode);
    }

    @Override
    public String findBatchTacticCode(String itemCode) {
        ItemInfo itemInfo = super.findBy("itemCode", itemCode);
        String batchTacticCode = itemInfo.getBatchTacticCode();
        List<BatchTacticDetail> list = batchTacticDetailService.findByBatchTacticCode(batchTacticCode);

        if (list.size() != 14) {
            Integer itemTypeId = itemInfo.getItemTypeId();
            ItemType itemType = itemTypeService.findById(itemTypeId);
            if (itemType != null) {
                list = batchTacticDetailService.findByBatchTacticCode(batchTacticCode);
            }
            if (list.size() != 14) {
                Integer organizationId = itemInfo.getOrganizationId();
                Organization organization = organizationService.findById(organizationId);
                batchTacticCode = organization.getBatchTacticCode();
                list = batchTacticDetailService.findByBatchTacticCode(batchTacticCode);
                if (list.size() != 14) {
                    throw new ServiceException(CommonCode.BATCH_TACTIC_ERROR, "不存在该批次属性，请在物料或物料类型或货主中新建批次属性");
                }
            }
        }
        return batchTacticCode;
    }

    @Override
    public List<BatchTacticDetail> findBatchTacticDetails(String itemCode) {
        ItemInfo itemInfo = super.findBy("itemCode", itemCode);
        String batchTacticCode = itemInfo.getBatchTacticCode();
        List<BatchTacticDetail> list = batchTacticDetailService.findByBatchTacticCode(batchTacticCode);

        if (list.size() != 14) {
            Integer itemTypeId = itemInfo.getItemTypeId();
            ItemType itemType = itemTypeService.findById(itemTypeId);
            if (itemType != null) {
                list = batchTacticDetailService.findByBatchTacticCode(batchTacticCode);
            }
            if (list.size() != 14) {
                Integer organizationId = itemInfo.getOrganizationId();
                Organization organization = organizationService.findById(organizationId);
                batchTacticCode = organization.getBatchTacticCode();
                list = batchTacticDetailService.findByBatchTacticCode(batchTacticCode);
                if (list.size() != 14) {
                    throw new ServiceException(CommonCode.BATCH_TACTIC_ERROR, "不存在该批次属性，请在物料或物料类型或货主中新建批次属性");
                }
            }
        }
        return list;
    }

    @Override
    public List<QcTacticDetail> findQcTacticDetails(String itemCode) {
        ItemInfo itemInfo = super.findBy("itemCode", itemCode);
        String qcTacticCode = itemInfo.getQcTacticCode();
        List<QcTacticDetail> list = qcTacticDetailService.findByQcTacticCode(qcTacticCode);

        if (list == null) {
            Integer itemTypeId = itemInfo.getItemTypeId();
            ItemType itemType = itemTypeService.findById(itemTypeId);
            if (itemType == null) {
                list = qcTacticDetailService.findByQcTacticCode(qcTacticCode);
            }
            if (list == null) {
                Integer organizationId = itemInfo.getOrganizationId();
                Organization organization = organizationService.findById(organizationId);
                qcTacticCode = organization.getQcTacticCode();
                list = qcTacticDetailService.findByQcTacticCode(qcTacticCode);
                if (list == null) {
                    throw new ServiceException(CommonCode.QC_TACTIC_ERROR);
                }
            }
        }
        return list;
    }

    @Override
    public void modifyItemState(String itemCode, String sate, CurrentUser currentUser) {
        ItemInfo itemInfo = itemInfoService.findBy("itemCode", itemCode);
        itemInfo.setItemStatus(sate);
        this.updateItem(itemInfo);
    }

    @Override
    public void xhcSku(TongBuSkuCriteria criteria) throws IOException {
        if (criteria == null) {
            criteria = new TongBuSkuCriteria("001", "17243");
        }
        if (criteria.getOrgCode() == null || criteria.getOrgCode() == "") {
            criteria.setOrgCode("001");
        }
        if (criteria.getShopId() == null || criteria.getShopId() == "") {
            criteria.setShopId("17243");
        }
        Organization organization = organizationService.findBy("organizationCode", criteria.getOrgCode());
        String url = passQuanConfig.getHost() + passQuanPathConfig.getGetSku() + "?shop_id=" + criteria.getShopId();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        for (Map.Entry<String, String> entry : passQuanConfig.getHeads().entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (key != null && val != null) {
                httpGet.setHeader(key, val);
            }
        }
        String res = HttpClientUtil.doGet(url, httpGet);
        XhcSku xhcSku = JSON.parseObject(res, XhcSku.class);
        List<Data> datas = xhcSku.getData();// PQ全部SKU
        List<ItemInfo> itemInfos = findAll();// WMS全部SKU
        Map<String, ItemInfo> itemInfoMap = new HashMap<>();
        for (ItemInfo item : itemInfos) {
            if (!itemInfoMap.containsKey(item.getItemCode())) {
                itemInfoMap.put(item.getItemCode(), item);// WMS item对象去重
            }
        }
        Map<String, List<Products>> skuToProduct = new HashMap<>();// PQ全部SKU
        for (Data data : datas) {
            List<Products> products = data.getProducts();
            for (Products product : products) {
                String sku = product.getSku();
                if (!skuToProduct.containsKey(sku)) {
                    skuToProduct.put(sku, new ArrayList<>());
                }
                skuToProduct.get(sku).add(product);
            }
        }
        for (Map.Entry<String, List<Products>> skuGroup : skuToProduct.entrySet()) {
            // 整理SKU与Prod的关系
            String sku = skuGroup.getKey();
            Products brandNewProduct = null;// 崭新的Product（此Prod将用于同步到SKU）：上次同步的Prod叫old，本次同步的Prod叫new，new和old对比后需要更新sku的Prod叫brandNew
            List<Products> newProducts = skuGroup.getValue();
            Map<Long, Products> newProductMap = newProducts.stream().collect(Collectors.toMap(x -> x.getProduct_id(), x -> x));
            List<Products> oldProducts = cache.getObject(CacheKey.SKU_PQ_PRODUCT, sku);
            Map<Long, Products> oldProductMap = Np.i(oldProducts).x(x -> x.stream().collect(Collectors.toMap(prod -> prod.getProduct_id(), prod -> prod))).o(new HashMap<>());
            // 有新的product，需要更新
            for (Map.Entry<Long, Products> newPItem : newProductMap.entrySet()) {
                Long prodId = newPItem.getKey();
                Products newProduct = newPItem.getValue();
                Products oldProduct = oldProductMap.get(prodId);
                if (oldProduct == null) {// 匹配到一个以前没有的product
                    brandNewProduct = newProduct;
                    break;
                } else if (Np.i(newProduct.getName()).notEq(oldProduct.getName())) {// 匹配到有变化
                    brandNewProduct = newProduct;
                    break;
                }
            }
            // 以前的product被删除了，而且没有新的，随意取一个product进行更新
            if (brandNewProduct == null) {// 上一步没匹配到新的prod
                Products used = null;
                for (Products oldProduct : oldProducts) {
                    if (oldProduct.getWmsUsed()) {
                        used = oldProduct;
                        break;
                    }
                }
                if (used == null) {// 上一次同步没有记录使用了哪个Prod，随便选一个
                    brandNewProduct = newProducts.get(0);
                } else if (!newProductMap.containsKey(used.getProduct_id())) {// 上次同步使用的Prod已经不在本次同步的Prods中，被删了
                    brandNewProduct = newProducts.get(0);
                } else {
                    Products newUsed = newProductMap.get(used.getProduct_id());
                    newUsed.setWmsUsed(used.getWmsUsed());
                    if (!newUsed.equals(used)) {// 新旧相比有变化
                        brandNewProduct = newUsed;
                    }
                    newUsed.setWmsUsed(null);
                }
            }

            // 同步SKU操作：
            if (brandNewProduct != null) {// brandNewProduct==null说明prod无变化，无需同步
                String type = brandNewProduct.getCategory();
                if (type != null) {
                    String[] types = type.split("\\|");
                    if (types.length == 2) {
                        type = types[1];
                    }

                }
                Integer itemTypeId = null;
                String itemTypeCode = null; // 被郭总后来改成了name... 不知道谁下的产品需求
                List<ItemType> itemTypes = itemTypeService.findByCode(type);
                if (itemTypes.size() > 0) {
                    itemTypeId = itemTypes.get(0).getItemTypeId();
                    itemTypeCode = itemTypes.get(0).getItemTypeCode();
                } else {
                    ItemType itemType = new ItemType();
                    itemType.setItemTypeCode(type);
                    itemType.setItemTypeName(type);
                    itemType.setItemTypeState(1);
                    itemType.setOrganizationId(organization.getOrganizationId());
                    itemType.setParentId(1742);
                    itemTypeService.save(itemType);
                    itemTypeId = itemType.getItemTypeId();
                    itemTypeCode = itemType.getItemTypeCode();
                }
                if (sku != null && !sku.trim().equals("")) {
                    ItemInfo itemInfo = itemInfoMap.get(sku);
                    if (itemInfo != null) {
                        // 修改SKU
                        if (itemInfo.getItemName() == null
                                || itemInfo.getImgUrl() == null
                                || !itemInfo.getItemName().equals(brandNewProduct.getName())
                                || !itemInfo.getImgUrl().equals(brandNewProduct.getImage_url())
                                || !itemTypeCode.equals(itemInfo.getItemTypeCode())) {
                            itemInfo.setItemCode(sku);
                            itemInfo.setItemName(brandNewProduct.getName());
                            itemInfo.setItemBarCode(String.valueOf(brandNewProduct.getProduct_id()));
                            if (brandNewProduct.getShort_desc() != null && brandNewProduct.getShort_desc().length() > 600) {
                                itemInfo.setItemDesc(brandNewProduct.getShort_desc().substring(0, 600));
                            } else {
                                itemInfo.setItemDesc(brandNewProduct.getShort_desc());
                            }
                            itemInfo.setItemTypeId(itemTypeId);
                            itemInfo.setItemTypeCode(itemTypeCode);
                            itemInfo.setImgUrl(brandNewProduct.getImage_url());
                            this.updateItem(itemInfo);
                        }
                    } else {
                        // 新增SKU
                        itemInfo = new ItemInfo();
                        itemInfo.setItemCode(sku);
                        itemInfo.setItemName(brandNewProduct.getName());
                        itemInfo.setItemBarCode(String.valueOf(brandNewProduct.getProduct_id()));
                        if (brandNewProduct.getShort_desc() != null && brandNewProduct.getShort_desc().length() > 600) {
                            itemInfo.setItemDesc(brandNewProduct.getShort_desc().substring(0, 600));
                        } else {
                            itemInfo.setItemDesc(brandNewProduct.getShort_desc());
                        }
                        itemInfo.setImgUrl(brandNewProduct.getImage_url());
                        itemInfo.setItemTypeId(itemTypeId);
                        itemInfo.setItemTypeCode(itemTypeCode);
                        itemInfo.setPackCode("sys_pack");
                        itemInfo.setBatchTacticCode("sys_batch_tactic");
                        itemInfo.setOrganizationId(organization.getOrganizationId());
                        super.save(itemInfo);
                    }
                }

                // 更新同步SKU的“上次缓存”
                cache.del(CacheKey.SKU_PQ_PRODUCT, sku);
                for (Products p : newProducts) {
                    if (p.getProduct_id() == brandNewProduct.getProduct_id()) {
                        p.setWmsUsed(true);
                    } else {
                        p.setWmsUsed(false);
                    }
                }
                cache.setObject(CacheKey.SKU_PQ_PRODUCT, sku, newProducts, CacheTime.SKU_PQ_PRODUCT);
            }
        }
    }


    /**
     * 修改商品信息
     * @param itemInfo
     * @return
     */
    public int updateItem(ItemInfo itemInfo){
        int i = this.update(itemInfo);
        if(i > 0) {
            itemInfo = this.findById(itemInfo.getId());
            applicationEventPublisher.publishEvent(new UpdateItemInfoEvent(itemInfo));
        }
        return i;
    }

    @Override
    public ItemType selectItemTypeCode(Integer itemTypeId) {
        return itemTypeService.findById(itemTypeId);
    }

    @Override
    public ItemInfo selectItemWeightAndSpec(String itemCode, Integer organizationId) {
        return itemInfoMapper.selectItemWeightAndSpec(itemCode,organizationId);
    }

    @Override
    public ItemInfoDto findItemByIdOrCode(Integer id, String itemCode) {
        if (id != null && itemCode != null){
            itemCode = null;
        }
        ItemInfoDto itemInfo = itemInfoMapper.findItemByIdOrCode(id, itemCode);
        return itemInfo;
    }

    @Override
    public String getStorageConditions(String itemCode) {
        if (StrUtil.isBlank(itemCode)) {
            return null;
        }
        if (Np.i(itemCode.substring(0, 1).toUpperCase()).in("C", "D")) {
            return StorageConditions.COLD;
        }
        return StorageConditions.SCB;
    }

    @Override
    public String getItemNameI18n(String itemName) {
        Language language = i18n.getLanguage();
        if (language == null || StrUtil.isBlank(itemName)) {
            return itemName;
        }
        String[] itemNameArray = itemName.split("\\|");
        if (language == Language.CN && itemNameArray.length >= 1) {
            return itemNameArray[0];
        }
        if (language == Language.EN && itemNameArray.length >= 2) {
            return itemNameArray[1];
        }
        return itemName;
    }
}
