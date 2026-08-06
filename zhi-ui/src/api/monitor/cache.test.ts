import { describe, it, expect, vi, beforeEach } from 'vitest'
import {
  getCache,
  listCacheName,
  listCacheKey,
  getCacheValue,
  clearCacheName,
  clearCacheKey,
  clearCacheAll
} from './cache'
import request from '@/utils/request'

vi.mock('@/utils/request')
const mockRequest = vi.mocked(request)

describe('Monitor Cache API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('getCache 应调用 GET /monitor/cache', () => {
    getCache()
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/monitor/cache', method: 'get' })
    )
  })

  it('listCacheName 应调用 GET /monitor/cache/getNames', () => {
    listCacheName()
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/monitor/cache/getNames', method: 'get' })
    )
  })

  it('listCacheKey 应调用 GET /monitor/cache/getKeys/:name', () => {
    listCacheKey('sys_config')
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/monitor/cache/getKeys/sys_config', method: 'get' })
    )
  })

  it('getCacheValue 应调用 GET /monitor/cache/getValue/:name/:key', () => {
    getCacheValue('sys_config', 'blog_name')
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({
        url: '/monitor/cache/getValue/sys_config/blog_name',
        method: 'get'
      })
    )
  })

  it('clearCacheName 应调用 DELETE /monitor/cache/clearCacheName/:name', () => {
    clearCacheName('sys_config')
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/monitor/cache/clearCacheName/sys_config', method: 'delete' })
    )
  })

  it('clearCacheKey 应调用 DELETE /monitor/cache/clearCacheKey/:key', () => {
    clearCacheKey('blog_name')
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/monitor/cache/clearCacheKey/blog_name', method: 'delete' })
    )
  })

  it('clearCacheAll 应调用 DELETE /monitor/cache/clearCacheAll', () => {
    clearCacheAll()
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/monitor/cache/clearCacheAll', method: 'delete' })
    )
  })
})
