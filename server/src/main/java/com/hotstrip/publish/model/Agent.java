package com.hotstrip.publish.model;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Agent extends BaseBean {
    // 主键
    private Long agentId;
    // 终端名称
    private String agentName;
    // 终端 Code
    private String agentCode;
    // 心跳频率 单位：秒
    private Integer heartbeatFrequency;
    // 上次心跳时间
    private Date lastHeartbeatTime;
    // 离线天数
    private Integer onlineStatus;
}
