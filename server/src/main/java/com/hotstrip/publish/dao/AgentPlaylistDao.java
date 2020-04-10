package com.hotstrip.publish.dao;

import com.hotstrip.publish.model.AgentPlaylist;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AgentPlaylistDao {
    // 新增
    int insert(AgentPlaylist info);

    // 修改
    int update(AgentPlaylist info);

    // 根据 agentId 查询
    List<AgentPlaylist> getAgentPlaylistByAgentId(@Param("agentId") Long agentId);

    // 批量删除
    @Delete("delete from t_agent_playlist where agent_id = #{agentId}")
    int deleteByAgentId(@Param("agentId") Long agentId);

    // 删除
    @Delete("delete from t_agent_playlist where list_id = #{listId}")
    int deleteByListId(Long listId);

}
