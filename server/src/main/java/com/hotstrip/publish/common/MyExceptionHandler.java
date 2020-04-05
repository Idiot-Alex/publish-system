package com.hotstrip.publish.common;

import com.hotstrip.publish.model.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class MyExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("exception url: [{}]", req.getRequestURL().toString());
        logger.error("error......message: [{}]......cause: [{}]", e.getMessage(), e.getCause());
        e.printStackTrace();
        return R.error(500, "server error");
    }
}
