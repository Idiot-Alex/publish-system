package com.hotstrip.publish.service.impl;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.util.IdGen;
import com.hotstrip.publish.dao.DirectoryDao;
import com.hotstrip.publish.model.Directory;
import com.hotstrip.publish.service.DirectoryService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    @Resource
    private DirectoryDao directoryDao;

    @Override
    public void insert(Directory info) {
        if (null == info.getDirectoryId())
            info.setDirectoryId(IdGen.get().nextId());
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
}
