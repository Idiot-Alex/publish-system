package com.hotstrip.publish.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class UserAgent extends BaseBean {
    // 主键
    private Long userAgentId;
    // 用户 ID
    private Long userId;
    // 终端 ID
    private Long agentId;
}
