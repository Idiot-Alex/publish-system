package com.hotstrip.publish.service;

import com.github.pagehelper.Page;
import com.hotstrip.publish.model.User;
import org.apache.ibatis.session.RowBounds;

public interface UserService {
    // 新增
    void insert(User info);

    // 修改
    void update(User info);

    // 删除
    void deleteByUserId(Long userId);

    // 分页查询
    Page<User> getUsers(RowBounds rowBounds, User info);

    // 根据 userName 查询
    User getUserByUserName(String userName);

    // 根据 userId 查询
    User getUserByUserId(Long userId);
}
