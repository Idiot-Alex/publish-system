import request from '@/util/request'

// 分页查询
export function getArticles(data) {
  return request({
    url: '/web/article/list',
    method: 'post',
    data
  })
}

// 编辑
export function editArticle(data) {
  const param = {
    articleId: data.articleId,
    title: data.title,
    coverImage: data.coverImage,
    content: data.content,
    editStatus: data.editStatus
  }
  return request({
    url: '/web/article/edit',
    method: 'post',
    data: param
  })
}

// 删除
export function deleteByArticleId(articleId) {
  return request({
    url: '/web/article/delete',
    method: 'post',
    data: {
      articleId
    }
  })
}