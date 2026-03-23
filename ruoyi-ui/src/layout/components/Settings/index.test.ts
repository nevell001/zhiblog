import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Settings from './index.vue'

describe('Settings 组件测试', () => {
  it('应该导出 Settings 组件', () => {
    expect(Settings).toBeDefined()
    expect(typeof Settings).toBe('object')
  })
})
