package com.deer.wms.intercept.intercept;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.deer.wms.intercept.common.LoginTool;
import com.deer.wms.project.root.util.NetworkUtil;
import com.deer.wms.project.root.util.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.beust.jcommander.internal.Lists;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.common.data.CommonDataService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.Constants;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;

/**
 * 访问拦截，主要用于调用api接口前对访问权限的控制和处理
 * <p>
 * Created by guo on 2017/9/16.
 */
public class AccessIntercept extends HandlerInterceptorAdapter {

    @Autowired
    private CommonDataService commonDataService;
    @Autowired
    private LoginTool loginTool;

    private List<String> noPermissionUrl = Lists.newArrayList(
            "/login"
            , "swagger-resources"
            ,"/swagger/"
            ,"/createASN"
            ,"/updateSkuQuantity"
            ,"/addInShipNo"
            ,"/addInExResult"
            ,"/disableASN"
            ,"/createSO"
            ,"/cancel"
            ,"/item"
            ,"/getOrder"
            ,"/createOrder"
            ,"/err"
            ,"/sys/params/findByMemo"
            ,"/sys/params/all"
            ,"/print/ips/list"
            ,"/pickUpdateBill"
            ,"/forcedProcessing"
            ,"/erp/api/passquan/"
            ,"/properties/getDetail"
            ,"/inventory/adjusts/batchCreateInventoryAdjust"
            , "/webhook/setting/ex"
    );

    /**
     * 拦截处理
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        if (noPermissionUrl.stream().filter(item -> requestUri.contains(item)).count() > 0) {
            return true;
        }

        response.setContentType("text/html;charset=UTF-8");
        //设置响应请求的格式

        //获取请求对象Header中的token值
        String token = request.getHeader(Constants.TOKEN_ACCESS_KEY);
        if (StringUtils.isEmpty(token)) {
            Result r = new Result();
            r.setCode(CommonCode.MISSING_PERMISSION_PARAMTER);
            r.setMessage(CommonCode.MISSING_PERMISSION_PARAMTER.getMessage());
            response.getWriter().print(r);
            return false;
        }

        //有可能 commonDataService 为 null
        if (null == commonDataService) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            commonDataService = (CommonDataService) factory.getBean("commonDataService");
        }

        String userId = commonDataService.getUserIdByToken(token);
        if (StringUtils.isEmpty(userId)) {
            Result r = new Result();
            r.setCode(CommonCode.TOKEN_INVALID);
            r.setMessage(CommonCode.TOKEN_INVALID.getMessage());
            response.getWriter().print(r);
            return false;
        } else {
            // 重新设置失效时间
            //验证当前用户是否有权限或者是否是isAdmin=1
            if (loginTool == null){
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                loginTool = (LoginTool) factory.getBean("loginTool");
            }
            CurrentUser currentUser = commonDataService.getCurrentUserDataFromRedisByUserId(userId);
            if (null == currentUser){
                currentUser = loginTool.getLoginUserInfo(userId);
                currentUser.setIp(NetworkUtil.getIpAddress(request));
                currentUser.setToken(token);
            }
            boolean isAuth;
            List<String> authMenus = new ArrayList<>();
            if(currentUser.getIsAdmin()!=1){
                authMenus = commonDataService.getAuthDataFromRedis(userId);
                if(authMenus==null){
                    authMenus = loginTool.getUserAuthByUserId(userId);
                }
                /**
                 * 如果没有获取到权限控制的注解，说明当前访问目标不需要权限控制，直接放行通过，进入后面环节
                 */
                isAuth = this.getAuthorityAnnotation(handler,authMenus);
                if(!isAuth){
                    Result result = new Result();
                    result.setCode(CommonCode.NO_AUTH);
                    result.setMessage("您当前没有权限！");
                    response.getWriter().print(result);
                    return false;
                }
            }
            currentUser.setAuthMenus(authMenus);
            commonDataService.putCurrentUserDataToRedis(currentUser);
        }
        return true;
    }

    /**
     * 获取当前访问目标类或方法上的权限控制注解
     *
     * @param handler 当前访问目标的处理
     * @return 返回当前访问目标类或方法上的权限控制注解，如果返回null说明访问目标不需要权限控制
     */
    private Boolean getAuthorityAnnotation(Object handler,List<String> auth) {
        //获取当前访问目标类的权限控制注解
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //如果目标类没有权限控制注解，则获取当前访问方法的权限控制注解，如果都没有说明访问目标则不需要权限控制
        Method method = handlerMethod.getMethod();
        Annotation annotation = method.getAnnotation(Authority.class);
        if(annotation!=null){
            String[] methods = method.getAnnotation(Authority.class).value();
            if(!Arrays.stream(methods).anyMatch(auth::contains)){
                return false;
            }
        }
        return true;
    }
}
