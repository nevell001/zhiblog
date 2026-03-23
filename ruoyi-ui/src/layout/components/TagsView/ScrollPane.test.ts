import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ScrollPane from './ScrollPane.vue'

describe('ScrollPane 组件测试', () => {
  it('应该导出 ScrollPane 组件', () => {
    expect(ScrollPane).toBeDefined()
    expect(typeof ScrollPane).toBe('object')
  })
})
