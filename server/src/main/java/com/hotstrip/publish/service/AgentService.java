package com.hotstrip.publish.service;

import com.github.pagehelper.Page;
import com.hotstrip.publish.model.Agent;
import com.hotstrip.publish.model.User;
import org.apache.ibatis.session.RowBounds;

public interface AgentService {
    // 新增
    void insert(Agent info);

    // 修改
    void update(Agent info);

    // 删除
    void deleteByAgentId(Long agentId);

    // 分页查询
    Page<Agent> getAgents(RowBounds rowBounds, Agent info);

    // 根据 agentCode 查询
    Agent getAgentByAgentCode(String agentCode);

}
