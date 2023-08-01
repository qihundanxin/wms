package com.deer.wms.lzb.erp.web.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.asn.AsnDetailDto;
import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.service.AsnDetailService;
import com.deer.wms.ASN.manage.service.AsnMasterService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

@RestController
@RequestMapping("/erp/api/passquan/order")
public class OrderController {

    @Autowired
    private AsnMasterService asnMasterService;

    @Autowired
    private AsnDetailService asnDetailService;

    /**
     * 退货状态
     * 
     * passquan根据order id查看目前wms中订单的退货状态
     */
    @GetMapping("/return/{orderId}")
    @Authority("erp_api_passquan_order_return")
    public Result getReturnInfo(@PathVariable String orderId) {
        String[] prefixArr = {
                "SO-",
                "SO-8000",
                "SO-9000",
        };
        List<AsnMaster> asnMasterList = new ArrayList<>();
        for (String prefix: prefixArr) {
            AsnMaster asnMaster = asnMasterService.findByOrderId(prefix + orderId, 44, null);
            if (asnMaster != null) {
                asnMasterList.add(asnMaster);
            }
        }
        List<AsnDetail> asnDetailList = new ArrayList<>();
        for (AsnMaster asnMaster : asnMasterList) {
            List<AsnDetail> asnDetailListTemp = asnDetailService.findByBillNo(asnMaster.getBillNo());
            asnDetailList.addAll(asnDetailListTemp);
        }
        List<AsnDetailDto> returnDtoList = new ArrayList<>();
        for (AsnDetail asnDetail : asnDetailList) {
            AsnDetailDto returnDto = new AsnDetailDto();
            returnDto.setItemCode(asnDetail.getItemCode());
            returnDto.setAcceptQuantity(asnDetail.getAcceptQuantity());
            returnDto.setExpectQuantity(asnDetail.getExpectQuantity());
            returnDto.setItemName(asnDetail.getItemName());
            returnDtoList.add(returnDto);
        }
        return ResultGenerator.genSuccessResult().setData(returnDtoList);
    }
}
