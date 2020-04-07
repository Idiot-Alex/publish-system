import request from '@/util/request'

// 分页查询
export function getArticles(data) {
  return request({
    url: '/web/article/list',
    method: 'post',
    data
  })
}