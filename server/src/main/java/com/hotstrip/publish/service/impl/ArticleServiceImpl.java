package com.hotstrip.publish.service.impl;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.util.IdGen;
import com.hotstrip.publish.dao.ArticleDao;
import com.hotstrip.publish.model.Article;
import com.hotstrip.publish.service.ArticleService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleDao articleDao;

    @Override
    public void insert(Article info) {
        if (null == info.getArticleId())
            info.setArticleId(IdGen.get().nextId());
        if (null == info.getCreateTime())
            info.setCreateTime(new Date());
        if (articleDao.insert(info) < 1) {
            throw new RuntimeException("insert article data failed");
        }
    }

    @Override
    public void update(Article info) {
        if (null == info.getUpdateTime())
            info.setUpdateTime(new Date());
        if (articleDao.update(info) < 1) {
            throw new RuntimeException("update article data failed");
        }
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        if (articleDao.deleteByArticleId(articleId) < 1) {
            throw new RuntimeException("delete article data failed");
        }
    }

    @Override
    public Page<Article> getArticles(RowBounds rowBounds, Article info) {
        return articleDao.getArticles(rowBounds, info);
    }
}
