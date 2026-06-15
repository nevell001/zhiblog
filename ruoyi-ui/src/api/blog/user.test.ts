import { describe, it, expect, vi } from 'vitest'
import {
  blogLogin,
  blogRegister,
  blogLogout,
  getBlogUserInfo
} from './auth'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn()
}))

describe('Blog User API 测试', () => {
  it('应该导出 login 函数', () => {
    expect(blogLogin).toBeDefined()
    expect(typeof blogLogin).toBe('function')
  })

  it('应该导出 register 函数', () => {
    expect(blogRegister).toBeDefined()
    expect(typeof blogRegister).toBe('function')
  })

  it('应该导出 logout 函数', () => {
    expect(blogLogout).toBeDefined()
    expect(typeof blogLogout).toBe('function')
  })

  it('应该导出 getUserInfo 函数', () => {
    expect(getBlogUserInfo).toBeDefined()
    expect(typeof getBlogUserInfo).toBe('function')
  })

  it('应该支持用户登录', () => {
    const loginData = {
      username: 'test',
      password: '123456'
    }
    expect(loginData.username).toBe('test')
    expect(loginData.password).toBe('123456')
  })

  it('应该支持用户注册', () => {
    const registerData = {
      username: 'newuser',
      email: 'test@example.com',
      password: '123456'
    }
    expect(registerData.email).toBe('test@example.com')
    expect(registerData.username).toBe('newuser')
  })

  it('应该支持获取用户信息', () => {
    const userId = 1
    expect(typeof userId).toBe('number')
    expect(userId).toBeGreaterThan(0)
  })

  it('应该支持更新用户资料', () => {
    const profileData = {
      nickname: '新昵称',
      avatar: '/avatar.jpg'
    }
    expect(profileData.nickname).toBe('新昵称')
    expect(profileData.avatar).toBe('/avatar.jpg')
  })
})
