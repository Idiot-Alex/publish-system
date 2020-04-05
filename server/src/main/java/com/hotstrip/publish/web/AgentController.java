package com.hotstrip.publish.web;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.annotation.NotNullParam;
import com.hotstrip.publish.common.annotation.RequireToken;
import com.hotstrip.publish.common.util.Const;
import com.hotstrip.publish.model.Agent;
import com.hotstrip.publish.model.R;
import com.hotstrip.publish.model.User;
import com.hotstrip.publish.service.AgentPlaylistService;
import com.hotstrip.publish.service.AgentService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AgentController extends SuperController {

    @Resource
    private AgentService agentService;
    @Resource
    private AgentPlaylistService agentPlaylistService;

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param agentName
     * @param agentCode
     * @return
     */
    @RequireToken
    @PostMapping(value = "/web/agent/list")
    public Object list(@RequestHeader(value = Const.TOKEN) String token,
                       @RequestParam(defaultValue = "1") Integer pageNo,
                       @RequestParam(defaultValue = "20") Integer pageSize,
                       String agentName,
                       String agentCode) {
        // 获取用户信息
        User user = getUser(getUserIdByToken(token));
        if(user == null){
            return R.error(Const.ERROR_PARAM, "invalid user");
        }
        Agent info = Agent.builder()
                .agentName(agentName)
                .agentCode(agentCode)
                .build();
        info.setUserId(user.getUserId());
        Page<User> list = agentService.getAgents(new RowBounds((pageNo - 1) * pageSize, pageSize), info);
        return R.ok("success").put("data", list).put("totalCount", list.getTotal());
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
    public Object edit(Long agentId,
                       String agentName,
                       String agentCode) {
        Agent info = Agent.builder()
                .agentId(agentId)
                .agentName(agentName)
                .agentCode(agentCode)
                .build();
        // 查询终端是否存在
        Agent agent = agentService.getAgentByAgentCode(agentCode);
        if (null != agent)
            return R.error(Const.ERROR_PARAM, "agent already exist");
        if (null == agentId) {
            // 新增
            agentService.insert(info);
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
        // 删除终端
        agentService.deleteByAgentId(agentId);
        return R.ok("success");
    }
}
