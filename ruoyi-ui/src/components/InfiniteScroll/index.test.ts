import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import InfiniteScroll from './index.vue'

describe('InfiniteScroll 组件测试', () => {
  it('应该导出 InfiniteScroll 组件', () => {
    expect(InfiniteScroll).toBeDefined()
    expect(typeof InfiniteScroll).toBe('object')
  })
})