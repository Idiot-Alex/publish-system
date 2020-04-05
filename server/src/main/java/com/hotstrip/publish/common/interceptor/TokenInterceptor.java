package com.hotstrip.publish.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.hotstrip.publish.common.annotation.RequireToken;
import com.hotstrip.publish.common.util.Const;
import com.hotstrip.publish.common.util.TokenUtil;
import com.hotstrip.publish.model.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by idiot on 2018/2/5.
 */
public class TokenInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws IOException {
        // 如果是静态资源拦截类型 直接返回 true
        if (o instanceof ResourceHttpRequestHandler){
            return true;
        }
        // 获取token的注解
        HandlerMethod handlerMethod = (HandlerMethod) o;
        RequireToken requireToken = handlerMethod.getMethodAnnotation(RequireToken.class);
        // 如果不存在RequireToken注解  返回true
        if (requireToken == null || requireToken.value() == false){
            return true;
        }
        //设置响应头
        httpServletResponse.setHeader("Content-Type","application/json;charset=UTF-8");
        // 获取token的参数值
        String tokenValue = httpServletRequest.getHeader(Const.TOKEN);
        if (StringUtils.isEmpty(tokenValue)){
            // 输出错误信息
            logger.error("token interceptor.............token is null");
            httpServletResponse.getWriter().write(JSON.toJSONString(R.error(Const.ERROR_PARAM, "token is null")));
            return false;
        }
        // 解密token
        String decodeToken = null;
        try {
            decodeToken = TokenUtil.decodeToken(tokenValue);
        } catch (Exception e) {
            logger.error("token interceptor........token decode failed");
            httpServletResponse.getWriter().write(JSON.toJSONString(R.error(Const.ERROR_PARAM, "token decode failed")));
            return false;
        }
        String[] message = decodeToken.split(Const.HASH_TAG);
        if (message == null || message.length != 2){
            // 输出错误信息
            logger.error("token interceptor.............token decode failed");
            httpServletResponse.getWriter().write(JSON.toJSONString(R.error(Const.ERROR_PARAM, "token decode failed")));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
