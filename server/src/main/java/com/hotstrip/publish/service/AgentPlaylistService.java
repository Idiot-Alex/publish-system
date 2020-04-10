package com.hotstrip.publish.service;

import com.hotstrip.publish.model.AgentPlaylist;

import java.util.List;

public interface AgentPlaylistService {
    // 新增
    void insert(AgentPlaylist info);

    // 修改
    void update(AgentPlaylist info);

    // 根据 agentId 查询
    List<AgentPlaylist> getAgentPlaylistByAgentId(Long agentId);

    // 根据 agentId 删除
    void deleteByAgentId(Long agentId);

    // 删除
    void deleteByListId(Long listId);

}
