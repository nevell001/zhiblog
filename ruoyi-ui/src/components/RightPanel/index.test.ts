import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import RightPanel from './index.vue'

describe('RightPanel 组件测试', () => {
  it('应该导出 RightPanel 组件', () => {
    expect(RightPanel).toBeDefined()
    expect(typeof RightPanel).toBe('object')
  })
})
