package com.hotstrip.publish.web;

import com.alibaba.fastjson.JSONArray;
import com.hotstrip.publish.common.annotation.NotNullParam;
import com.hotstrip.publish.common.annotation.RequireToken;
import com.hotstrip.publish.common.util.JsonUtil;
import com.hotstrip.publish.model.AgentPlaylist;
import com.hotstrip.publish.model.R;
import com.hotstrip.publish.service.AgentPlaylistService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AgentPlayListController extends SuperController {

    @Resource
    private AgentPlaylistService agentPlaylistService;

    /**
     * 根据 agentId 查询
     * @param agentId
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"agentId"})
    @PostMapping(value = "/web/agent-playlist/list")
    public Object list(Long agentId) {
        List<AgentPlaylist> list = agentPlaylistService.getAgentPlaylistByAgentId(agentId);
        JSONArray objects = JsonUtil.parseArray(list);
        return R.ok("success").put("data", objects);
    }

    /**
     * 新增
     * @param agentId
     * @param articleId
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"agentId", "articleId"})
    @PostMapping(value = "/web/agent-playlist/add")
    public Object add(Long agentId,
                      Long articleId) {
        AgentPlaylist info = AgentPlaylist.builder()
                .agentId(agentId)
                .articleId(articleId)
                .build();
        agentPlaylistService.insert(info);
        return R.ok("success");
    }

    /**
     * 修改顺序
     * @param listId
     * @param serialNumber
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"listId", "serialNumber"})
    @PostMapping(value = "/web/agent-playlist/edit")
    public Object edit(Long listId,
                       Integer serialNumber) {
        AgentPlaylist info = AgentPlaylist.builder()
                .listId(listId)
                .serialNumber(serialNumber)
                .build();
        agentPlaylistService.update(info);
        return R.ok("success");
    }

    /**
     * 删除
     * @param listId
     * @return
     */
    @RequireToken
    @NotNullParam(params = {"listId"})
    @PostMapping(value = "/web/agent-playlist/delete")
    public Object delete(Long listId) {
        // 删除
        agentPlaylistService.deleteByListId(listId);
        return R.ok("success");
    }
}
