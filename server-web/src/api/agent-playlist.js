import request from '@/util/request'

// 根据 agentId 查询
export function getAgentPlaylistByAgentId(agentId) {
  return request({
    url: '/web/agent-playlist/list',
    method: 'post',
    data: {
      agentId
    }
  })
}

// 新增
export function addAgentPlaylist(data) {
  const param = {
    listId: data.listId,
    agentId: data.agentId,
    articleId: data.articleId,
    serialNumber: data.serialNumber
  }
  return request({
    url: '/web/agent-playlist/add',
    method: 'post',
    data: param
  })
}

// 修改
export function editAgentPlaylist(data) {
  const param = {
    listId: data.listId,
    agentId: data.agentId,
    articleId: data.articleId,
    serialNumber: data.serialNumber
  }
  return request({
    url: '/web/agent-playlist/edit',
    method: 'post',
    data: param
  })
}

// 删除
export function deleteByListId(listId) {
  return request({
    url: '/web/agent-playlist/delete',
    method: 'post',
    data: {
      listId
    }
  })
}