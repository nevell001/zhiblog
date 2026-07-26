import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Dashboard from './index.vue'

describe('Dashboard 视图组件测试', () => {
  it('应该导出 Dashboard 组件', () => {
    expect(Dashboard).toBeDefined()
    expect(typeof Dashboard).toBe('object')
  })
})
