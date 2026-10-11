import request from '@/utils/request'
import type { Comment, PageParams } from '@/types'

/**
 * 获取文章评论列表（前台用：顶级评论分页，回复随楼层返回；sort 支持 newest/oldest）
 */
export function getArticleComments(
  articleId: number,
  query?: PageParams & { sort?: 'newest' | 'oldest' }
): Promise<any> {
  return request({
    url: '/blog/comment/article/' + articleId,
    method: 'get',
    params: query
  })
}

/**
 * 添加评论（前台用）
 */
export function addBlogComment(data: Comment): Promise<any> {
  return request({
    url: '/blog/comment',
    method: 'post',
    data: data
  })
}

/**
 * 修改本人评论（前台用，需登录 + 服务端归属校验）
 */
export function updateMyComment(id: number, data: { content: string }): Promise<any> {
  return request({
    url: '/blog/comment/' + id,
    method: 'put',
    data: data
  })
}

/**
 * 删除本人评论（前台用，需登录 + 服务端归属校验）
 */
export function deleteMyComment(id: number): Promise<any> {
  return request({
    url: '/blog/comment/' + id,
    method: 'delete'
  })
}
