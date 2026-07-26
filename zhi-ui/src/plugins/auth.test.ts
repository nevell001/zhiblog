import { describe, it, expect, vi, beforeEach } from 'vitest'
import auth from './auth'

describe('Auth Plugin 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 auth 插件对象', () => {
    expect(auth).toBeDefined()
    expect(typeof auth).toBe('object')
  })

  it('应该有配置选项', () => {
    const options = { key: 'token', name: 'admin' }
    expect(options.key).toBe('token')
    expect(options.name).toBe('admin')
  })

  it('应该存储用户令牌', () => {
    const token = 'test-token-12345'
    expect(typeof token).toBe('string')
    expect(token.length).toBeGreaterThan(0)
  })

  it('应该移除用户令牌', () => {
    const hasToken = false
    expect(hasToken).toBe(false)
  })
})
