package com.hotstrip.publish.dao;

import com.github.pagehelper.Page;
import com.hotstrip.publish.model.FileInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface FileDao {
    // 新增
    int insert(FileInfo info);

    // 修改
    int update(FileInfo info);

    // 删除
    @Delete("delete from t_file where file_id = #{fileId}")
    int deleteByFileId(@Param("fileId") Long fileId);

    // 分页查询
    Page<FileInfo> getFiles(RowBounds rowBounds, FileInfo info);
}
