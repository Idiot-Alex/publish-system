package com.hotstrip.publish.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.hotstrip.publish.common.annotation.NotNullParam;
import com.hotstrip.publish.common.util.Const;
import com.hotstrip.publish.model.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by idiot on 2017/6/27.
 * @description   拦截 非空参数
 */
public class NotNullParamInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(NotNullParamInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 如果是静态资源拦截类型 直接返回 true
        if (o instanceof ResourceHttpRequestHandler){
            return true;
        }
        // 获取非空参数的注解
        HandlerMethod handlerMethod = (HandlerMethod) o;
        NotNullParam notNullParam = handlerMethod.getMethodAnnotation(NotNullParam.class);
        // 判断是否为空   以及params是否有值
        if(notNullParam != null && notNullParam.params().length > 0){
            boolean result = false;
            // 设置响应头
            httpServletResponse.setHeader("Content-Type","application/json;charset=UTF-8");
            // 遍历需要拦截的参数name
            for(String param : notNullParam.params()){
                // 根据参数name获取参数value
                String paramValue = httpServletRequest.getParameter(param);
                // 判断参数value是否为null或者""  如果是就返回json 数据   停止执行下去
                if(paramValue == null || "".equals(paramValue)){
                    logger.error("exception url: [{}]......param: [{}] is null", httpServletRequest.getRequestURL().toString(), param);
                    result = false;
                    // 输出错误信息
                    String errorMessage  = JSON.toJSONString(R.error(Const.ERROR_PARAM, "invalid param: {" + param + ":" + paramValue + "}"));
                    httpServletResponse.getWriter().write(errorMessage);
                    break;
                }else {
                    result = true;
                }
            }
            return result;
        }else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }
}
