package com.hotstrip.publish.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AgentPlaylistDao {
    // 批量新增

    // 批量删除
    @Delete("delete from t_agent_playlist where agent_id = #{agentId}")
    int deleteByAgentId(@Param("agentId") Long agentId);
}
