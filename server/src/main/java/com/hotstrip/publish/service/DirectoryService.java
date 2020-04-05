package com.hotstrip.publish.service;

import com.github.pagehelper.Page;
import com.hotstrip.publish.model.Directory;
import org.apache.ibatis.session.RowBounds;

public interface DirectoryService {
    // 新增
    void insert(Directory info);

    // 修改
    void update(Directory info);

    // 删除
    void deleteByDirectoryId(Long directoryId);

    // 分页查询
    Page<Directory> getDirectories(RowBounds rowBounds, Directory info);
}
