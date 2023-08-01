package com.deer.wms.ware.task.quartz;

import com.fasterxml.jackson.databind.JsonNode;

public interface OrderService {
    JsonNode getUnfinishedOrders(Boolean ignoreDriver, String shopId);

}
