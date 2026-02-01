import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import TopNav from './index.vue'

describe('TopNav 组件测试', () => {
  it('应该导出 TopNav 组件', () => {
    expect(TopNav).toBeDefined()
    expect(typeof TopNav).toBe('object')
  })
})