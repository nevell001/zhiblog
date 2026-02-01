import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import BlogSetting from './index.vue'

describe('BlogSetting 视图组件测试', () => {
  it('应该导出 BlogSetting 组件', () => {
    expect(BlogSetting).toBeDefined()
    expect(typeof BlogSetting).toBe('object')
  })
})