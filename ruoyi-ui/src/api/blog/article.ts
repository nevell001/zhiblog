import request from '@/utils/request'
import type { Article } from '@/types'
import type { ArticleParams, QueryResult } from '@/types/api'

/**
 * 查询博客文章列表
 */
export function listArticle(query?: ArticleParams): Promise<QueryResult<Article>> {
  return request({
    url: '/system/article/list',
    method: 'get',
    params: query
  })
}

/**
 * 前台匿名访问文章列表
 */
export function getArticleListAnonymous(query?: ArticleParams): Promise<QueryResult<Article>> {
  return request({
    url: '/blog/article/list',
    method: 'get',
    params: query,
    headers: { isToken: false }
  })
}

/**
 * 查询博客文章详细
 */
export function getArticle(id: number): Promise<Article> {
  return request({
    url: '/system/article/' + id,
    method: 'get'
  })
}

/**
 * 新增博客文章
 */
export function addArticle(data: Article): Promise<any> {
  return request({
    url: '/system/article',
    method: 'post',
    data: data
  })
}

/**
 * 修改博客文章
 */
export function updateArticle(data: Article): Promise<any> {
  return request({
    url: '/system/article',
    method: 'put',
    data: data
  })
}

/**
 * 删除博客文章
 */
export function delArticle(id: number): Promise<any> {
  return request({
    url: '/system/article/' + id,
    method: 'delete'
  })
}

/**
 * 增加浏览量
 */
export function addViewCount(id: number): Promise<any> {
  return request({
    url: '/system/article/view/' + id,
    method: 'get'
  })
}

/**
 * 获取文章列表（前台用，包含分类和标签信息，支持分页）
 */
export function getArticleList(query?: ArticleParams): Promise<QueryResult<Article>> {
  return request({
    url: '/blog/article/list',
    method: 'get',
    params: query,
    headers: { isToken: false }
  })
}

/**
 * 根据分类获取文章列表（前台用，支持分页）
 */
export function getArticlesByCategory(
  categoryId: number,
  query?: ArticleParams
): Promise<QueryResult<Article>> {
  return request({
    url: '/blog/article/category/' + categoryId,
    method: 'get',
    params: query,
    headers: { isToken: false }
  })
}

/**
 * 获取热门文章（支持分页）
 */
export function getHotArticles(query?: ArticleParams): Promise<QueryResult<Article>> {
  return request({
    url: '/blog/article/hot',
    method: 'get',
    params: { ...query, pageSize: query?.pageSize || 5 },
    headers: { isToken: false }
  })
}

/**
 * 获取置顶文章
 */
export function getTopArticles(query?: ArticleParams): Promise<QueryResult<Article>> {
  return request({
    url: '/common/blog/article/top',
    method: 'get',
    params: { ...query, pageSize: query?.pageSize || 5 },
    headers: { isToken: false }
  })
}

/**
 * 获取推荐文章
 */
export function getRecommendArticles(query?: ArticleParams): Promise<QueryResult<Article>> {
  return request({
    url: '/common/blog/article/recommend',
    method: 'get',
    params: { ...query, pageSize: query?.pageSize || 5 },
    headers: { isToken: false }
  })
}

/**
 * 获取文章详情（前台用，包含完整内容和上下篇文章信息）
 */
export function getArticleDetail(id: number): Promise<any> {
  return request({
    url: '/blog/article/' + id,
    method: 'get',
    headers: { isToken: false }
  })
}

/**
 * 更新文章浏览量
 */
export function updateArticleViewCount(id: number): Promise<any> {
  return request({
    url: '/blog/article/view/' + id,
    method: 'get',
    headers: { isToken: false }
  })
}

/**
 * 获取文章归档
 */
export function getArticleArchive(): Promise<any> {
  return request({
    url: '/blog/article-archive',
    method: 'get',
    headers: { isToken: false }
  })
}

/**
 * 根据归档月份获取文章列表
 */
export function getArticlesByArchive(
  year: number,
  month: number,
  query?: ArticleParams
): Promise<QueryResult<Article>> {
  return request({
    url: `/blog/article/archive-month/${year}/${month}`,
    method: 'get',
    params: query,
    headers: { isToken: false }
  })
}

/**
 * 搜索文章（支持分页）
 */
export function searchArticles(
  keyword: string,
  query?: ArticleParams
): Promise<QueryResult<Article>> {
  return request({
    url: '/blog/article/search',
    method: 'get',
    params: { ...query, keyword: keyword },
    headers: { isToken: false }
  })
}

/**
 * 获取相关文章
 */
export function getRelatedArticles(id: number): Promise<Article[]> {
  return request({
    url: '/blog/article/related/' + id,
    method: 'get',
    headers: { isToken: false }
  })
}

/**
 * 提交评论
 */
export function submitComment(data: Record<string, any>): Promise<any> {
  return request({
    url: '/blog/comment',
    method: 'post',
    data: data
  })
}
