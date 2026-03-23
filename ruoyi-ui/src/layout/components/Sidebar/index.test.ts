import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Sidebar from './index.vue'

describe('Sidebar 组件测试', () => {
  it('应该导出 Sidebar 组件', () => {
    expect(Sidebar).toBeDefined()
    expect(typeof Sidebar).toBe('object')
  })
})
