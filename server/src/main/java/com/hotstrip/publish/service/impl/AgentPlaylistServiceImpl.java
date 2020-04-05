package com.hotstrip.publish.service.impl;

import com.hotstrip.publish.dao.AgentPlaylistDao;
import com.hotstrip.publish.service.AgentPlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AgentPlaylistServiceImpl implements AgentPlaylistService {
    private static Logger logger = LoggerFactory.getLogger(AgentPlaylistServiceImpl.class);

    @Resource
    private AgentPlaylistDao agentPlaylistDao;

    @Override
    public void deleteByAgentId(Long agentId) {
        if (agentPlaylistDao.deleteByAgentId(agentId) < 1) {
            logger.error("delete agentPlaylist by agentId failed...agentId: [{}]", agentId);
        }
    }
}
