import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import HeaderSearch from './index.vue'

describe('HeaderSearch 组件测试', () => {
  it('应该导出 HeaderSearch 组件', () => {
    expect(HeaderSearch).toBeDefined()
    expect(typeof HeaderSearch).toBe('object')
  })
})