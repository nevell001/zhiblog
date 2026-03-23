import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import OperLog from './index.vue'

describe('OperLog 视图组件测试', () => {
  it('应该导出 OperLog 组件', () => {
    expect(OperLog).toBeDefined()
    expect(typeof OperLog).toBe('object')
  })
})
