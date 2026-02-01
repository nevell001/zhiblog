import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import RuoYiDoc from './index.vue'

describe('RuoYi Doc 组件测试', () => {
  it('应该导出 RuoYiDoc 组件', () => {
    expect(RuoYiDoc).toBeDefined()
    expect(typeof RuoYiDoc).toBe('object')
  })
})