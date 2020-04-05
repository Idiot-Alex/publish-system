package com.hotstrip.publish.service.impl;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.util.IdGen;
import com.hotstrip.publish.dao.UserDao;
import com.hotstrip.publish.model.User;
import com.hotstrip.publish.service.UserService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public void insert(User info) {
        if (null == info.getUserId())
            info.setUserId(IdGen.get().nextId());
        if (null == info.getCreateTime())
            info.setCreateTime(new Date());
        if (userDao.insert(info) < 1) {
            throw new RuntimeException("insert user data failed");
        }
    }

    @Override
    public void update(User info) {
        if (null == info.getUpdateTime())
            info.setUpdateTime(new Date());
        if (userDao.update(info) < 1) {
            throw new RuntimeException("update user data failed");
        }
    }

    @Override
    public void deleteByUserId(Long userId) {
        if (userDao.deleteByUserId(userId) < 1) {
            throw new RuntimeException("delete user data failed");
        }
    }

    @Override
    public Page<User> getUsers(RowBounds rowBounds, User info) {
        return userDao.getUsers(rowBounds, info);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    @Override
    public User getUserByUserId(Long userId) {
        return userDao.getUserByUserId(userId);
    }
}
