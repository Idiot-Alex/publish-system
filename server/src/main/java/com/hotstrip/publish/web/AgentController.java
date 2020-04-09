package com.hotstrip.publish.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.hotstrip.publish.common.annotation.NotNullParam;
import com.hotstrip.publish.common.annotation.RequireToken;
import com.hotstrip.publish.common.util.Const;
import com.hotstrip.publish.common.util.IdGen;
import com.hotstrip.publish.common.util.JsonUtil;
import com.hotstrip.publish.model.Agent;
import com.hotstrip.publish.model.R;
import com.hotstrip.publish.model.User;
import com.hotstrip.publish.model.UserAgent;
import com.hotstrip.publish.service.AgentPlaylistService;
import com.hotstrip.publish.service.AgentService;
import com.hotstrip.publish.service.UserAgentService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AgentController extends SuperController {

    @Resource
    private AgentService agentService;
    @Resource
    private UserAgentService userAgentService;
    @Resource
    private AgentPlaylistService agentPlaylistService;

    /**
     * 分页查询 全部终端
     * @param pageNo
     * @param pageSize
     * @param agentName
     * @param agentCode
     * @return
     */
    @RequireToken
    @PostMapping(value = "/web/agent/list")
    public Object list(@RequestParam(defaultValue = "1") Integer pageNo,
                       @RequestParam(defaultValue = "20") Integer pageSize,
                       String agentName,
                       String agentCode) {
        Agent info = Agent.builder()
                .agentName(agentName)
                .agentCode(agentCode)
                .build();
        Page<User> list = agentService.getAgents(new RowBounds((pageNo - 1) * pageSize, pageSize), info);
        JSONArray objects = JsonUtil.parseArray(list);
        return R.ok("success").put("data", objects).put("totalCount", list.getTotal());
    }

    /**
     * 分页查询 用户拥有的终端
     * @param pageNo
     * @param pageSize
     * @param agentName
     * @param agentCode
     * @param userId
     * @return
     */
    @RequireToken
    @PostMapping(value = "/web/agent/user-list")
    public Object userList(@RequestHeader(value = Const.TOKEN) String token,
                           @RequestParam(defaultValue = "1") Integer pageNo,
                           @RequestParam(defaultValue = "20") Integer pageSize,
                           String agentName,
                           String agentCode,
                           Long userId) {
        // 获取用户信息
        if (null == userId) {
            User user = getUser(getUserIdByToken(token));
            if (user == null) {
                return R.error(Const.ERROR_PARAM, "invalid user");
            }
            userId = user.getUserId();
        }
        Agent info = Agent.builder()
                .agentName(agentName)
                .agentCode(agentCode)
                .build();
        info.setUserId(userId);
        Page<User> list = agentService.getAgents(new RowBounds((pageNo - 1) * pageSize, pageSize), info);
        JSONArray objects = JsonUtil.parseArray(list);
        return R.ok("success").put("data", objects).put("totalCount", list.getTotal());
    }

    /**
     * 编辑终端
     * @param agentId
     * @param agentName
     * @param agentCode
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"agentName", "agentCode"})
    @PostMapping(value = "/web/agent/edit")
    public Object edit(@RequestHeader(value = Const.TOKEN) String token,
                       Long agentId,
                       String agentName,
                       String agentCode) {
        // 获取用户信息
        User user = getUser(getUserIdByToken(token));
        if(user == null){
            return R.error(Const.ERROR_PARAM, "invalid user");
        }
        Agent info = Agent.builder()
                .agentId(agentId)
                .agentName(agentName)
                .agentCode(agentCode)
                .build();
        // 查询终端是否存在
        Agent agent = agentService.getAgentByAgentCode(agentCode);
        if (null != agent && agent.getAgentId() != agentId)
            return R.error(Const.ERROR_PARAM, "agent already exist");
        if (null == agentId) {
            // 新增
            agentId = IdGen.get().nextId();
            UserAgent userAgent = UserAgent.builder()
                    .agentId(agentId)
                    .userId(user.getUserId())
                    .build();
            info.setAgentId(agentId);
            agentService.insert(info);
            userAgentService.insert(userAgent);
        } else {
            // 修改
            agentService.update(info);
        }
        return R.ok("success");
    }

    /**
     * 删除
     * @param agentId
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"agentId"})
    @PostMapping(value = "/web/agent/delete")
    public Object delete(Long agentId) {
        // 先删除 agentPlaylist 数据
        agentPlaylistService.deleteByAgentId(agentId);
        // 删除 userAgent 数据
        userAgentService.deleteByAgentId(agentId);
        // 删除终端
        agentService.deleteByAgentId(agentId);
        return R.ok("success");
    }

    /**
     * 分配用户终端
     * @param userId
     * @param agentIds
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"userId", "agentIds"})
    @PostMapping(value = "/web/agent/grant-user-agents")
    public Object grantUserAgents(Long userId,
                                  String agentIds) {
        List<Long> ids = JSON.parseArray(agentIds, Long.class);
        if (ids.isEmpty()) {
            return R.error(Const.ERROR_PARAM, "invalid param agentIds");
        }
        // 删除原来的数据
        userAgentService.deleteByUserId(userId);
        // 添加数据
        ids.forEach(agentId -> {
            UserAgent info = UserAgent.builder()
                    .userId(userId)
                    .agentId(agentId)
                    .build();
            userAgentService.insert(info);
        });
        return R.ok("success");
    }
}
