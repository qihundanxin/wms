package com.deer.wms.base.system.service.MESWebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
@Service
@Lazy
public class MesWebServiceImpl implements MesWebService {

    @Autowired
    private IBillOutDetailService billOutDetailService;

    @Override
    public WebserviceResponse macIntf(@WebParam(name = "methodInvoke") String methodInvoke,
                                      @WebParam(name = "input") String input) {
        WebserviceResponse webServiceResponse = null;
//        判断走哪个接口
        try {

            if (methodInvoke.equals("InStockReq")) {
                //MES询问WMS包装入立体库
                webServiceResponse = billOutDetailService.InStockReq(input);
            } else if (methodInvoke.equals("EmptyShelfReq")) {
                //MES询问立体库是否有空框
                webServiceResponse = billOutDetailService.EmptyShelfReq(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webServiceResponse;
    }
}
