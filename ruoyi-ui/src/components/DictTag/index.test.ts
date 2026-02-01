import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import DictTag from './index.vue'

describe('DictTag 组件测试', () => {
  it('应该导出 DictTag 组件', () => {
    expect(DictTag).toBeDefined()
    expect(typeof DictTag).toBe('object')
  })
})