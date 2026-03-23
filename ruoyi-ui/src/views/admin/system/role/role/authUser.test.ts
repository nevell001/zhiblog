import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import AuthUser from './authUser.vue'

describe('AuthUser 视图组件测试', () => {
  it('应该导出 AuthUser 组件', () => {
    expect(AuthUser).toBeDefined()
    expect(typeof AuthUser).toBe('object')
  })
})
