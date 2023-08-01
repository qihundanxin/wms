package com.deer.wms.ASN.manage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.ASN.manage.model.asn.AsnData;
import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.service.AsnDetailService;
import com.deer.wms.ASN.manage.service.AsnMasterService;
import com.deer.wms.base.system.model.ItemUpc;
import com.deer.wms.base.system.service.ItemUpcService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.system.manage.model.carrier.Carrier;
import com.deer.wms.system.manage.service.CarrierService;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.xhcOrderInfo.Items;
import com.deer.wms.ware.task.model.xhcOrderInfo.Order;
import com.deer.wms.ware.task.service.So2AsnConnector;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoMasterService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class So2AsnConnectorImpl implements So2AsnConnector {

    private final SoMasterService soMasterService;
    private final SoDetailService soDetailService;
    private final AsnMasterService asnMasterService;
    private final AsnDetailService asnDetailService;
    private final CarrierService carrierService;
    private final ItemUpcService itemUpcService;

    @Override
    public String autoSaveReturnAsn(Order order) {
        Date now = DateUtils.getNowDateTime();
        String nowString = DateUtils.dateToStr(now, DateUtils.DEFAULT_DATETIME_FORMAT);
        // 新出库单详情
        Map<String, Integer> sku2NewQty = new HashMap<>();
        // 单子和数量
        if (order.getItems().size() > 0) {
            for (Items item : order.getItems()) {
                if (item.getRevision() != null && item.getRevision().getSku() != null && item.getQty() > 0) {
                    int thisQty = Np.i(sku2NewQty.get(item.getRevision().getSku())).o(0);
                    sku2NewQty.put(item.getRevision().getSku(), thisQty + item.getQty());
                }
            }
        } else {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "生成退货入库异常：PQ推送来订单无详情信息");
        }
        // 已有出库单、详情
        SoMaster soMaster = soMasterService.findBy("allotBillNo", String.valueOf(order.getId()));
        if (soMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "生成退货入库异常：未找到出库单" + order.getId() + "，可能存在丢单。");
        }
        List<SoDetail> soDetails = soDetailService.findSelfByBillNo(soMaster.getBillNo());
        if (soDetails == null || soDetails.isEmpty()) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "生成退货入库异常：" + soMaster.getBillNo() + "出库单无Detail信息。" + order.getId());
        }
        // 对比详情数量
        Map<String, SoDetail> sku2SoDetail = new HashMap<String, SoDetail>();// 仅保留重复SKU项里的最后一个，用于SKU去重，用于单位换算；多单位支持需要修改此处
        Map<String, Double> sku2OldQty = new HashMap<String, Double>();
        for (SoDetail soDetail : soDetails) {
            if (soDetail.getState() > 0) {
                sku2SoDetail.put(soDetail.getItemCode(), soDetail);
                Double thisQty = Np.i(sku2OldQty.get(soDetail.getItemCode())).o(0D);
                sku2OldQty.put(soDetail.getItemCode(), thisQty + soDetail.getOrderQuantity() * Np.i(soDetail.getTransRatio()).o(1D));// 重复SKU数量累加，其他详情进行替换
            }
        }
        Map<String, Double> sku2DeltaQty = new HashMap<String, Double>();
        Set<String> removeSkuKeysSet = new HashSet<>();
        for (String sku : sku2SoDetail.keySet()) {
            // sku2DeltaQty计算变化量（退货数量）
            Integer newQty = Np.i(sku2NewQty.get(sku)).o(0);
            Double oldQty = Np.i(sku2OldQty.get(sku)).o(0D);
            Double deltaQty = oldQty - Double.valueOf(sku2SoDetail.get(sku).getTransRatio() * newQty);
            if (deltaQty > 0) {
                sku2DeltaQty.put(sku, deltaQty);
            } else {
                removeSkuKeysSet.add(sku);
            }
        }
        for (String sku : removeSkuKeysSet) {
            sku2SoDetail.remove(sku);
        }
        if (sku2SoDetail.isEmpty()) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未生成退货入库：" + order.getId() + "SKU数量均未减少");
        }
        // 创建或修改入库单
        AsnMaster asnMaster = asnMasterService.findByOrderId(soMaster.getBillNo(), 44, soMaster.getWareId());
        if (asnMaster == null) {// 创建入库单
            Integer createUserId = 206;
            String createUserName = "xhctest";
            // data封装
            AsnData asnData = new AsnData();
            asnData.setBillSource(5);
            asnData.setInsertType(0);
            // master
            asnMaster = new AsnMaster();
            asnMaster.setBillType(1);
            asnMaster.setOrderNo(1);
            asnMaster.setWareId(soMaster.getWareId());
            asnMaster.setWareCode(soMaster.getWareCode());
            asnMaster.setWareName(soMaster.getWareName());
            asnMaster.setOrganizationId(soMaster.getOrganizationId());
            asnMaster.setOrganizationCode(soMaster.getOrganizationCode());
            asnMaster.setOrganizationName(soMaster.getOrganizationName());
            asnMaster.setCreateTime(nowString);
            asnMaster.setCreateUserId(createUserId);
            asnMaster.setCreateUserName(createUserName);
            asnMaster.setAsnState(0);
            asnMaster.setAsnUdfHs3(soMaster.getBillNo());
            asnMaster.setSoBillNo(soMaster.getBillNo());
            if (soMaster.getCarrierId() != null) {
                Carrier carrier = carrierService.findById(soMaster.getCarrierId());
                asnMaster.setCarrierCode(carrier.getCarrierCode());
            }
            asnMaster.setInsertType(0);
            asnMaster.setBillSource(5);
            asnMaster.setAsnType(44);
            asnMaster.setQcState(-1);
            asnMaster.setIsDeleted("n");
            asnMaster.setVersion(1);
            asnData.setAsnMaster(asnMaster);
            // detail
            List<AsnDetail> details = new ArrayList<>(sku2SoDetail.size());
            for (String sku : sku2SoDetail.keySet()) {
                AsnDetail asnDetail = new AsnDetail();
                SoDetail soDetail = sku2SoDetail.get(sku);
                asnDetail.setState(0);
                asnDetail.setQcState(1);
                asnDetail.setItemCode(soDetail.getItemCode());
                asnDetail.setItemName(soDetail.getItemName());
                asnDetail.setImgUrl(soDetail.getImgUrl());
                asnDetail.setPackCode("sys_pack");
                asnDetail.setPackDetailId(soDetail.getPackDetailId());
                asnDetail.setPackDescribe(soDetail.getPackDescribe());
                asnDetail.setTransRatio(soDetail.getTransRatio());
                List<ItemUpc> itemUpcList = itemUpcService.findByItemCode(soDetail.getItemCode());
                if (!itemUpcList.isEmpty()) {
                    ItemUpc itemupc = itemUpcList.stream().filter(x -> x.getOrganizationId() != null && x.getOrganizationId().equals(soMaster.getOrganizationId())).findFirst().orElseGet(null);
                    if (itemupc != null) {
                        asnDetail.setUpc(itemupc.getUpc());
                    }
                }
                asnDetail.setExpectQuantity(sku2DeltaQty.get(sku));
                asnDetail.setAcceptQuantity(0D);
                asnDetail.setAcceptTransRatio(soDetail.getTransRatio());
                asnDetail.setUpperQuantity(0D);
                asnDetail.setUpperTransRatio(soDetail.getTransRatio());
                asnDetail.setVersion(1);
                details.add(asnDetail);
            }
            asnData.setAsnDetails(details);
            // user
            CurrentUser createUser = new CurrentUser();
            createUser.setUserId(createUserId);
            createUser.setUserName(createUserName);
            asnMasterService.createAsnMaster(asnData, createUser);
            // 回显BillNo
            asnMaster = asnMasterService.findByOrderId(soMaster.getBillNo(), 44, soMaster.getWareId());
        } else {// 修改入库单
            List<AsnDetail> details = asnDetailService.findByBillNo(asnMaster.getBillNo());
            Map<String, AsnDetail> detailsMap = details.stream().collect(Collectors.toMap(x -> x.getItemCode(), x -> x));
            int i = details.size();
            // ASN状态 0-初始化 1-已码盘 2-部分收货 3-全部收货 4-ASN完成 5-超额收货
            Map<Integer, Integer> asnState2Count = new HashMap<>(9);
            asnState2Count.put(0, 0);
            asnState2Count.put(1, 0);
            asnState2Count.put(2, 0);
            asnState2Count.put(3, 0);
            asnState2Count.put(4, 0);
            asnState2Count.put(5, 0);
            for (String sku : sku2SoDetail.keySet()) {
                AsnDetail asnDetail = detailsMap.get(sku2SoDetail.get(sku).getItemCode());
                if (asnDetail != null) {// 修改已有detail
                    Double oldQty = asnDetail.getExpectQuantity();
                    Double newQty = sku2DeltaQty.get(sku);
                    if (oldQty.equals(newQty) || oldQty > newQty) {
                        continue;// 数量未更改 || 退货数量变小
                    }
                    asnDetail.setExpectQuantity(newQty);
                    Integer state = asnDetail.getState();
                    if (state == null || state == 2 || state == 3 || state == 4 || state == 5) {
                        if (asnDetail.getAcceptQuantity() > asnDetail.getExpectQuantity()) {
                            asnDetail.setState(5);
                        } else if (asnDetail.getAcceptQuantity() < asnDetail.getExpectQuantity()) {
                            asnDetail.setState(2);
                        } else {
                            asnDetail.setState(3);
                        }
                    }
                    asnDetailService.update(asnDetail);
                } else {// 新增detail
                    asnDetail = new AsnDetail();
                    SoDetail soDetail = sku2SoDetail.get(sku);
                    asnDetail.setBillNo(asnMaster.getBillNo());
                    asnDetail.setDetailNo(asnMaster.getBillNo() + "-" + (++i));

                    asnDetail.setState(0);
                    asnDetail.setQcState(1);
                    asnDetail.setItemCode(soDetail.getItemCode());
                    asnDetail.setItemName(soDetail.getItemName());
                    asnDetail.setImgUrl(soDetail.getImgUrl());
                    asnDetail.setPackCode("sys_pack");
                    asnDetail.setPackDetailId(soDetail.getPackDetailId());
                    asnDetail.setPackDescribe(soDetail.getPackDescribe());
                    asnDetail.setTransRatio(soDetail.getTransRatio());
                    List<ItemUpc> itemUpcList = itemUpcService.findByItemCode(soDetail.getItemCode());
                    if (!itemUpcList.isEmpty()) {
                        ItemUpc itemupc = itemUpcList.stream().filter(x -> x.getOrganizationId() != null && x.getOrganizationId().equals(soMaster.getOrganizationId())).findFirst().orElseGet(null);
                        if (itemupc != null) {
                            asnDetail.setUpc(itemupc.getUpc());
                        }
                    }
                    asnDetail.setExpectQuantity(sku2DeltaQty.get(sku));
                    asnDetail.setAcceptQuantity(0D);
                    asnDetail.setAcceptTransRatio(soDetail.getTransRatio());
                    asnDetail.setUpperQuantity(0D);
                    asnDetail.setUpperTransRatio(soDetail.getTransRatio());
                    asnDetail.setVersion(1);
                    asnDetailService.save(asnDetail);
                }
                // 统计detail的ASN状态，换算master状态
                asnState2Count.put(asnDetail.getState(), asnState2Count.get(asnDetail.getState()) + 1);
            }
            // 状态转换
            boolean updateMaster = true;
            if (asnState2Count.get(2) > 0) {
                asnMaster.setAsnState(2);
            } else if (asnState2Count.get(5) > 0) {
                asnMaster.setAsnState(5);
            } else if (asnState2Count.get(3) > 0) {
                asnMaster.setAsnState(3);
            } else {
                updateMaster = false;
            }
            if (updateMaster) {
                asnMasterService.update(asnMaster);
            }
        }
        return asnMaster.getBillNo();
    }
}
