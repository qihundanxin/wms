package com.deer.wms.project.root.util;

/**
 * HTTP请求工具，不再HTTP上下文环境中时，返回null
 * 
 * @author xuesinuo
 *
 */
public interface HttpRequestTool {
    /**
     * 获取当前请求的token
     * 
     * @return token
     */
    String getToken();

    /**
     * 获取当前登录人ID
     * 
     * @return userId
     */
    Long getUserId();
}
