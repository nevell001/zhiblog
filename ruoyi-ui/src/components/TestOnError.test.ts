import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import TestOnError from './TestOnError.vue'

describe('TestOnError 组件测试', () => {
  it('应该导出 TestOnError 组件', () => {
    expect(TestOnError).toBeDefined()
    expect(typeof TestOnError).toBe('object')
  })
})
