import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import TestError from './TestError.vue'

describe('TestError 组件测试', () => {
  it('应该导出 TestError 组件', () => {
    expect(TestError).toBeDefined()
    expect(typeof TestError).toBe('object')
  })
})