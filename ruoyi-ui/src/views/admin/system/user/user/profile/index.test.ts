import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Profile from './index.vue'

describe('Profile 视图组件测试', () => {
  it('应该导出 Profile 组件', () => {
    expect(Profile).toBeDefined()
    expect(typeof Profile).toBe('object')
  })
})