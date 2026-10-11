import request from '@/utils/request'
import type { Article } from '@/types'
import type {
  ArticleParams,
  QueryResult,
  DataResult,
  ArticleDetailResponse,
  ArticleArchive
} from '@/types/api'

/**
 * 获取文章列表（前台用，包含分类和标签信息，支持分页）
 */
export function getArticleList(query?: ArticleParams): Promise<QueryResult<Article>> {
  return request({
    url: '/blog/api/article/list',
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
    url: '/blog/api/article/hot',
    method: 'get',
    params: { ...query, pageSize: query?.pageSize || 5 },
    headers: { isToken: false }
  })
}

/**
 * 获取文章详情（前台用，包含完整内容和上下篇文章信息）
 */
export function getArticleDetail(id: number): Promise<DataResult<ArticleDetailResponse>> {
  return request({
    url: '/blog/api/article/' + id,
    method: 'get',
    headers: { isToken: false }
  })
}

/**
 * 获取文章归档
 */
export function getArticleArchive(): Promise<DataResult<ArticleArchive[]>> {
  return request({
    url: '/blog/api/article-archive',
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
    url: `/blog/api/article/archive-month/${year}/${month}`,
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
    url: '/blog/api/article/search',
    method: 'get',
    params: { ...query, keyword: keyword },
    headers: { isToken: false }
  })
}

/**
 * 获取相关文章
 */
export function getRelatedArticles(id: number): Promise<DataResult<Article[]>> {
  return request({
    url: '/blog/api/article/related/' + id,
    method: 'get',
    headers: { isToken: false }
  })
}
