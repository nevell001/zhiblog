import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import AuthRole from './authRole.vue'

describe('AuthRole 视图组件测试', () => {
  it('应该导出 AuthRole 组件', () => {
    expect(AuthRole).toBeDefined()
    expect(typeof AuthRole).toBe('object')
  })
})
