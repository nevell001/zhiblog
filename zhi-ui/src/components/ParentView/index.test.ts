import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ParentView from './index.vue'

describe('ParentView 组件测试', () => {
  it('应该导出 ParentView 组件', () => {
    expect(ParentView).toBeDefined()
    expect(typeof ParentView).toBe('object')
  })
})
