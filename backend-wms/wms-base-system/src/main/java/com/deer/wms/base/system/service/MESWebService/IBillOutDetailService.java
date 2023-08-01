package com.deer.wms.base.system.service.MESWebService;

import java.util.List;

/**
 * 出库单 服务层
 *
 * @author cai
 * @date 2019-05-13
 */
public interface IBillOutDetailService {

    WebserviceResponse InStockReq(String input);

    WebserviceResponse EmptyShelfReq(String input);

}
