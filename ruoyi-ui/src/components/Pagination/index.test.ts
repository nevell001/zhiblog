import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Pagination from './index.vue'

describe('Pagination 组件测试', () => {
  it('应该导出 Pagination 组件', () => {
    expect(Pagination).toBeDefined()
    expect(typeof Pagination).toBe('object')
  })

  it('应该有组件名称', () => {
    expect(Pagination.name).toBeTruthy()
  })
})
