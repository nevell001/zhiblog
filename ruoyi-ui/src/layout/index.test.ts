import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Layout from './index.vue'

describe('Layout 组件测试', () => {
  it('应该导出 Layout 组件', () => {
    expect(Layout).toBeDefined()
    expect(typeof Layout).toBe('object')
  })
})