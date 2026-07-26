import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import BlogFooter from './BlogFooter.vue'

describe('BlogFooter 组件测试', () => {
  it('应该导出 BlogFooter 组件', () => {
    expect(BlogFooter).toBeDefined()
    expect(typeof BlogFooter).toBe('object')
  })
})
