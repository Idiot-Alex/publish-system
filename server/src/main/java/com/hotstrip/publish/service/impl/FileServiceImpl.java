package com.hotstrip.publish.service.impl;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.util.IdGen;
import com.hotstrip.publish.dao.FileDao;
import com.hotstrip.publish.model.FileInfo;
import com.hotstrip.publish.service.FileService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileDao fileDao;

    @Override
    public void insert(FileInfo info) {
        if (null == info.getFileId())
            info.setFileId(IdGen.get().nextId());
        if (null == info.getCreateTime())
            info.setCreateTime(new Date());
        if (fileDao.insert(info) < 1) {
            throw new RuntimeException("insert file data failed");
        }
    }

    @Override
    public void update(FileInfo info) {
        if (null == info.getUpdateTime())
            info.setUpdateTime(new Date());
        if (fileDao.update(info) < 1) {
            throw new RuntimeException("update file data failed");
        }
    }

    @Override
    public void delete(Long fileId) {
        if (fileDao.deleteByFileId(fileId) < 1) {
            throw new RuntimeException("delete file data failed");
        }
    }

    @Override
    public Page<FileInfo> getFiles(RowBounds rowBounds, FileInfo info) {
        return fileDao.getFiles(rowBounds, info);
    }
}
