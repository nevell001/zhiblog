import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import TagsView from './index.vue'

describe('TagsView 组件测试', () => {
  it('应该导出 TagsView 组件', () => {
    expect(TagsView).toBeDefined()
    expect(typeof TagsView).toBe('object')
  })
})
