package com.deer.wms.ware.task.quartz;

import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.ware.task.model.PassQResponse;
import com.deer.wms.ware.task.model.SO.SoMasterCriteria;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.service.SoMasterService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
public class PassQ {
    @Autowired
    private OrderService orderService;

    @Autowired
    private SoMasterService soMasterService;

    private Integer interval = 10;

    @Autowired
    private CacheTool cache;

    public PassQResponse getMissedBillNos(String shopId) {
        JsonNode data = orderService.getUnfinishedOrders(true, shopId);
        Map<String, Instant> billNoMap = new HashMap<>();
        List<String> unFinishedBillNos = new ArrayList<>();
        data.forEach(order -> {
            JsonNode types = order.get("types");
            if (types.get(0) == null) {
                return;
            }
            Instant confirmAt = Instant.now();
            JsonNode confirmAtNode = order.get("confirm_at");
            if (!confirmAtNode.isNull()) {
                confirmAt = Instant.ofEpochSecond(confirmAtNode.intValue());
            }
            int id = order.get("id").intValue();
            List<String> orderBillNos = new ArrayList<>();
            if ("8000".equals(types.get(0).textValue()) && types.get(1) == null) {
                billNoMap.put("SO-" + id, confirmAt);
                unFinishedBillNos.add("SO-" + id);
            } else {
                billNoMap.put("SO-" + types.get(0).textValue() + id, confirmAt);
                unFinishedBillNos.add("SO-" + types.get(0).textValue() + id);
                if (types.get(1) != null) {
                    unFinishedBillNos.add("SO-" + types.get(1).textValue() + id);
                    billNoMap.put("SO-" + types.get(1).textValue() + id, confirmAt);
                }
            }
        });
        PassQResponse response = new PassQResponse();
        response.setUnFinishedNos(unFinishedBillNos);
        SoMasterCriteria criteria = new SoMasterCriteria();
        criteria.setBillNos(new ArrayList<>(billNoMap.keySet()));

        if (criteria.getBillNos().size() == 0) {
            response.setMissBillNos(Collections.emptyList());
        }
        List<SoMasterDto> soMasterDtoList = soMasterService.findList(criteria);
        soMasterDtoList.forEach(soMasterDto -> billNoMap.remove(soMasterDto.getBillNo()));
        List<String> billNos = new ArrayList<>();
        Instant threshold = Instant.now().minus(Duration.ofMinutes(interval));
        billNoMap.forEach((billNo, confirmAt) -> {
            if (confirmAt.isBefore(threshold)) {
                billNos.add(billNo);
            }
        });
        response.setMissBillNos(billNos);
        return response;
    }
}
