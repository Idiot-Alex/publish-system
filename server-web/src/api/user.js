import request from '@/util/request'

// 登录
export function login(userName, userPassword) {
  return request({
    url: '/web/user/login',
    method: 'post',
    data: {
      userName,
      userPassword
    }
  })
}

// 分页查询
export function getUsers(data) {
  return request({
    url: '/web/user/list',
    method: 'post',
    data
  })
}

// 编辑
export function editUser(data) {
  const param = {
    userId: data.userId,
    userName: data.userName,
    userPassword: data.userPassword
  }
  return request({
    url: '/web/user/edit',
    method: 'post',
    data: param
  })
}

// 删除
export function deleteByUserId(userId) {
  return request({
    url: '/web/user/delete',
    method: 'post',
    data: {
      userId
    }
  })
}