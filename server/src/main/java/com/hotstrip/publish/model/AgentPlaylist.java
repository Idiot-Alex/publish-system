package com.hotstrip.publish.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class AgentPlaylist extends BaseBean {
    // 主键
    private Long listId;
    // 终端 ID
    private Long agentId;
    // 文稿 ID
    private Long articleId;
    // 序号
    private Integer serialNumber;
}
