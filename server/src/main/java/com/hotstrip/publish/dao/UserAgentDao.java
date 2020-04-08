package com.hotstrip.publish.dao;

import com.hotstrip.publish.model.UserAgent;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAgentDao {
    // 新增
    int insert(UserAgent info);

    // 根据 userId 删除
    @Delete("delete from t_user_agent where user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    // 根据 agentId 删除
    @Delete("delete from t_user_agent where agent_id = #{agentId}")
    int deleteByAgentId(@Param("agentId") Long agentId);
}
