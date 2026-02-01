import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Hamburger from './index.vue'

describe('Hamburger 组件测试', () => {
  it('应该导出 Hamburger 组件', () => {
    expect(Hamburger).toBeDefined()
    expect(typeof Hamburger).toBe('object')
  })
})