package com.hotstrip.publish.web;

import com.hotstrip.publish.common.util.Const;
import com.hotstrip.publish.common.util.TokenUtil;
import com.hotstrip.publish.model.User;
import com.hotstrip.publish.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class SuperController {
    protected static Logger logger = LoggerFactory.getLogger(SuperController.class);

    @Resource
    private UserService userService;

    //获取用户
    protected User getUser(Long userId){
        if (userId == null)
            return null;
        User user = userService.getUserByUserId(userId);
        return user;
    }

    // 根据token获取用户信息编号
    protected Long getUserIdByToken(String token){
        Long userId = null;
        try {
            String decodeToken = TokenUtil.decodeToken(token);
            String[] message = decodeToken.split(Const.HASH_TAG);
            userId = Long.valueOf(message[0]);
        } catch (Exception e) {
            logger.error("decode token failed..........caused: [{}]......msg: [{}]", e.getCause(), e.getMessage());
            //抛出异常比较合适   抛出自定义异常   异常返回json 信息
        }
        return userId;
    }
}
