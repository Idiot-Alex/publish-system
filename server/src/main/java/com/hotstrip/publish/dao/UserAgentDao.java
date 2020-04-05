package com.hotstrip.publish.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAgentDao {
    // 批量新增

    // 批量删除
    @Delete("delete from t_user_agent where user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);
}
