package com.deer.wms.system.manage.configurer;

import com.deer.wms.intercept.annotation.PdaUserAnnotationMethodArgumentResolver;
import com.deer.wms.intercept.annotation.UserAnnotationMethodArgumentResolver;
import com.deer.wms.intercept.intercept.AccessIntercept;
import com.deer.wms.intercept.intercept.PdaLoginIntercept;
import com.deer.wms.project.root.configurer.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

/**
 * Created by guo on 2017/9/29.
 */
@Configuration
public class SystemManageWebMvcConfigurer extends WebMvcConfigurer {

    @Bean
    public PdaLoginIntercept getPdaLoginIntercept(){
        return new PdaLoginIntercept();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessIntercept()).addPathPatterns("/**").excludePathPatterns("/pda/**");
        registry.addInterceptor(getPdaLoginIntercept()).addPathPatterns("/pda/**").excludePathPatterns("/pda/user/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new UserAnnotationMethodArgumentResolver());
        argumentResolvers.add(new PdaUserAnnotationMethodArgumentResolver());
    }
}
