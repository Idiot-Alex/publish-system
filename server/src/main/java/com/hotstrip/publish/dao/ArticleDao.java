package com.hotstrip.publish.dao;

import com.github.pagehelper.Page;
import com.hotstrip.publish.model.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface ArticleDao {
    // 新增
    int insert(Article info);

    // 修改
    int update(Article info);

    // 删除
    @Delete("delete from t_article where article_id = #{articleId}")
    int deleteByArticleId(@Param("articleId") Long articleId);

    // 分页查询
    Page<Article> getArticles(RowBounds rowBounds, Article info);
}
