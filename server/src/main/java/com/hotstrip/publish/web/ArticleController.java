package com.hotstrip.publish.web;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.annotation.NotNullParam;
import com.hotstrip.publish.common.annotation.RequireToken;
import com.hotstrip.publish.model.Article;
import com.hotstrip.publish.model.R;
import com.hotstrip.publish.service.ArticleService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleController extends SuperController {

    @Resource
    private ArticleService articleService;

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param title
     * @param editStatus
     * @return
     */
    @RequireToken
    @PostMapping(value = "/web/article/list")
    public Object list(@RequestParam(defaultValue = "1") Integer pageNo,
                       @RequestParam(defaultValue = "20") Integer pageSize,
                       String title,
                       Integer editStatus) {
        Article info = Article.builder()
                .title(title)
                .editStatus(editStatus)
                .build();
        Page<Article> list = articleService.getArticles(new RowBounds((pageNo - 1) * pageSize, pageSize), info);
        return R.ok("success").put("data", list).put("totalCount", list.getTotal());
    }

    /**
     * 编辑
     * @param articleId
     * @param title
     * @param coverImage
     * @param content
     * @param editStatus
     * @return
     */
    @RequireToken
    @PostMapping(value = "/web/article/edit")
    public Object edit(Long articleId,
                       String title,
                       String coverImage,
                       String content,
                       Integer editStatus) {
        Article info = Article.builder()
                .articleId(articleId)
                .title(title)
                .coverImage(coverImage)
                .content(content)
                .editStatus(editStatus)
                .build();
        // 判断新增还是修改
        if (null == articleId) {
            articleService.insert(info);
        } else {
            articleService.update(info);
        }
        return R.ok("success");
    }

    /**
     * 删除
     * @param articleId
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"articleId"})
    @PostMapping(value = "/web/article/delete")
    public Object delete(Long articleId) {
        // 删除文稿
        articleService.deleteByArticleId(articleId);
        return R.ok("success");
    }
}
