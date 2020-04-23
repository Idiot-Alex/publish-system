package com.hotstrip.publish.service.impl;

import com.hotstrip.publish.common.util.IdGen;
import com.hotstrip.publish.dao.UserAgentDao;
import com.hotstrip.publish.model.UserAgent;
import com.hotstrip.publish.service.UserAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserAgentServiceImpl implements UserAgentService {
    private static Logger logger = LoggerFactory.getLogger(UserAgentServiceImpl.class);

    @Resource
    private UserAgentDao userAgentDao;

    @Override
    public void insert(UserAgent info) {
        if (null == info.getUserAgentId())
            info.setUserAgentId(IdGen.get().nextId());
        if (null == info.getCreateTime())
            info.setCreateTime(new Date());
        if (userAgentDao.insert(info) < 1) {
            throw new RuntimeException("insert userAgent data failed");
        }
    }

    @Override
    public void deleteByUserId(Long userId) {
        if (userAgentDao.deleteByUserId(userId) < 1) {
            logger.error("delete userAgent by userId failed...userId: [{}]", userId);
        }
    }

    @Override
    public void deleteByAgentId(Long agentId) {
        if (userAgentDao.deleteByAgentId(agentId) < 1) {
            logger.error("delete userAgent by userId failed...agentId: [{}]", agentId);
        }
    }
}
