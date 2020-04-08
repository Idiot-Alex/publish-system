import request from '@/util/request'

// 分页查询
export function getFiles(data) {
  return request({
    url: '/web/file/list',
    method: 'post',
    data
  })
}

// 编辑
export function editFile(data) {
  const param = {
    fileId: data.fileId,
    oldName: data.oldName
  }
  return request({
    url: '/web/file/edit',
    method: 'post',
    data: param
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