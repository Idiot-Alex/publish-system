package com.hotstrip.publish.service.impl;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.util.IdGen;
import com.hotstrip.publish.dao.AgentDao;
import com.hotstrip.publish.model.Agent;
import com.hotstrip.publish.service.AgentService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class AgentServiceImpl implements AgentService {

    @Resource
    private AgentDao agentDao;

    @Override
    public void insert(Agent info) {
        if (null == info.getAgentId())
            info.setAgentId(IdGen.get().nextId());
        if (null == info.getCreateTime())
            info.setCreateTime(new Date());
        if (agentDao.insert(info) < 1) {
            throw new RuntimeException("insert agent data failed");
        }
    }

    @Override
    public void update(Agent info) {
        if (null == info.getUpdateTime())
            info.setUpdateTime(new Date());
        if (agentDao.update(info) < 1) {
            throw new RuntimeException("update agent data failed");
        }
    }

    @Override
    public void deleteByAgentId(Long agentId) {
        if (agentDao.deleteByAgentId(agentId) < 1) {
            throw new RuntimeException("delete agent data failed");
        }
    }

    @Override
    public Page<Agent> getAgents(RowBounds rowBounds, Agent info) {
        return agentDao.getAgents(rowBounds, info);
    }

    @Override
    public Agent getAgentByAgentCode(String agentCode) {
        return agentDao.getAgentByAgentCode(agentCode);
    }
}
