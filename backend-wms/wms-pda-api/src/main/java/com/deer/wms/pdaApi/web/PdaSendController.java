package com.deer.wms.pdaApi.web;

import cn.hutool.core.util.StrUtil;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.PdaUser;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.configurer.ApiResultPacking;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.model.QrEntity;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.ware.task.model.ScanLog;
import com.deer.wms.ware.task.model.Send.SendDetail;
import com.deer.wms.ware.task.model.Send.SendDetailParam;
import com.deer.wms.ware.task.model.Send.SendOrderInfo;
import com.deer.wms.ware.task.model.Send.SendOutCriteria;
import com.deer.wms.ware.task.quartz.OrderService;
import com.deer.wms.ware.task.service.ScanLogService;
import com.deer.wms.ware.task.service.SendDetailService;
import com.deer.wms.ware.task.service.SoMasterService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * PDA发运相关接口
 */
@ApiResultPacking
@RestController
@RequestMapping("/pda/send")
@RequiredArgsConstructor
public class PdaSendController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private SoMasterService soMasterService;

    @Autowired
    private SendDetailService sendDetailService;

    @Autowired
    private ScanLogService scanLogService;

    @Data
    public static class SendScanParams {
        /**
         * 二维码
         **/
        @NotBlank
        private String qrCode;

        /**
         * 发运数量
         **/
        @NotNull
        @Min(1)
        private double qty;
    }

    @Data
    public static class SendOrdersParams {

        @NotBlank
        private String sendIds;
    }

    /**
     * 刷新司机
     * @return
     */
    @PostMapping("/assignedOrders")
    @Authority("pda_send")
    public SimpleMap getAssignedOrders() {
        JsonNode result = orderService.getUnfinishedOrders(false, null);
        if (result == null) {
            throw new ServiceException(CommonCode.HTTP_METHOD_ERROR);
        }
        return SimpleMap.init().append("list", result);
    }

    /**
     * 发运订单详情
     */
    @PostMapping("/getSendOrders")
    @Authority("pda_send")
    public SimpleMap getSendOrders(@RequestBody @Validated SendOrdersParams params) {
        String[] ids = params.getSendIds().split(",");
        String[] soPrefix = {
                "SO-",
                "SO-8000",
                "SO-9000",
        };
        ArrayList<String> billNos = new ArrayList<>();
        for(int i = 0; i < ids.length; i++) {
            for(int j = 0; j < soPrefix.length; j++) {
                billNos.add(soPrefix[j] + ids[i]);
            }
        }

        SendDetailParam param = new SendDetailParam();
        param.setBillNos(billNos);
        List<SendDetail> result = sendDetailService.findList(param);
        return SimpleMap.init().append("list", result);
    }

    /**
     * 获取本日的发运数据
     * @param currentUser
     */
    @PostMapping("/history")
    @Authority("pda_send")
    public SimpleMap todaySendHistory(@PdaUser CurrentUser currentUser) {
        SendDetailParam param = new SendDetailParam();
        param.setSendUserId(currentUser.getUserId());
        param.setStartSendTime(DateUtils.getDateStartTime(DateUtils.now()));
        param.setEndSendTime(DateUtils.getDateEndTime(DateUtils.now()));
        List<SendOrderInfo> list = sendDetailService.findOrderInfo(param);
        HashMap<String, List> resultMap = new HashMap();

        for(SendOrderInfo info: list) {
            String driverName = info.getDriverName();
            if(resultMap.containsKey(driverName)) {
                List oldList = resultMap.get(driverName);
                oldList.add(info);
                resultMap.put(driverName, oldList);
            } else {
                List newList = new ArrayList();
                newList.add(info);
                resultMap.put(driverName, newList);
            }
        }

        return SimpleMap.init().append("drivers", resultMap);
    }

    /**
     * 发运扫码
     */
    @PostMapping("/scan")
    @Authority("pda_send")
    public SimpleMap sendOut(@RequestBody @Validated SendScanParams params, @PdaUser CurrentUser currentUser) {
        String qrCode = params.getQrCode();
        QrEntity qrEntity = QrEntity.stringToEntity(QrEntity.QrStringType.SIMPLE_STRING, qrCode);
        if (qrEntity != null &&
                StrUtil.isNotBlank(qrEntity.getSo()) &&
                StrUtil.isNotBlank(qrEntity.getSku())) {
            //记录扫码日志
            ScanLog log = new ScanLog();
            log.setCreateTime(new Date());
            log.setScanValue(qrCode);
            log.setCreateUserId(currentUser.getUserId());
            log.setScanType(3);
            scanLogService.save(log);

            //发运
            SendOutCriteria criteria =  new SendOutCriteria();
            criteria.setSendQuantity(params.getQty());
            criteria.setItemCode(qrEntity.getSku());
            criteria.setSoBillNo(qrEntity.getSo());
            soMasterService.skuSendOut(criteria, currentUser);
            String orgSoId = qrEntity.getSo().replace("SO-8000", "").replace("SO-9000", "").replace("SO-", "");

            return SimpleMap.init().append("orderID", orgSoId);
        }
        throw new ServiceException(CommonCode.SERVICE_ERROR, "二维码解析错误");
    }
}
