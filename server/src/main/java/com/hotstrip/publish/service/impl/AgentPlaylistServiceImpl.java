package com.hotstrip.publish.service.impl;

import com.hotstrip.publish.common.util.IdGen;
import com.hotstrip.publish.dao.AgentPlaylistDao;
import com.hotstrip.publish.model.AgentPlaylist;
import com.hotstrip.publish.service.AgentPlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AgentPlaylistServiceImpl implements AgentPlaylistService {
    private static Logger logger = LoggerFactory.getLogger(AgentPlaylistServiceImpl.class);

    @Resource
    private AgentPlaylistDao agentPlaylistDao;

    @Override
    public void insert(AgentPlaylist info) {
        if (null == info.getListId())
            info.setListId(IdGen.get().nextId());
        if (null == info.getCreateTime())
            info.setCreateTime(new Date());
        if (null == info.getSerialNumber())
            info.setSerialNumber(0);
        if (agentPlaylistDao.insert(info) < 1) {
            throw new RuntimeException("insert agentPlaylist data failed");
        }
    }

    @Override
    public void update(AgentPlaylist info) {
        if (null == info.getUpdateTime())
            info.setUpdateTime(new Date());
        if (agentPlaylistDao.update(info) < 1) {
            throw new RuntimeException("update agentPlaylist data failed");
        }
    }

    @Override
    public List<AgentPlaylist> getAgentPlaylistByAgentId(Long agentId) {
        return agentPlaylistDao.getAgentPlaylistByAgentId(agentId);
    }

    @Override
    public void deleteByAgentId(Long agentId) {
        if (agentPlaylistDao.deleteByAgentId(agentId) < 1) {
            logger.error("delete agentPlaylist by agentId failed...agentId: [{}]", agentId);
        }
    }

    @Override
    public void deleteByListId(Long listId) {
        if (agentPlaylistDao.deleteByListId(listId) < 1) {
            logger.error("delete agentPlaylist by listId failed...agentId: [{}]", listId);
        }
    }
}
