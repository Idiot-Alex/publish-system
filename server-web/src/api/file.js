import request from '@/util/request'

// 分页查询
export function getFiles(data) {
  return request({
    url: '/web/file/list',
    method: 'post',
    data
  })
}

// 删除
export function deleteFile(fileId) {
  return request({
    url: '/web/file/delete',
    method: 'post',
    data: {
      fileId
    }
  })
}