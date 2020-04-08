package com.hotstrip.publish.service;

import com.hotstrip.publish.model.UserAgent;

public interface UserAgentService {
    // 新增
    void insert(UserAgent info);

    // 根据 userId 删除
    void deleteByUserId(Long userId);
}
