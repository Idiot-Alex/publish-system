package com.hotstrip.publish.service.impl;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.util.EncodeMD5;
import com.hotstrip.publish.common.util.IdGen;
import com.hotstrip.publish.dao.DirectoryDao;
import com.hotstrip.publish.model.Directory;
import com.hotstrip.publish.service.DirectoryService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    @Resource
    private DirectoryDao directoryDao;

    @Override
    public void insert(Directory info) {
        if (null == info.getDirectoryId())
            info.setDirectoryId(IdGen.get().nextId());
        if(null == info.getParentDirectoryId())
            info.setRootFlag(1);
        else
            info.setRootFlag(0);
        if (null == info.getPathCode() || info.getPathCode().length() < 1)
            info.setPathCode(EncodeMD5.getMD5Code(info.getDirectoryName()));
        if (null == info.getCreateTime())
            info.setCreateTime(new Date());
        if (directoryDao.insert(info) < 1) {
            throw new RuntimeException("insert directory data failed");
        }
    }

    @Override
    public void update(Directory info) {
        if (null == info.getUpdateTime())
            info.setUpdateTime(new Date());
        if (directoryDao.update(info) < 1) {
            throw new RuntimeException("update directory data failed");
        }
    }

    @Override
    public void deleteByDirectoryId(Long directoryId) {
        if (directoryDao.deleteByDirectoryId(directoryId) < 1) {
            throw new RuntimeException("delete directory data failed");
        }
    }

    @Override
    public Page<Directory> getDirectories(RowBounds rowBounds, Directory info) {
        return directoryDao.getDirectories(rowBounds, info);
    }

    @Override
    public Directory getDirectoryByDirectoryId(Long directoryId) {
        return directoryDao.getDirectoryByDirectoryId(directoryId);
    }

    @Override
    public String getDirectoryPathByDirectoryId(Long directoryId) {
        // 根据目录编号获取目录完整路径  递归
        StringBuffer buffer = new StringBuffer();
        // 获取目录信息
        Directory directory = directoryDao.getDirectoryByDirectoryId(directoryId);
        if(directory != null){
            buffer.insert(0, directory.getPathCode());
            // 如果该目录是子目录
            if(directory.getParentDirectoryId() != null){
                buffer.insert(0, File.separator);
                buffer.insert(0, getDirectoryPathByDirectoryId(directory.getParentDirectoryId()));
            }
        }
        return buffer.toString();
    }
}
