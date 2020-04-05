package com.hotstrip.publish.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Directory extends BaseBean {
    // 主键
    private Long directoryId;
    // 目录名称
    private String directoryName;
    // 父级目录 ID
    private Long parentDirectoryId;
    // 是否根节点
    private Integer rootFlag;
    // 目录编码
    private String pathCode;
}
