import { describe, it, expect } from 'vitest'
import errorCode from './errorCode'

describe('errorCode 工具测试', () => {
  it('应该导出 errorCode 对象', () => {
    expect(errorCode).toBeDefined()
    expect(typeof errorCode).toBe('object')
  })

  it('应该包含所有预定义的错误码', () => {
    expect(errorCode).toHaveProperty('401')
    expect(errorCode).toHaveProperty('403')
    expect(errorCode).toHaveProperty('404')
    expect(errorCode).toHaveProperty('default')
  })

  it('应该返回正确的错误消息', () => {
    expect(errorCode['401']).toBe('认证失败，无法访问系统资源')
    expect(errorCode['403']).toBe('当前操作没有权限')
    expect(errorCode['404']).toBe('访问资源不存在')
    expect(errorCode['default']).toBe('系统未知错误，请反馈给管理员')
  })

  it('应该正确处理不存在的错误码', () => {
    expect(errorCode['999']).toBeUndefined()
    expect(errorCode['unknown']).toBeUndefined()
  })
})
