import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Logo from './Logo.vue'

describe('Sidebar Logo 组件测试', () => {
  it('应该导出 Logo 组件', () => {
    expect(Logo).toBeDefined()
    expect(typeof Logo).toBe('object')
  })
})
