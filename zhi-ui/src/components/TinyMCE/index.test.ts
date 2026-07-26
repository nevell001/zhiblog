import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import TinyMCE from './index.vue'

describe('TinyMCE 组件测试', () => {
  it('应该导出 TinyMCE 组件', () => {
    expect(TinyMCE).toBeDefined()
    expect(typeof TinyMCE).toBe('object')
  })
})
