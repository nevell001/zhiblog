import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import LoginLog from './index.vue'

describe('LoginLog 视图组件测试', () => {
  it('应该导出 LoginLog 组件', () => {
    expect(LoginLog).toBeDefined()
    expect(typeof LoginLog).toBe('object')
  })
})
