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
