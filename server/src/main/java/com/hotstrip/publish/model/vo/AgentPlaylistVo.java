package com.hotstrip.publish.model.vo;

import com.hotstrip.publish.model.AgentPlaylist;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class AgentPlaylistVo extends AgentPlaylist {
    // 终端名称
    private String agentName;
    // 终端编号
    private String agentCode;
    // 文章标题
    private String title;
    // 文章封面图
    private String coverImage;
    // 文章内容
    private String content;
}
