package com.hotstrip.publish.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class BaseBean {
    // 创建时间
    private Date createTime;
    // 修改时间
    private Date updateTime;
    // 创建人
    private Long createUser;
    // 修改人
    private Long updateUser;
    // 状态
    private Integer status;
    // userId
    private Long userId;
}
