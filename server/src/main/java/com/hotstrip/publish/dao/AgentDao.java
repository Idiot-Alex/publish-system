package com.hotstrip.publish.dao;

import com.github.pagehelper.Page;
import com.hotstrip.publish.model.Agent;
import com.hotstrip.publish.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface AgentDao {
    // 新增
    int insert(Agent info);

    // 修改
    int update(Agent info);

    // 删除
    @Delete("delete from t_agent where agent_id = #{agentId}")
    int deleteByAgentId(@Param("agentId") Long agentId);

    // 分页查询
    Page<User> getAgents(RowBounds rowBounds, Agent info);

    // 根据 agentCode 查询
    @Select("select * from t_agent where agent_code = #{agentCode}")
    Agent getAgentByAgentCode(@Param("agentCode") String agentCode);
}
