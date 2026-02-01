import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import SizeSelect from './index.vue'

describe('SizeSelect 组件测试', () => {
  it('应该导出 SizeSelect 组件', () => {
    expect(SizeSelect).toBeDefined()
    expect(typeof SizeSelect).toBe('object')
  })
})