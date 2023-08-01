package com.deer.wms.lzb.erp.web.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.ware.task.quartz.OrderService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController("RpcOrderController")
@RequestMapping("/erp/rpc/passquan/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 刷新司机
     * @return
     */
    @RequestMapping("/assignedOrders")
    @Authority("erp_rpc_passquan_order_assignedOrders")
    public Result getAssignedOrders() {
        JsonNode result = orderService.getUnfinishedOrders(false, null);
        if (result != null) {
            return ResultGenerator.genSuccessResult(result);
        }
        throw new ServiceException(CommonCode.HTTP_METHOD_ERROR);
    }
}
