package com.hotstrip.publish.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgentTaskDao {
    // 新增

    // 修改

    // 删除
    @Delete("delete from t_agent_task where task_id = #{taskId}")
    int deleteByTaskId(Long taskId);
}
