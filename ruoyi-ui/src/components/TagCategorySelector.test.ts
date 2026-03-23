import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import TagCategorySelector from './TagCategorySelector.vue'

describe('TagCategorySelector 组件测试', () => {
  it('应该导出 TagCategorySelector 组件', () => {
    expect(TagCategorySelector).toBeDefined()
    expect(typeof TagCategorySelector).toBe('object')
  })
})
