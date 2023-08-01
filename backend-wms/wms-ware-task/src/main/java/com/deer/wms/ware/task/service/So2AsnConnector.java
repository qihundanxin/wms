package com.deer.wms.ware.task.service;

import com.deer.wms.ware.task.model.xhcOrderInfo.Order;

/**
 * 20220809: <br>
 * 现有依赖方式：task -> asn，无法实现asn中调用task。<br>
 * 依赖倒置，task只提供此接口，启动时没有找到AsnConnector的实现，将无法实现AsnConnector功能<br>
 * @author xuesinuo
 */
public interface So2AsnConnector {
	/**
	 * 
	 * @param order WebHook推送的Order全信息
	 * @return 退货单号
	 */
	String autoSaveReturnAsn(Order order);
}
