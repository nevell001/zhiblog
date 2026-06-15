import { describe, it, expect, vi } from 'vitest'
import {
  login,
  logout,
  getCodeImg,
  getInfo,
  register
} from '../login'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn()
}))

describe('Login API 测试', () => {
  it('应该导出 login 函数', () => {
    expect(login).toBeDefined()
    expect(typeof login).toBe('function')
  })

  it('应该导出 logout 函数', () => {
    expect(logout).toBeDefined()
    expect(typeof logout).toBe('function')
  })

  it('应该导出 getCode 函数', () => {
    expect(getCodeImg).toBeDefined()
    expect(typeof getCodeImg).toBe('function')
  })

  it('应该导出 getInfo 函数', () => {
    expect(getInfo).toBeDefined()
    expect(typeof getInfo).toBe('function')
  })

  it('应该导出 register 函数', () => {
    expect(register).toBeDefined()
    expect(typeof register).toBe('function')
  })
})
