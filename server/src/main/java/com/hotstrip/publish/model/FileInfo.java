package com.hotstrip.publish.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class FileInfo extends BaseBean {
    // 主键
    private Long fileId;
    // 目录 ID
    private Long directoryId;
    // 文件名称
    private String fileName;
    // 源文件名称
    private String oldName;
    // 文件类型 1：图片 2：视频
    private Integer fileType;
    // 文件访问路径
    private String filePath;
    // 文件大小
    private Long fileSize;
}
