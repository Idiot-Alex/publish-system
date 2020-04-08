import request from '@/util/request'

// 查询根目录
export function getRootDirectories() {
  return request({
    url: '/web/directory/root-list',
    method: 'post'
  })
}

// 查询子目录
export function getChildDirectoies(parentDirectoryId) {
  return request({
    url: '/web/directory/child-list',
    method: 'post',
    data: {
      parentDirectoryId
    }
  })
}

// 编辑目录
export function editDirectory(data) {
  const param = {
    directoryId: data.directoryId,
    directoryName: data.directoryName,
    parentDirectoryId: data.parentDirectoryId
  }
  return request({
    url: '/web/directory/edit',
    method: 'post',
    data: param
  })
}

// 删除目录
export function deleteByDirectoryId(directoryId) {
  return request({
    url: '/web/directory/delete',
    method: 'post',
    data: {
      directoryId
    }
  })
}