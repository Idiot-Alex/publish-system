package com.hotstrip.publish.dao;

import com.github.pagehelper.Page;
import com.hotstrip.publish.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface UserDao {
    // 新增
    int insert(User info);

    // 修改
    int update(User info);

    // 删除
    @Delete("delete from t_user where user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    // 分页查询
    Page<User> getUsers(RowBounds rowBounds, User info);

    // 根据 userName 查询
    @Select("select * from t_user where user_name = #{userName}")
    User getUserByUserName(@Param("userName") String userName);

    // 根据 userId 查询
    @Select("select * from t_user where user_id = #{userId}")
    User getUserByUserId(@Param("userId") Long userId);
}
