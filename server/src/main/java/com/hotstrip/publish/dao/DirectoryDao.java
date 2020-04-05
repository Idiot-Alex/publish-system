package com.hotstrip.publish.dao;

import com.github.pagehelper.Page;
import com.hotstrip.publish.model.Directory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface DirectoryDao {
    // 新增
    int insert(Directory info);

    // 修改
    int update(Directory info);

    // 删除
    @Delete("delete from t_directory where directory_id = #{directoryId}")
    int deleteByDirectoryId(@Param("directoryId") Long directoryId);

    // 分页查询
    Page<Directory> getDirectories(RowBounds rowBounds, Directory info);
}
