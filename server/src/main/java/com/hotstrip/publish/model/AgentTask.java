package com.hotstrip.publish.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class AgentTask extends BaseBean {
    // 主键
    private Long taskId;
    // 终端 ID
    private Long agentId;
    // 命令
    private Integer code;
}
