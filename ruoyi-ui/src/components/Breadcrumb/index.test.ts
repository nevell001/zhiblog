import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Breadcrumb from './index.vue'

describe('Breadcrumb 组件测试', () => {
  it('应该导出 Breadcrumb 组件', () => {
    expect(Breadcrumb).toBeDefined()
    expect(typeof Breadcrumb).toBe('object')
  })
})
