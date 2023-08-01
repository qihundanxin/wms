package com.deer.wms.base.system.service.MESWebService;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;

/**
 * 入库单 服务层实现
 *
 * @author cai
 * @date 2019-07-17
 */
@Service
public class BillOutDetailServiceImpl implements IBillOutDetailService {

    /**
     * 包装工单入立体库请求
     */
    @Override
    @Transactional
    public synchronized WebserviceResponse InStockReq(@Param("input") String input) {
        WebserviceResponse webServiceResponse = null;

        BillOutWorkerOrder billOutWorkerOrder = null;
        try {
            //解析字符串（此处断点及下面的方法中可断点查看解析是否正确或者有没有值）
            billOutWorkerOrder = splitEmptyShelfArriveXmlCode(input);
            //逻辑处理（可能与WCS交互或者取数据）

//			处理成功给MES返回值(可做判断，处理失败的话此处errorCode不为0，errormsg返回报错信息)
            webServiceResponse = new WebserviceResponse(billOutWorkerOrder.getTaskCode(), "0", "OK", null);
        } catch (Exception e) {
            e.printStackTrace();
//			报错则返回以下内容
            webServiceResponse = new WebserviceResponse(billOutWorkerOrder.getTaskCode(), "-1", e.getMessage(), null);
            throw new ServiceException(CommonCode.CARRIER_CODE_EXISTS);
        } finally {
//			此条一定会执行
            return webServiceResponse;
        }
    }

    /**
     * 空载具到达出库输送线的入库口
     */
    @Override
    public WebserviceResponse EmptyShelfReq(String input) {
        WebserviceResponse webServiceResponse = null;
        BillOutWorkerOrder billOutWorkerOrder = null;
        try {
            //解析字符串（此处断点及下面的方法中可断点查看解析是否正确或者有没有值）
            billOutWorkerOrder = splitEmptyShelfArriveXmlCode(input);
            //逻辑处理（判断有无空框，如果告诉WCS出空框）

            //此判断假设有空框可以出
            if (true) {
//			处理成功给MES返回值(可做判断，处理失败的话此处errorCode不为0，errormsg返回报错信息)
                webServiceResponse = new WebserviceResponse(billOutWorkerOrder.getTaskCode(), "0", "OK", null);
            } else {//如果没有则返回以下信息
                webServiceResponse = new WebserviceResponse(billOutWorkerOrder.getTaskCode(), "0", "NG", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
//			当出错是提示报错信息
            webServiceResponse = new WebserviceResponse(billOutWorkerOrder.getTaskCode(), "-1", e.getMessage(), null);
            throw new ServiceException(CommonCode.CARRIER_CODE_EXISTS);
        } finally {
            //此处一定会执行不管报不报错
            return webServiceResponse;
        }
    }

    /**
     * 解析MES下发工单字符串
     *
     * @param xmlCode
     * @return
     * @throws Exception
     */
    private void splitDownWipToStockXmlCode(String xmlCode) throws Exception {

        try {
            Document doc = DocumentHelper.parseText(xmlCode);
            Element root = doc.getRootElement();
            List<Element> elements = root.elements();
            root.attributeValue("macCode");
            root.attributeValue("taskCode");
            root.attributeValue("wipEntity");

            String code = "设备编码";
            for (Element element : elements) {
                String tagCode = element.attributeValue("tagCode");
                String tagValue = element.attributeValue("tagValue");
                if (tagCode.equals(code + "_1000")) {
//					billOutWorkerOrder.setFinishedCode(tagValue);
                } else if (tagCode.equals(code + "_1001")) {
//					billOutWorkerOrder.setQuantity(Integer.parseInt(tagValue));
                } else if (tagCode.equals(code + "_1002")) {
//					billOutWorkerOrder.setPriority(tagValue);
                } else if (tagCode.equals(code + "_1003")) {
//					billOutWorkerOrder.setItemCode(tagValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析Mes  xml字符串
     *
     * @param xmlCode
     * @return
     * @throws Exception
     */
    private BillOutWorkerOrder splitEmptyShelfArriveXmlCode(String xmlCode) throws Exception {
        BillOutWorkerOrder billOutWorkerOrder = new BillOutWorkerOrder();
        try {
            Document doc = DocumentHelper.parseText(xmlCode);
            Element root = doc.getRootElement();
            billOutWorkerOrder.setMacCode(root.attributeValue("macCode"));
            billOutWorkerOrder.setTaskCode(root.attributeValue("taskCode"));
//			root.attributeValue("shelfCode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return billOutWorkerOrder;
    }

}

