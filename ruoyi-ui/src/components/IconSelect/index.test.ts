import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import IconSelect from './index.vue'

describe('IconSelect 组件测试', () => {
  it('应该导出 IconSelect 组件', () => {
    expect(IconSelect).toBeDefined()
    expect(typeof IconSelect).toBe('object')
  })
})