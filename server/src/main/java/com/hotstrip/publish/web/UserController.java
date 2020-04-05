package com.hotstrip.publish.web;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.annotation.NotNullParam;
import com.hotstrip.publish.common.annotation.RequireToken;
import com.hotstrip.publish.common.util.Const;
import com.hotstrip.publish.common.util.EncodeMD5;
import com.hotstrip.publish.common.util.TokenUtil;
import com.hotstrip.publish.model.R;
import com.hotstrip.publish.model.User;
import com.hotstrip.publish.service.UserAgentService;
import com.hotstrip.publish.service.UserService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
public class UserController extends SuperController {

    @Resource
    private UserService userService;
    @Resource
    private UserAgentService userAgentService;

    /**
     * 登录
     * @param userName
     * @param userPassword
     * @return
     */
    @NotNullParam(params = {"userName", "userPassword"})
    @PostMapping(value = "/web/user/login")
    public Object login(String userName,
                        String userPassword) {
        // 查询用户
        User user = userService.getUserByUserName(userName);
        if (null == user) {
            logger.error("invalid param userName: [{}]", userName);
            return R.error(Const.ERROR_PARAM, "invalid param userName");
        }
        // 比较密码
        if (!user.getUserPassword().equals(EncodeMD5.getMD5Code(userPassword))) {
            logger.error("invalid param userPassword: [{}]", userPassword);
            return R.error(Const.ERROR_PARAM, "invalid param userPassword");
        }
        // 生成 token 返回用户信息
        String token = TokenUtil.encodeToken(user.getUserId() + Const.HASH_TAG + System.currentTimeMillis());
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", String.valueOf(user.getUserId()));
        map.put("userName", user.getUserName());
        map.put(Const.TOKEN, token);
        return R.ok("success").put("data", map);
    }

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param userName
     * @return
     */
    @RequireToken
    @PostMapping(value = "/web/user/list")
    public Object list(@RequestParam(defaultValue = "1") Integer pageNo,
                       @RequestParam(defaultValue = "20") Integer pageSize,
                       String userName) {
        User info = User.builder()
                .userName(userName)
                .build();
        Page<User> list = userService.getUsers(new RowBounds((pageNo - 1) * pageSize, pageSize), info);
        return R.ok("success").put("data", list).put("totalCount", list.getTotal());
    }

    /**
     * 编辑用户
     * @param userId
     * @param userName
     * @param userPassword
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"userName"})
    @PostMapping(value = "/web/user/edit")
    public Object edit(Long userId,
                       String userName,
                       String userPassword) {
        User info = User.builder()
                .userId(userId)
                .userName(userName)
                .build();
        if (!StringUtils.isEmpty(userPassword)) {
            info.setUserPassword(EncodeMD5.getMD5Code(userPassword));
        }
        // 查询用户是否存在
        User user = userService.getUserByUserName(userName);
        if (null != user)
            return R.error(Const.ERROR_PARAM, "user already exist");
        if (null == userId) {
            // 新增
            userService.insert(info);
        } else {
            // 修改
            userService.update(info);
        }
        return R.ok("success");
    }

    /**
     * 删除
     * @param userId
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"userId"})
    @PostMapping(value = "/web/user/delete")
    public Object delete(Long userId) {
        // 先删除 userAgent 数据
        userAgentService.deleteByUserId(userId);
        // 删除用户
        userService.deleteByUserId(userId);
        return R.ok("success");
    }
}
