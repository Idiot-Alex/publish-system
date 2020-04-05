package com.hotstrip.publish.service;

import com.github.pagehelper.Page;
import com.hotstrip.publish.model.Article;
import org.apache.ibatis.session.RowBounds;

public interface ArticleService {
    // 新增
    void insert(Article info);

    // 修改
    void update(Article info);

    // 删除
    void deleteByArticleId(Long articleId);

    // 分页查询
    Page<Article> getArticles(RowBounds rowBounds, Article info);

}
