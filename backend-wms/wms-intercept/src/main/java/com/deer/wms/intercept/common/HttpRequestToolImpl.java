package com.deer.wms.intercept.common;

import javax.servlet.http.HttpServletRequest;

import com.deer.wms.project.root.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deer.wms.intercept.common.data.CommonDataService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.Constants;
import com.deer.wms.project.root.util.HttpRequestTool;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpRequestToolImpl implements HttpRequestTool {

    @Autowired
    HttpServletRequest request;
    @Autowired
    CommonDataService commonDataService;

    @Override
    public String getToken() {
        try {
            if (request != null) {
                return request.getHeader(Constants.TOKEN_ACCESS_KEY);
            }
        } catch (Exception e) {
            log.error("获取token失败", e);
        }
        return null;
    }

    @Override
    public Long getUserId() {
        String token = this.getToken();
        if (token == null) {
            return null;
        }
        String userId = commonDataService.getUserIdByToken(token);
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        return Integer.valueOf(userId).longValue();
    }

}
