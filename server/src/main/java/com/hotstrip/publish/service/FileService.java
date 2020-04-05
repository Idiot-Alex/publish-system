package com.hotstrip.publish.service;

import com.github.pagehelper.Page;
import com.hotstrip.publish.model.FileInfo;
import org.apache.ibatis.session.RowBounds;

public interface FileService {
    // 新增
    void insert(FileInfo info);

    // 修改
    void update(FileInfo info);

    // 删除
    void delete(Long fileId);

    // 分页查询
    Page<FileInfo> getFiles(RowBounds rowBounds, FileInfo info);
}
