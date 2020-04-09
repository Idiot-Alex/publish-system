import request from '@/util/request'

// 分页查询 所有终端
export function getAgents(data) {
  return request({
    url: '/web/agent/list',
    method: 'post',
    data
  })
}

// 分页查询 用户拥有终端
export function getUserAgents(data) {
  return request({
    url: '/web/agent/user-list',
    method: 'post',
    data
  })
}

// 编辑终端
export function editAgent(data) {
  const param = {
    agentId: data.agentId,
    agentName: data.agentName,
    agentCode: data.agentCode,
    heartbeatFrequency: data.heartbeatFrequency
  }
  return request({
    url: '/web/agent/edit',
    method: 'post',
    data: param
  })
}

// 删除
export function deleteByAgentId(agentId) {
  return request({
    url: '/web/agent/delete',
    method: 'post',
    data: {
      agentId
    }
  })
}

// 分配用户终端
export function grantUserAgents(data) {
  return request({
    url: '/web/agent/grant-user-agents',
    method: 'post',
    data
  })
}