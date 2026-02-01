import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import RightToolbar from './index.vue'

describe('RightToolbar 组件测试', () => {
  it('应该导出 RightToolbar 组件', () => {
    expect(RightToolbar).toBeDefined()
    expect(typeof RightToolbar).toBe('object')
  })
})