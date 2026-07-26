import request from '@/utils/request'

/**
 * 切换收藏状态
 * @param articleId 文章ID
 */
export function toggleBookmark(articleId: number): Promise<any> {
  return request({
    url: '/blog/bookmark/toggle/' + articleId,
    method: 'post'
  })
}

/**
 * 检查收藏状态
 * @param articleId 文章ID
 */
export function checkBookmark(articleId: number): Promise<any> {
  return request({
    url: '/blog/bookmark/check/' + articleId,
    method: 'get'
  })
}

/**
 * 获取用户收藏列表
 */
export function getBookmarkList(): Promise<any> {
  return request({
    url: '/blog/bookmark/list',
    method: 'get'
  })
}
