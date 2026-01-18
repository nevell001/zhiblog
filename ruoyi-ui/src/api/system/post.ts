import request from '@/utils/request'
import type { Post, PostParams, QueryResult } from '@/types'

/**
 * 查询岗位列表
 */
export function listPost(query?: PostParams): Promise<QueryResult<Post>> {
  return request({
    url: '/system/post/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询岗位详细
 */
export function getPost(postId: number): Promise<Post> {
  return request({
    url: '/system/post/' + postId,
    method: 'get'
  })
}

/**
 * 新增岗位
 */
export function addPost(data: Post): Promise<any> {
  return request({
    url: '/system/post',
    method: 'post',
    data: data
  })
}

/**
 * 修改岗位
 */
export function updatePost(data: Post): Promise<any> {
  return request({
    url: '/system/post',
    method: 'put',
    data: data
  })
}

/**
 * 删除岗位
 */
export function delPost(postId: number): Promise<any> {
  return request({
    url: '/system/post/' + postId,
    method: 'delete'
  })
}
