package com.hotstrip.publish.common.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Created by idiot on 2017/1/4.
 * @description 自定义拦截器配置
 */
@Configuration
public class AdminWebAppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //添加拦截器  生效
        registry.addInterceptor(new NotNullParamInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");
    }

}
