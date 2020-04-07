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