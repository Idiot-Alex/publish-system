package com.hotstrip.publish.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Article extends BaseBean {
    // 文稿 ID
    private Long articleId;
    // 标题
    private String title;
    // 封面图片
    private String coverImage;
    // 富文本内容
    private String content;
    // 文稿状态 0：未完成 1：已完成 2：已撤销
    private Integer editStatus;
}
