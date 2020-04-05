package com.hotstrip.publish.service.impl;

import com.hotstrip.publish.dao.UserAgentDao;
import com.hotstrip.publish.service.UserAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserAgentServiceImpl implements UserAgentService {
    private static Logger logger = LoggerFactory.getLogger(UserAgentServiceImpl.class);

    @Resource
    private UserAgentDao userAgentDao;

    @Override
    public void deleteByUserId(Long userId) {
        if (userAgentDao.deleteByUserId(userId) < 1) {
            logger.error("delete userAgent by userId failed...userId: [{}]", userId);
        }
    }
}
