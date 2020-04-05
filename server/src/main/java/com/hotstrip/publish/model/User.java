package com.hotstrip.publish.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class User extends BaseBean {
    // 主键
    private Long userId;
    // 用户名称
    private String userName;
    // 用户密码
    private String userPassword;
}
