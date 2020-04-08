import request from '@/util/request'

// 分页查询
export function getAgents(data) {
  return request({
    url: '/web/agent/list',
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