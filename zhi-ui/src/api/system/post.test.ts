import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as postApi from './post'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('Post API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listPost', () => {
    it('应该导出 listPost 函数', () => {
      expect(postApi.listPost).toBeDefined()
      expect(typeof postApi.listPost).toBe('function')
    })

    it('应该调用岗位列表接口', () => {
      postApi.listPost({ pageNum: 1, pageSize: 10 })
      expect(request).toHaveBeenCalledWith({
        url: '/system/post/list',
        method: 'get',
        params: { pageNum: 1, pageSize: 10 }
      })
    })
  })

  describe('getPost', () => {
    it('应该导出 getPost 函数', () => {
      expect(postApi.getPost).toBeDefined()
      expect(typeof postApi.getPost).toBe('function')
    })

    it('应该调用岗位详情接口', () => {
      postApi.getPost(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/post/123',
        method: 'get'
      })
    })
  })

  describe('addPost', () => {
    it('应该导出 addPost 函数', () => {
      expect(postApi.addPost).toBeDefined()
      expect(typeof postApi.addPost).toBe('function')
    })

    it('应该调用新增岗位接口', () => {
      const postData = { postName: '测试岗位', postCode: 'test' }
      postApi.addPost(postData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/post',
        method: 'post',
        data: postData
      })
    })
  })

  describe('updatePost', () => {
    it('应该导出 updatePost 函数', () => {
      expect(postApi.updatePost).toBeDefined()
      expect(typeof postApi.updatePost).toBe('function')
    })

    it('应该调用修改岗位接口', () => {
      const postData = { postId: 123, postName: '更新的岗位' }
      postApi.updatePost(postData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/post',
        method: 'put',
        data: postData
      })
    })
  })

  describe('delPost', () => {
    it('应该导出 delPost 函数', () => {
      expect(postApi.delPost).toBeDefined()
      expect(typeof postApi.delPost).toBe('function')
    })

    it('应该调用删除岗位接口', () => {
      postApi.delPost(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/post/123',
        method: 'delete'
      })
    })
  })
})
