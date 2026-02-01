import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import IframeToggle from './index.vue'

describe('IframeToggle 组件测试', () => {
  it('应该导出 IframeToggle 组件', () => {
    expect(IframeToggle).toBeDefined()
    expect(typeof IframeToggle).toBe('object')
  })
})