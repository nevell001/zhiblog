import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import BlogNav from './BlogNav.vue'

describe('BlogNav 组件测试', () => {
  it('应该导出 BlogNav 组件', () => {
    expect(BlogNav).toBeDefined()
    expect(typeof BlogNav).toBe('object')
  })
})